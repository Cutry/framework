package com.bluesky.framework.api.controller.region;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.constant.AdministrativeLevel;
import com.bluesky.framework.account.constant.OperateLogStep;
import com.bluesky.framework.account.constant.RoleType;
import com.bluesky.framework.account.log.AccountOperateLog;
import com.bluesky.framework.account.log.AccountOperateLogManager;
import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.account.organization.OrganizationManager;
import com.bluesky.framework.account.region.Region;
import com.bluesky.framework.account.region.RegionManager;
import com.bluesky.framework.api.controller.common.IPUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 区域管理
 * 只有系统管理员才能修改
 */
@RestController
@RequestMapping("/regions")
class RegionController {
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private AccountOperateLogManager accountOperateLogManager;
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 查询省市区是否已经初始化
     */
    @GetMapping("/is_init")
    @PreAuthorize("hasPermission('crm-api', 'om_region_view')")
    public DataResponse isInit() {
        DataResponse result = new DataResponse();
        result.addData("isInit", regionManager.isInit());
        return result;
    }


    /**
     * 查询系统级别
     */
    @GetMapping("/get_system_level")
    @PreAuthorize("hasPermission('crm-api', 'om_region_view')")
    public DataResponse getSystemLevel() {
        DataResponse result = new DataResponse();
        result.addData("systemLevel", regionManager.findSystemLevel());
        return result;
    }


    @PutMapping("/update_system_level")
    @PreAuthorize("hasPermission('crm-api', 'om_region_manage')")
    public DataResponse updateSystemLevel(@AuthenticationPrincipal Account account, HttpServletRequest request, @RequestParam int newLevel) {
        DataResponse result = new DataResponse();
        if (!isLevelLegal(newLevel, result)) return result;
        List<Region> list = regionManager.findByLevel(newLevel);
        if (list.isEmpty()) {
            result.setFalseAndMessage(AdministrativeLevel.getName(newLevel) + "行政区不存在，无法选择");
            return result;
        }
        regionManager.updateSystemLevel(newLevel);

        // 记录日志
        this.logOperateRegion(account, OperateLogStep.REGION_UPDATE_SYSTEM_LEVEL.getCode(), request);

        result.setMessage("操作成功");
        return result;
    }


    /**
     * 增加省市区
     *
     * @param parentId 父级区域的id,省级为顶级，parentId传null
     * @param name     区域名称
     * @param code     行政区域代码
     */
    @PostMapping("/add")
    @PreAuthorize("hasPermission('crm-api', 'om_region_manage')")
    public DataResponse add(@AuthenticationPrincipal Account account, @RequestParam long parentId, @RequestParam String name, @RequestParam String code, HttpServletRequest request) {
        DataResponse result = new DataResponse();
        // 是否已经初始化过
        boolean isInit = regionManager.isInit();
        if (!isInit && parentId != 0L) {
            result.setFalseAndMessage("请从省级区域开始创建");
            return result;
        }

        Region parentRegion;
        if (parentId != 0L) {
            parentRegion = regionManager.findOne(parentId);
            if (parentRegion == null) {
                result.setFalseAndMessage("父级区域选择错误");
                return result;
            }
        } else parentRegion = null;

        // 校验区域代码
        if (!isCodeLegal(code, result)) return result;
        // 校验名称
        if (!isNameLegal(name, result)) return result;

        // 新区域的等级=父区域等级+1
        int newLevel;
        if (parentRegion != null) {
            newLevel = parentRegion.getLevel() + 1;
        } else {
            newLevel = AdministrativeLevel.PROVINCIAL_LEVEL.getCode();
        }
        if (!isLevelLegal(newLevel, result)) return result;

        boolean top;
        if (!isInit) top = true;
        else {
            // 查询系统级别
            int systemLevel = regionManager.findSystemLevel();
            // 如果当前创建的等级就是系统级别，则把top置为true
            top = systemLevel == newLevel;
        }

        Region region = Region.builder().name(name).level(newLevel).code(code).parentId(parentRegion == null ? 0 : parentRegion.getId()).top(top).leaf(true).build();
        long id = regionManager.insert(region);
        result.addData("id", id);

        // 修改父级为非末节点,刚刚新建的才是末节点
        if (parentRegion != null) {
            Region update = Region.builder().id(parentRegion.getId()).leaf(false).build();
            regionManager.update(update);
        }

        // 记录日志
        this.logOperateRegion(account, OperateLogStep.REGION_ADD.getCode(), request);

        return result;
    }


    @Async
    public void logOperateRegion(Account account, String logStep, HttpServletRequest request) {
        Date date = new Date();
        String now = sdf.format(date);
        String ip = IPUtils.getIpAddr(request);
        String memo = "区域管理，步骤名称：" + OperateLogStep.getName(logStep) + "，步骤编码：" + logStep + "，操作人：" + account.getName() + "，操作ip：" + ip + ",操作时间：" + now;
        AccountOperateLog accountOperateLog = AccountOperateLog
                .builder()
                .accountId(account.getId())
                .accountName(account.getName())
                .mobile(account.getMobile())
                .ip(ip)
                .stepCode(logStep)
                .step(OperateLogStep.getName(logStep))
                .createdAt(date)
                .memo(memo)
                .build();
        accountOperateLogManager.insert(accountOperateLog);
    }


    /**
     * 修改名称或区域代码
     */
    @PutMapping("/{id}/update")
    @PreAuthorize("hasPermission('crm-api', 'om_region_manage')")
    public DataResponse updateName(@AuthenticationPrincipal Account account, HttpServletRequest request, @PathVariable long id, @RequestParam String name, @RequestParam String code) {
        DataResponse result = new DataResponse();
        Region region = regionManager.findOne(id);
        if (region == null) {
            return result.setFalseAndMessage("区域选择错误择错误");
        }
        // 校验区域代码
        if (code.isEmpty() || code.length() > 15) {
            return result.setFalseAndMessage("区域代码长度为空或超出");
        }
        boolean exist = regionManager.isCodeExist(code);
        if (!Objects.equals(code, region.getCode()) && exist) {
            return result.setFalseAndMessage("区域代码重复");
        }
        // 校验名称
        if (!isNameLegal(name, result)) return result;

        Region newRegion = Region.builder().id(id).name(name).code(code).build();
        regionManager.update(newRegion);

        // 记录日志
        this.logOperateRegion(account, OperateLogStep.REGION_UPDATE.getCode(), request);

        return result;
    }


    @PutMapping("/{id}/delete")
    @PreAuthorize("hasPermission('crm-api', 'om_region_manage')")
    public DataResponse delete(@AuthenticationPrincipal Account account, @PathVariable long id, HttpServletRequest request) {
        DataResponse result = new DataResponse();
        Region region = regionManager.findOne(id);
        if (region == null) {
            return result.setFalseAndMessage("区域选择错误");
        }

        List<Region> children = regionManager.findByParentId(id);
        if (!children.isEmpty()) return result.setFalseAndMessage("请从末级开始删除");

        // 查询区域下是否存在单位，存在单位则不能删除
        Page<Organization> organizations = organizationManager.findPageByCondition(id, null, 1, 1);
        if (!organizations.getList().isEmpty()) {
            return result.setFalseAndMessage("当前区域下存在单位，请先删除单位");
        }
        regionManager.delete(id);

        // 记录日志
        this.logOperateRegion(account, OperateLogStep.REGION_DELETE.getCode(), request);

        result.setMessage("删除成功");
        return result;
    }

    /**
     * 查询所有系统级别下的下级的树状结构
     *
     * @param account          账户
     * @param underSystemLevel false时市级系统会返回：省->市->区。true时只查系统级别下的：市->区
     */
    @GetMapping("/all_tree_under_level")
    public DataResponse getAllRegionUnderLevelLikeTree(@AuthenticationPrincipal Account account, @RequestParam(required = false, defaultValue = "false") boolean underSystemLevel) {
        DataResponse result = new DataResponse();

        // 1.如果系统管理员
        if (Objects.equals(account.getRoleType(), RoleType.PLATFORM.getCode())) {
            result.addData("allRegionTreeUnderLevel", regionManager.findAllTree(underSystemLevel));
            return result;
        }

        // 2. 如果非系统管理员，先查询当前部门id，查询省id,再递归往上查
        // 用户所在单位
        Organization organization = organizationManager.findOne(account.getOrganizationId());
        result.addData("allRegionTreeUnderLevel", Collections.singleton(regionManager.findParentTree(organization.getRegionId(), underSystemLevel)));
        return result;
    }


    /**
     * 根据id查询所有的子节点
     *
     * @param parentId 如果查询省级部门则传：0
     */
    @GetMapping("/find_children")
    public DataResponse findChildren(@AuthenticationPrincipal Account account, @RequestParam long parentId) {
        DataResponse result = new DataResponse();

        // 1.如果系统管理员
        if (Objects.equals(account.getRoleType(), RoleType.PLATFORM.getCode())) {
            result.addData("children", regionManager.findByParentId(parentId));
            return result;
        }

        // 当前用户单位
        Organization organization = organizationManager.findOne(account.getOrganizationId());
        // 当前用户单位所在省市区
        Region currentRegion = regionManager.findOne(organization.getRegionId());

        // 如果parentId = 0 查询当前省
        if (parentId == 0L) {
            List<Region> children = regionManager.findParentList(currentRegion.getId(), false);
            return result.addData("children", Collections.singleton(children.get(0)));
        }

        Region queryRegion = regionManager.findOne(parentId);
        if (queryRegion == null) {
            return result.setFalseAndMessage("区域不存在");
        }

        // 如果待查询区域等级level>当前用户等级level，判断当前用户区域是否是待查询区域等级的上级，上上级
        if (queryRegion.getLevel() > currentRegion.getLevel()) {
            long queryRegionId = queryRegion.getId();
            List<Long> parentIdsList = regionManager.findParentList(queryRegionId, false).stream().map(Region::getId).collect(Collectors.toList());

            if (parentIdsList.contains(currentRegion.getId())) {
                return result.addData("children", Collections.emptyList());
            }

            List<Region> children = regionManager.findByParentId(queryRegionId);
            result.addData("children", children);
        } else {
            // 如果待查询区域等级level<当前用户等级level，判断待查询区域是否是当前用户区域的上级，上上级
            long currentRegionId = currentRegion.getId();
            List<Long> parentIdsList = regionManager.findParentList(currentRegionId, false).stream().map(Region::getId).collect(Collectors.toList());
            if (parentIdsList.contains(queryRegion.getId())) {
                return result.addData("children", Collections.emptyList());
            }
            int queryIndex = parentIdsList.indexOf(queryRegion.getId());
            int currentIndex = parentIdsList.indexOf(currentRegionId);
            if (queryIndex < currentIndex) {
                Region child = regionManager.findOne(parentIdsList.get(queryIndex + 1));
                result.addData("children", Collections.singleton(child));
            } else {
                List<Region> children = regionManager.findByParentId(queryRegion.getId());
                result.addData("children", children);
            }
        }

        return result;
    }


    /**
     * 校验code合法
     *
     * @param code   行政区域代码
     * @param result 返回结果
     * @return true合法，false不合法
     */
    private boolean isCodeLegal(String code, DataResponse result) {
        if (code.isEmpty() || code.length() > 15) {
            result.setFalseAndMessage("区域代码长度为空或超出");
            return false;
        }
        boolean exist = regionManager.isCodeExist(code);
        if (exist) {
            result.setFalseAndMessage("区域代码重复");
            return false;
        }
        return true;
    }


    /**
     * 校验名称合法
     *
     * @param name   区域名称
     * @param result 返回结果
     * @return true合法，false不合法
     */
    private boolean isNameLegal(String name, DataResponse result) {
        if (!name.isEmpty() && name.length() < 30) {
            return true;
        }

        result.setFalseAndMessage("名称超出长度");
        return false;
    }


    /**
     * 校验行政级别是否合法
     *
     * @param level  区域级别
     * @param result 返回结果
     * @return true合法，false不合法
     */
    private boolean isLevelLegal(int level, DataResponse result) {
        if (AdministrativeLevel.isCodeLegal(level)) {
            return true;
        }
        result.setFalseAndMessage("行政级别选择错误");
        return false;
    }
}