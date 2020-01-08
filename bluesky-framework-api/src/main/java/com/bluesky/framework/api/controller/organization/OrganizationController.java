package com.bluesky.framework.api.controller.organization;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.account.AccountManager;
import com.bluesky.framework.account.constant.OperateLogStep;
import com.bluesky.framework.account.constant.OrganizationStatus;
import com.bluesky.framework.account.constant.RegionStatus;
import com.bluesky.framework.account.constant.RoleType;
import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.account.organization.OrganizationManager;
import com.bluesky.framework.account.region.Region;
import com.bluesky.framework.account.region.RegionManager;
import com.bluesky.framework.api.controller.common.IPUtils;
import com.bluesky.framework.api.event.LogEventProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 只有系统管理员可以修改
 * 单位管理
 */
@RestController
@RequestMapping("/organization")
public class OrganizationController {
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private LogEventProcessor logEventProcessor;


    /**
     * 新增单位
     */
    @PostMapping("/add")
    @PreAuthorize("hasPermission('crm-api','om_org_add')")
    public DataResponse add(@AuthenticationPrincipal Account account, HttpServletRequest request, @RequestParam String code,
                            @RequestParam String fullName, @RequestParam String name, @RequestParam(required = false) String unifiedSocialCreditCode,
                            @RequestParam("parentId") Long parentId, @RequestParam Long regionId, @RequestParam Integer status,
                            @RequestParam Integer sort, @RequestParam(required = false) String memo) {
        DataResponse result = new DataResponse();
        // 校验
        if (!isCodeLegal(code, result)) {
            return result;
        }
        if (!isFullNameLegal(fullName, result)) {
            return result;
        }
        if (!isNameLegal(name, result)) {
            return result;
        }
        if (!isUnifiedSocialCreditCode(unifiedSocialCreditCode, result)) {
            return result;
        }
        if (!isStatusLegal(status, result)) {
            return result;
        }
        if (!isSortLegal(sort, result)) {
            return result;
        }
        if (!isMemoLegal(memo, result)) {
            return result;
        }
        if (parentId != null && !isParentIdLegal(parentId, result)) {
            return result;
        }

        Region region = isRegionIdLegal(regionId, result);
        if (region == null) {
            return result;
        }
        String areaCode = region.getCode();
        Organization organization = Organization.builder().code(code).name(name).fullName(fullName).unifiedSocialCreditCode(unifiedSocialCreditCode).
                regionId(regionId).areaCode(areaCode).parentId(parentId).status(status).sort(sort).memo(memo).build();
        Long id = organizationManager.insert(organization);

        result.addData("id", id);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, OperateLogStep.ORGANIZATION_ADD.getCode(), ip);

        return result;
    }


    /**
     * 修改单位
     */
    @PutMapping("/{id}/update")
    @PreAuthorize("hasPermission('crm-api','om_org_update')")
    public DataResponse update(@AuthenticationPrincipal Account account, HttpServletRequest request,
                               @PathVariable Long id, @RequestParam String code, @RequestParam String fullName,
                               @RequestParam String name, @RequestParam(required = false) String unifiedSocialCreditCode,
                               @RequestParam("parentId") Long parentId, @RequestParam(required = false) Long regionId,
                               @RequestParam Integer sort, @RequestParam(required = false) String memo) {
        DataResponse result = new DataResponse();
        // 校验
        if (!isCodeLegal(code, result)) {
            return result;
        }
        if (!isFullNameLegal(fullName, result)) {
            return result;
        }
        if (!isNameLegal(name, result)) {
            return result;
        }
        if (!isUnifiedSocialCreditCode(unifiedSocialCreditCode, result)) {
            return result;
        }
        if (!isSortLegal(sort, result)) {
            return result;
        }
        if (!isMemoLegal(memo, result)) {
            return result;
        }
        if (parentId != null && !isParentIdLegal(parentId, result)) {
            return result;
        }
        // 查询区域
        Region region = null;
        if (regionId != null) {
            region = isRegionIdLegal(regionId, result);
            if (region == null) {
                return result.setFalseAndMessage("所选择区域不存在");
            }
        }
        String areaCode = region != null ? region.getCode() : "";
        Organization organization = Organization.builder().id(id).code(code).name(name).fullName(fullName).unifiedSocialCreditCode(unifiedSocialCreditCode).
                regionId(regionId).areaCode(areaCode).parentId(parentId).sort(sort).memo(memo).build();
        organizationManager.update(organization);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, OperateLogStep.ORGANIZATION_UPDATE.getCode(), ip);

        return result;
    }


    @PostMapping("/{id}/delete")
    @PreAuthorize("hasPermission('crm-api','om_org_delete')")
    public DataResponse delete(@AuthenticationPrincipal Account account, HttpServletRequest request, @PathVariable Long id) {
        DataResponse result = new DataResponse();
        Organization organization = organizationManager.findOne(id);
        if (organization == null) {
            return result.setFalseAndMessage("单位不存在");
        }
        if (organization.getStatus() == OrganizationStatus.DELETE.getCode()) {
            return result.setFalseAndMessage("单位已删除,请刷新页面重试!");
        }

        Page<Account> accountPage = accountManager.findPageByCondition(null, null, id, null, null, 1, 1);
        if (!accountPage.getList().isEmpty()) {
            result.setFalseAndMessage("该单位下存在用户，请在删除用户后删除该单位");
        }

        // 删除
        organizationManager.update(Organization.builder().id(id).status(OrganizationStatus.DELETE.getCode()).build());

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, OperateLogStep.ORGANIZATION_DELETE.getCode(), ip);

        return result;
    }


    /**
     * 启用单位
     */
    @PostMapping("/{id}/enable")
    @PreAuthorize("hasPermission('crm-api','om_org_open')")
    public DataResponse enable(@AuthenticationPrincipal Account account, HttpServletRequest request, @PathVariable Long id) {
        DataResponse result = new DataResponse();
        Organization organization = organizationManager.findOne(id);
        if (organization == null || organization.getStatus() == OrganizationStatus.DELETE.getCode()) {
            return result.setFalseAndMessage("单位不存在");
        }
        if (organization.getStatus() == OrganizationStatus.ENABLE.getCode()) {
            return result.setFalseAndMessage("已启用,请刷新页面重试!");
        }

        organizationManager.update(Organization.builder().id(id).status(OrganizationStatus.ENABLE.getCode()).build());

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, OperateLogStep.ORGANIZATION_ENABLE.getCode(), ip);
        return result;
    }


    /**
     * 停用单位
     */
    @PostMapping("/{id}/disable")
    @PreAuthorize("hasPermission('crm-api','om_org_open')")
    public DataResponse disable(@AuthenticationPrincipal Account account, HttpServletRequest request, @PathVariable Long id) {
        DataResponse result = new DataResponse();
        Organization organization = organizationManager.findOne(id);
        if (organization == null || organization.getStatus() == OrganizationStatus.DELETE.getCode()) {
            return result.setFalseAndMessage("单位不存在");
        }
        if (organization.getStatus() == OrganizationStatus.DISABLE.getCode()) {
            return result.setFalseAndMessage("已停用,请刷新页面重试!");
        }


        Page<Account> accountPage = accountManager.findPageByCondition(null, null, id, null, null, 1, 1);
        if (!accountPage.getList().isEmpty()) {
            result.setFalseAndMessage("该单位下存在用户,不能停用!");
        }

        organizationManager.update(Organization.builder().id(id).status(OrganizationStatus.DISABLE.getCode()).build());

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, OperateLogStep.ORGANIZATION_DISABLE.getCode(), ip);
        return result;
    }


    /**
     * 分页根据省市区id查询单位
     */
    @GetMapping("/page")
    public DataResponse findPage(@AuthenticationPrincipal Account account, @RequestParam Long regionId,
                                 @RequestParam(required = false) String name,
                                 @RequestParam(required = false, defaultValue = "1") Integer pageNum) {
        DataResponse result = new DataResponse();
        Region region = regionManager.findOne(regionId);
        if (region == null) {
            return result.setFalseAndMessage("区域选择错误，请重新选择");
        }

        // 管理员查询全部，非管理员只能查询当前单位
        Page<Organization> page;
        if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
            page = organizationManager.findPageByCondition(regionId, name, pageNum, 10);
        } else {
            Organization organization = organizationManager.findOne(account.getOrganizationId());
            if (organization.getRegionId().equals(regionId)) {
                page = new Page<>(1, 1, 1, 1, 1, Collections.singletonList(organization));
            } else {
                page = new Page<>(0, 1, 0, 0, 0, Collections.emptyList());
            }
        }

        packageRichOrganization(page.getList());
        result.addData("organizations", page);
        return result;
    }


    /**
     * 不分页根据省市区查询单位
     */
    @GetMapping("/list")
    public DataResponse findList(@AuthenticationPrincipal Account account, @RequestParam Long regionId,
                                 @RequestParam(required = false) String name) {
        DataResponse result = new DataResponse();
        Region region = regionManager.findOne(regionId);
        if (region == null) {
            return result.setFalseAndMessage("区域选择错误，请重新选择");
        }

        // 管理员查询全部，非管理员只能查询当前单位
        List<Organization> list;
        if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
            list = organizationManager.findListByCondition(regionId, name);
        } else {
            Organization organization = organizationManager.findOne(account.getOrganizationId());
            if (organization.getRegionId().equals(regionId)) {
                list = Collections.singletonList(organization);
            } else {
                list = Collections.emptyList();
            }
        }
        result.addData("organizations", list);
        return result;
    }


    /**
     * 根据id查询下级部门
     *
     * @param account        账号
     * @param organizationId 单位id
     */
    @GetMapping("/{organizationId}/children")
    @PreAuthorize("hasPermission('crm-api','om_org_view')")
    public DataResponse findChildren(@AuthenticationPrincipal Account account, @PathVariable Long organizationId) {
        DataResponse result = new DataResponse();
        Organization organization = organizationManager.findOne(organizationId);
        if (organization == null) {
            return result.setFalseAndMessage("单位不存在");
        }
        if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
            List<Organization> list = organizationManager.findChildren(organizationId);
            result.addData("children", list);
            return result;
        }

        List<Long> parentIds = organizationManager.findParentList(organizationId).stream().map(Organization::getId).collect(Collectors.toList());
        if (parentIds.contains(account.getOrganizationId())) {
            return result.setFalseAndMessage("您没有权限进行此操作");
        }
        List<Organization> list = organizationManager.findChildren(organizationId);
        result.addData("children", list);
        return result;
    }

    /**
     * 查询当前用户所在单位
     */
    @GetMapping("/current_organization")
    @PreAuthorize("hasPermission('crm-api','om_org_view')")
    public DataResponse findCurrentOrganization(@AuthenticationPrincipal Account account) {
        DataResponse result = new DataResponse();
        if (account.getOrganizationId() == null) {
            return result.setFalseAndMessage("当前用户没有单位");
        }
        Organization organization = organizationManager.findOne(account.getOrganizationId());
        Organization parent = organizationManager.findOne(organization.getParentId());
        organization.setParentName(parent != null ? parent.getName() : null);
        result.addData("organization", organization);
        return result;
    }

    /**
     * 组装单位信息
     *
     * @param organizations 原始单位信息
     * @return 组装了其他详细信息的单位信息
     */
    private void packageRichOrganization(List<Organization> organizations) {
        if (organizations.isEmpty()) {
            return;
        }
        // 查询所有上级单位，组装上级单位名称
        List<Long> parentIds = organizations.stream().filter(organization -> organization.getParentId() != null).map(Organization::getParentId).collect(Collectors.toList());
        List<Organization> parents = organizationManager.findByIds(parentIds);
        Map<Long, String> idNameMap = parents.stream().collect(Collectors.toMap(Organization::getId, Organization::getName));

        organizations.forEach(organization -> {
            organization.setParentName(organization.getParentId() != null ? idNameMap.get(organization.getParentId()) : null);
        });
    }

    /**
     * 部门编码是否合法
     *
     * @param code   部门编码
     * @param result 结果
     */
    private Boolean isCodeLegal(String code, DataResponse result) {
        if (StringUtils.isBlank(code) || code.length() < 30) {
            return true;
        }
        result.setFalseAndMessage("部门编码超出长度");
        return false;
    }


    /**
     * 单位全名是否合法
     *
     * @param fullName 部门编码
     * @param result   结果
     */
    private Boolean isFullNameLegal(String fullName, DataResponse result) {
        if (StringUtils.isBlank(fullName) || fullName.length() < 60) {
            return true;
        }
        result.setFalseAndMessage("部门全称超出长度");
        return false;
    }


    /**
     * 单位名称是否合法
     *
     * @param name   部门编码
     * @param result 结果
     */
    private Boolean isNameLegal(String name, DataResponse result) {
        if (StringUtils.isBlank(name) || name.length() < 60) {
            return true;
        }
        result.setFalseAndMessage("部门名称超出长度");
        return false;
    }


    /**
     * 统一社会信用代码是否合法
     *
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @param result                  结果
     */
    private Boolean isUnifiedSocialCreditCode(String unifiedSocialCreditCode, DataResponse result) {
        if (unifiedSocialCreditCode != null && unifiedSocialCreditCode.length() > 30) {
            result.setFalseAndMessage("统一社会信用代码超出长度");
            return false;
        }
        return true;
    }


    /**
     * 省市区id是否合法
     */
    private Region isRegionIdLegal(Long regionId, DataResponse result) {
        Region region = regionManager.findOne(regionId);
        if (region == null) {
            result.setFalseAndMessage("区域不存在");
            return null;
        }
        if (region.getStatus() != RegionStatus.NORMAl.getCode()) {
            result.setFalseAndMessage("区域不存在");
            return null;
        }
        return region;
    }

    /**
     * 状态是否合法
     *
     * @param status 状态
     * @param result 结果
     */
    private Boolean isStatusLegal(Integer status, DataResponse result) {
        if (OrganizationStatus.isCodeLegal(status)) {
            return true;
        }
        result.setFalseAndMessage("状态不合法");
        return false;
    }


    /**
     * 排序是否合法
     *
     * @param sort   序号
     * @param result 结果
     */
    private Boolean isSortLegal(Integer sort, DataResponse result) {
        if (sort >= 1 && sort <= 4999999) {
            return true;
        }
        result.setFalseAndMessage("序号超出范围");
        return false;
    }


    /**
     * 备注说明是否合法
     *
     * @param memo   备注说明
     * @param result 结果
     */
    private Boolean isMemoLegal(String memo, DataResponse result) {
        if (memo != null && memo.length() > 500) {
            result.setFalseAndMessage("备注信息超出长度");
            return false;
        }
        return true;
    }


    /**
     * 上级部门ids是否合法
     *
     * @param parentId 上级部门id
     * @param result   结果
     */
    private Boolean isParentIdLegal(Long parentId, DataResponse result) {
        Organization organization = organizationManager.findOne(parentId);
        if (organization == null || organization.getStatus() != OrganizationStatus.ENABLE.getCode()) {
            result.setFalseAndMessage("上级部门选择有误，请重新选择");
            return false;
        }
        return true;
    }
}