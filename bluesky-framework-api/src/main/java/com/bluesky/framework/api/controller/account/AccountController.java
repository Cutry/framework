package com.bluesky.framework.api.controller.account;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.account.AccountManager;
import com.bluesky.framework.account.constant.AccountStatus;
import com.bluesky.framework.account.constant.OperateLogStep;
import com.bluesky.framework.account.constant.OrganizationStatus;
import com.bluesky.framework.account.constant.RoleType;
import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.account.organization.OrganizationManager;
import com.bluesky.framework.account.region.Region;
import com.bluesky.framework.account.region.RegionManager;
import com.bluesky.framework.account.role.Role;
import com.bluesky.framework.account.role.RoleManager;
import com.bluesky.framework.api.controller.common.IPUtils;
import com.bluesky.framework.api.event.LogEventProcessor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 账户
 */
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private OrganizationManager organizationManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private LogEventProcessor logEventProcessor;


    @PostMapping("/add")
    @PreAuthorize("hasPermission('crm-api','om_user_add')")
    public DataResponse add(@AuthenticationPrincipal Account account, HttpServletRequest request,
                            @RequestParam String name, @RequestParam String password,
                            @RequestParam String passwordConfirm, @RequestParam String mobile,
                            @RequestParam Long roleId, @RequestParam Long organizationId,
                            @RequestParam(required = false) String department, @RequestParam String idCard,
                            @RequestParam(required = false) Integer sort, @RequestParam(required = false, defaultValue = "0") Integer status) {
        DataResponse result = new DataResponse();

        if (!isNameLegal(name, result)) {
            return result;
        }
        if (!isMobileLegal(mobile, null, result)) {
            return result;
        }
        if (!isPasswordLegal(password, passwordConfirm, result)) {
            return result;
        }
        Organization organization = isOrganizationIdExistAndHasAuth(organizationId, account, result);
        if (organization == null) {
            return result;
        }
        Role role = isRoleIdLegal(roleId, result, account);
        if (role == null) {
            return result;
        }
        if (!isRoleLevelEqualsOrganizationLevel(organization, role, result)) {
            return result;
        }
        if (!isDepartmentLegal(department, result)) {
            return result;
        }
        if (!isIdCartLegal(idCard, result)) {
            return result;
        }
        if (!isStatusLegal(status, result)) {
            return result;
        }

        String authoritiesText = "";
        if (role.getType() == RoleType.PLATFORM.getCode()) {
            authoritiesText = "ROLE_PLATFORM,ROLE_ADMIN";
        } else if (role.getType() == RoleType.GOVERNMENT.getCode()) {
            authoritiesText = "ROLE_PLATFORM,ROLE_GOVERNMENT";
        }
        Account newAccount = Account.builder()
                .name(name)
                .headImg(null)
                .mobile(mobile)
                .roleId(roleId)
                .roleType(role.getType())
                .organizationId(organizationId)
                .department(department)
                .idCard(idCard)
                .sort(sort)
                .authoritiesText(authoritiesText)
                .status(status)
                .password(password).build();

        Long id = accountManager.insert(newAccount);
        result.addData("id", id);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, id, OperateLogStep.USER_ADD.getCode(), ip);
        return result;
    }


    @PostMapping("/{id}/update")
    @PreAuthorize("hasPermission('crm-api','om_user_update')")
    public DataResponse update(@AuthenticationPrincipal Account account, HttpServletRequest request, @PathVariable Long id,
                               @RequestParam String name, @RequestParam String mobile, @RequestParam(required = false) String department,
                               @RequestParam String idCard, @RequestParam(required = false) Integer sort) {
        DataResponse result = new DataResponse();

        if (null == isIdLegal(id, account, result)) {
            return result;
        }
        if (!isNameLegal(name, result)) {
            return result;
        }
        if (!isMobileLegal(mobile, id, result)) {
            return result;
        }
        if (!isDepartmentLegal(department, result)) {
            return result;
        }
        if (!isIdCartLegal(idCard, result)) {
            return result;
        }

        Account newAccount = Account.builder()
                .name(name)
                .headImg(null)
                .mobile(mobile)
                .department(department)
                .idCard(idCard)
                .id(id).build();

        accountManager.update(newAccount);
        Account updatedAccount = accountManager.findOne(id);
        result.addData("updatedAccount", updatedAccount);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, id, OperateLogStep.USER_UPDATE.getCode(), ip);

        return result;
    }


    /**
     * 批量启用用户
     */
    @PostMapping("/enable")
    @PreAuthorize("hasPermission('crm-api','om_user_open')")
    public DataResponse enable(@AuthenticationPrincipal Account account, HttpServletRequest request, @RequestParam("idsStr") String idsStr) {
        DataResponse result = new DataResponse();

        if (StringUtils.isBlank(idsStr)) {
            return result.setFalseAndMessage("请选择用户!");
        }
        List<Long> ids = Arrays.stream(idsStr.split(",")).map(Long::valueOf).collect(Collectors.toList());

        for (Long id : ids) {
            Account operateAccount = isIdLegal(id, account, result);
            if (operateAccount == null) {
                return result;
            }
            if (!operateAccount.getStatus().equals(AccountStatus.DISABLE.getCode())) {
                return result.setFalseAndMessage("用户状态异常,请刷新页面重试!");
            }
        }

        accountManager.enable(ids);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, null, OperateLogStep.USER_ENABLE.getCode(), ip);

        return result;
    }


    /**
     * 批量禁用用户
     */
    @PostMapping("/disable")
    @PreAuthorize("hasPermission('crm-api','om_user_open')")
    public DataResponse disable(@AuthenticationPrincipal Account account, HttpServletRequest
            request, @RequestParam("idsStr") String idsStr) {
        DataResponse result = new DataResponse();

        if (StringUtils.isBlank(idsStr)) {
            return result.setFalseAndMessage("请选择用户!");
        }
        List<Long> ids = Arrays.stream(idsStr.split(",")).map(Long::valueOf).collect(Collectors.toList());

        for (Long id : ids) {
            Account operateAccount = isIdLegal(id, account, result);
            if (operateAccount == null) {
                return result;
            }
            if (!operateAccount.getStatus().equals(AccountStatus.NORMAl.getCode())) {
                return result.setFalseAndMessage("用户状态异常,请刷新页面重试!");
            }
        }
        accountManager.disable(ids);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, null, OperateLogStep.USER_DISABLE.getCode(), ip);

        return result;
    }


    /**
     * 批量删除用户
     */
    @PostMapping("/delete")
    @PreAuthorize("hasPermission('crm-api','om_user_delete')")
    public DataResponse delete(@AuthenticationPrincipal Account account, HttpServletRequest
            request, @RequestParam("idsStr") String idsStr) {
        DataResponse result = new DataResponse();

        if (StringUtils.isBlank(idsStr)) {
            return result.setFalseAndMessage("请选择用户!");
        }

        List<Long> ids = Arrays.stream(idsStr.split(",")).map(Long::valueOf).collect(Collectors.toList());
        for (Long id : ids) {
            Account operateAccount = isIdLegal(id, account, result);
            if (operateAccount == null) {
                return result;
            }
            if (operateAccount.getStatus().equals(AccountStatus.DELETE.getCode())) {
                return result.setFalseAndMessage("用户状态异常,请刷新页面重试!");
            }
        }
        accountManager.delete(ids);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, null, OperateLogStep.USER_DISABLE.getCode(), ip);

        return result;
    }


    /**
     * 密码重置
     *
     * @param id              主键
     * @param password        密码
     * @param passwordConfirm 密码确认
     */
    @PostMapping("/{id}/reset_password")
    public DataResponse resetPassword(@AuthenticationPrincipal Account account, HttpServletRequest request,
                                      @PathVariable Long id, @RequestParam String password, @RequestParam String passwordConfirm) {
        DataResponse result = new DataResponse();
        // 校验id是否存在且有权限操作
        if (isIdLegal(id, account, result) == null) {
            return result;
        }
        // 校验密码是否符合规则
        if (!isPasswordLegal(password, passwordConfirm, result)) {
            return result;
        }

        accountManager.updatePassword(id, password);

        // 记录日志
        String ip = IPUtils.getIpAddr(request);
        logEventProcessor.logOperateRegion(account, id, OperateLogStep.USER_RESET_PASSWORD.getCode(), ip);

        return result;
    }


    /**
     * 条件查询
     *
     * @param name           姓名
     * @param mobile         手机号
     * @param organizationId 单位id
     * @param roleId         角色id
     * @param status         状态
     */
    @GetMapping("/list")
    @PreAuthorize("hasPermission('crm-api','om_user_view')")
    public DataResponse findByCondition(@AuthenticationPrincipal Account
                                                account, @RequestParam(required = false) String name, @RequestParam(required = false) String mobile,
                                        @RequestParam(required = false) Long organizationId, @RequestParam(required = false) Long roleId,
                                        @RequestParam(required = false) Integer
                                                status, @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                        @RequestParam(required = false, defaultValue = "15") Integer pageSize) {
        DataResponse result = new DataResponse();
        Long queryOrganizationId = null;
        if (organizationId != null) {
            // 校验organizationId，管理员可以查看所有，非管理员可以查看当前级别，或者child单位的数据。
            if (isOrganizationIdExistAndHasAuth(organizationId, account, result) == null) {
                return result;
            }
            queryOrganizationId = organizationId;
        } else {
            // 如果不是管理员，且单位id没有传的时候，则查询当前用户单位的其他用户。不能查询其他单位的用户
            if (account.getRoleType() == RoleType.GOVERNMENT.getCode()) {
                queryOrganizationId = organizationManager.findOne(account.getOrganizationId()).getId();
            }
        }

        // 校验角色的level。只显示当前level和下级level
        if (roleId != null && isRoleIdLegal(roleId, result, account) == null) {
            return result;
        }

        if (status != null && !isStatusLegal(status, result)) {
            return result;
        }
        Page<Account> page = accountManager.findPageByCondition(StringUtils.isNotBlank(name) ? name.trim() : name, StringUtils.isNotBlank(mobile) ? mobile.trim() : mobile, queryOrganizationId, roleId, status, pageNum, pageSize);

        // 组装单位角色信息
        this.packageRichAccount(page.getList());
        result.addData("page", page);
        return result;
    }


    /**
     * 打包account，补充部门，角色等信息
     *
     * @param accounts accounts
     */
    private void packageRichAccount(List<Account> accounts) {
        if (accounts.isEmpty()) {
            return;
        }
        // 单位
        List<Long> organizationIds = accounts.stream().filter(account -> account.getOrganizationId() != null).map(Account::getOrganizationId).collect(Collectors.toList());
        Map<Long, String> organizationIdAndNameMap = organizationManager.findByIds(organizationIds).stream().collect(Collectors.toMap(Organization::getId, Organization::getName));
        // 角色
        List<Long> roleIds = accounts.stream().filter(account -> account.getRoleId() != null).map(Account::getRoleId).collect(Collectors.toList());
        Map<Long, String> roleIdAndNameMap = roleManager.findByIds(roleIds).stream().collect(Collectors.toMap(Role::getId, Role::getName));
        accounts.forEach(account -> {
            account.setRoleName(account.getRoleId() != null ? roleIdAndNameMap.get(account.getRoleId()) : null);
            account.setOrganizationName(account.getOrganizationId() != null ? organizationIdAndNameMap.get(account.getOrganizationId()) : null);
        });
    }


    /**
     * 检查id是否合法，且有权限操作
     *
     * @param id      账号
     * @param account 当前登录账户
     * @param result  返回结果
     */
    private Account isIdLegal(Long id, Account account, DataResponse result) {
        Account operateAccount = accountManager.findOne(id);
        if (operateAccount == null) {
            result.setFalseAndMessage("用户不存在");
            return null;
        }

        // 管理员可以操作管理员
        if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
            return operateAccount;
        }

        // 非管理员，只能操作当前部门，和下级部门
        Organization operateOrganization = organizationManager.findOne(operateAccount.getOrganizationId());
        if (this.isCurrentOrganizationParentThan(operateOrganization.getId(), account, result)) {
            return operateAccount;
        }
        return null;
    }

    /**
     * 姓名是否合法
     *
     * @param name   姓名
     * @param result 结果
     */
    private Boolean isNameLegal(String name, DataResponse result) {
        if (StringUtils.isBlank(name) || name.length() < 30) {
            return true;
        }
        result.setFalseAndMessage("姓名超出长度");
        return false;
    }


    /**
     * 密码是否符合规则
     *
     * @param password        密码
     * @param passwordConfirm 确认密码
     * @param result          返回结果
     */
    private Boolean isPasswordLegal(String password, String passwordConfirm, DataResponse result) {
        if (StringUtils.isNotBlank(password) && password.equals(passwordConfirm) && password.matches("^[0-9a-zA-Z]{6,12}$")) {
            return true;
        }
        result.setFalseAndMessage("密码格式错误");
        return false;
    }


    /**
     * 添加用户时，校验选择的角色等级是否和所在单位的等级相等
     *
     * @param organization 所选择的单位
     * @param role         所选择的角色
     * @param result       返回结果
     */
    private Boolean isRoleLevelEqualsOrganizationLevel(Organization organization, Role role, DataResponse result) {
        Region region = regionManager.findOne(organization.getRegionId());
        if (region.getLevel().equals(role.getLevel())) {
            return true;
        }
        result.setFalseAndMessage("数据不一致，请重新选择角色");
        return false;
    }


    /**
     * 手机号是否合法
     *
     * @param mobile    手机号
     * @param accountId 账号id，新增则为null，修改传修改账号id
     * @param result    结果
     */
    private Boolean isMobileLegal(String mobile, Long accountId, DataResponse result) {
        if (StringUtils.isBlank(mobile) || !mobile.matches("^1\\d{10}$")) {
            result.setFalseAndMessage("手机号格式错误");
            return false;
        }

        List<Account> accounts = accountManager.findByMobile(mobile, RoleType.GOVERNMENT.getCode(), RoleType.PLATFORM.getCode());
        if (accounts.isEmpty()) {
            return true;
        }

        // 更新用户时
        if (accountId != null) {
            // 判断是否手机号重复
            Optional exist = accounts.stream().filter(account -> !account.getId().equals(accountId)).findFirst();
            if (exist.isPresent()) {
                result.setFalseAndMessage("手机号已被占用");
                return false;
            } else {
                return true;
            }
        }
        // 新增用户时
        result.setFalseAndMessage("手机号已被占用");
        return false;
    }


    /**
     * 角色id是否合法
     *
     * @param roleId 角色id
     * @param result 结果
     */
    private Role isRoleIdLegal(Long roleId, DataResponse result, Account account) {
        Role role = roleManager.findOne(roleId);
        if (role == null) {
            result.setFalseAndMessage("角色选择错误");
            return null;
        }

        List<Integer> typeList = Stream.of(new Integer[]{RoleType.PLATFORM.getCode(), RoleType.GOVERNMENT.getCode()}).collect(Collectors.toList());
        if (!typeList.contains(role.getType())) {
            result.setFalseAndMessage("角色选择错误");
            return null;
        }

        if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
            return role;
        }
        //操作人的权限超出了赋予角色的权限则不能操作
        Role accountRole = roleManager.findOne(account.getRoleId());
        if (accountRole.getLevel() > role.getLevel()) {
            result.setFalseAndMessage("您没有权限进行此操作");
            return null;
        }
        return role;
    }


    /**
     * 单位id是否合法
     *
     * @param organizationId 单位id
     * @param account        当前登录账户
     * @param result         返回结果
     */
    private Organization isOrganizationIdExistAndHasAuth(Long organizationId, Account account, DataResponse result) {
        Organization organization = organizationManager.findOne(organizationId);
        if (organization == null) {
            result.setFalseAndMessage("单位选择错误");
            return null;
        }

        if (organization.getStatus() == OrganizationStatus.DELETE.getCode()) {
            result.setFalseAndMessage("单位不存在");
            return null;
        }
        // 检查部门权限,上级单位能看下级单位
        if (this.isCurrentOrganizationParentThan(organizationId, account, result)) {
            return organization;
        } else {
            return null;
        }
    }


    /**
     * 检查当前账户的所在单位是否是给定部门的上级单位，上上级单位
     *
     * @param organizationId 给定单位id
     * @param account        当前账号
     */
    private Boolean isCurrentOrganizationParentThan(Long organizationId, Account account, DataResponse result) {
        // 系统管理员有全部权限
        if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
            return true;
        }

        // 当前用户所在单位
        Organization currentOrganization = organizationManager.findOne(account.getOrganizationId());
        // 待操作用户的单位
        List<Long> parentIdsList = organizationManager.findParentList(organizationId).stream().map(Organization::getId).collect(Collectors.toList());
        // 检查当前用户的单位是否是待查询单位的上级或者上上级单位，是上级才有权限
        if (!parentIdsList.contains(currentOrganization.getId())) {
            result.setFalseAndMessage("您没有权限进行此操作");
            return false;
        }
        return true;
    }


    /**
     * 部门是否合法
     *
     * @param department 部门
     * @param result     结果
     */
    private Boolean isDepartmentLegal(String department, DataResponse result) {
        if (department != null && department.length() > 60) {
            result.setFalseAndMessage("部门名称超出长度");
            return false;
        }
        return true;
    }


    /**
     * 身份证号是否合法
     *
     * @param idCard 身份证号码
     * @param result 返回结果
     */
    private Boolean isIdCartLegal(String idCard, DataResponse result) {
        String reg = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
        if (StringUtils.isNotBlank(idCard) && idCard.matches(reg)) {
            return true;
        } else {
            result.setFalseAndMessage("身份证号码填写有误");
            return false;
        }
    }


    /**
     * 选择的状态是否合法
     *
     * @param status 状态
     * @param result 结果
     */
    private Boolean isStatusLegal(Integer status, DataResponse result) {
        List<Integer> statusList = Stream.of(new Integer[]{AccountStatus.NORMAl.getCode(), AccountStatus.DISABLE.getCode()}).collect(Collectors.toList());
        if (!statusList.contains(status)) {
            result.setFalseAndMessage("状态选择有误");
            return false;
        }
        return true;
    }
}