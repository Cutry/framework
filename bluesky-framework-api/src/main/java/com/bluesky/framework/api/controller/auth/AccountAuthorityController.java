package com.bluesky.framework.api.controller.auth;

import com.bluesky.common.vo.DataResponse;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.account.account.AccountManager;
import com.bluesky.framework.account.auth.AccountAuthority;
import com.bluesky.framework.account.auth.AccountAuthorityManager;
import com.bluesky.framework.account.auth.RoleAuthority;
import com.bluesky.framework.account.auth.RoleAuthorityManager;
import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.account.organization.OrganizationManager;
import com.bluesky.framework.account.role.Role;
import com.bluesky.framework.account.role.RoleManager;
import com.bluesky.framework.setting.MenuSourceManager;
import com.bluesky.framework.setting.menu.MenuSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


/**
 * // todo linjiang 角色只有系统管理员才有权限
 * 角色权限配置管理
 */
@RestController
@RequestMapping("/account/auth")
public class AccountAuthorityController {
    @Autowired
    private AccountAuthorityManager accountAuthorityManager;
    @Autowired
    private RoleAuthorityManager roleAuthorityManager;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private MenuSourceManager menuSourceManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private OrganizationManager organizationManager;

    /**
     * 某个用户的权限信息
     */
    @GetMapping("/{accountId}/detail")
    public DataResponse findByAccountId(@PathVariable Long accountId) {
        DataResponse result = new DataResponse();
        //用户权限
        List<AccountAuthority> accountAuthorityList = accountAuthorityManager.findByAccountId(accountId);
        List<Long> operationIdList = accountAuthorityList.stream().filter(accountAuthority -> accountAuthority.getOperationId() != null).map(AccountAuthority::getOperationId).collect(Collectors.toList());
        //用户的操作权限
        List<MenuSource> accountMenuSourceList = menuSourceManager.findByOperationIds(operationIdList);
        result.addData("accountMenuSourceList", accountMenuSourceList);

        return result;
    }

    /**
     * 某个用户的权限信息编辑页面
     */
    @GetMapping("/{accountId}/edit")
    public DataResponse findByAccountIdForEdit(@PathVariable Long accountId) {
        DataResponse result = new DataResponse();
        //用户权限
        List<AccountAuthority> accountAuthorityList = accountAuthorityManager.findByAccountId(accountId);
        List<Long> operationIdList = accountAuthorityList.stream().filter(accountAuthority -> accountAuthority.getOperationId() != null).map(AccountAuthority::getOperationId).collect(Collectors.toList());
        //用户的操作权限
        // accountMenuSourceList = menuSourceManager.findByOperationIds(operationIdList)

        //查找当前用户所在角色，获取角色的权限，用户的权限<=角色的角色
        Account account = accountManager.findOne(accountId);
        if (account == null) {
            return result.setFalseAndMessage("当前用户不存在");
        }
        List<RoleAuthority> roleAuthorityList = roleAuthorityManager.findByRoleId(account.getRoleId());
        //所属角色的菜单及操作权限
        List<Long> roleOperationIdList = roleAuthorityList.stream().filter(roleAuthority -> roleAuthority.getOperationId() != null).map(RoleAuthority::getOperationId).collect(Collectors.toList());
        //用户的操作权限
        List<MenuSource> roleMenuSourceList = menuSourceManager.findByOperationIds(roleOperationIdList);

        //获取角色及单位信息
        Role role = roleManager.findOne(account.getRoleId());
        if (role == null) {
            return result.setFalseAndMessage("当前用户角色不存在");
        }
        String organizationName = "";
        if (account.getOrganizationId() != null) {
            Organization organization = organizationManager.findOne(account.getOrganizationId());
            organizationName = organization.getName();
        }
        account.setRoleName(role.getName());
        account.setOrganizationName(organizationName);
        account.setPassword(null);
        result.addData("account", account);
        result.addData("operationIdList", operationIdList);
        result.addData("menuSourceList", roleMenuSourceList);
        return result;
    }

    /**
     * 保存用户权限信息
     */
    @PreAuthorize("hasPermission('crm-api', 'om_user_auth_manage')")
    @PostMapping("{accountId}/save")
    public DataResponse updateMenu(@PathVariable Long accountId,
                                   @RequestBody List<AccountAuthority> accountAuthorityList) {
        DataResponse result = new DataResponse();
        accountAuthorityManager.saveAuthorities(accountId, accountAuthorityList);
        return result;
    }
}