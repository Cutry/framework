package com.bluesky.framework.account.boot.security.web;

import com.bluesky.framework.account.auth.AccountAuthority;
import com.bluesky.framework.account.auth.AccountAuthorityManager;
import com.bluesky.framework.account.boot.security.web.userdetails.CurrentUserDetails;
import com.bluesky.framework.account.constant.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.List;

/**
 * user permission service implementation
 *
 * @author linjiang
 */
public class PermissionEvaluatorImpl implements PermissionEvaluator {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
        if (authentication == null) {
            return false;
        }
        CurrentUserDetails account = (CurrentUserDetails) authentication.getPrincipal();
        //管理员
        if (account.getRoleType() == RoleType.PLATFORM.getCode()) {
            return true;
        }

        //获取当前登录用户的权限列表
        AccountAuthorityManager accountAuthorityManager = (AccountAuthorityManager) applicationContext.getBean("accountAuthorityManager");
        //权限列表
        List<AccountAuthority> sourceList = accountAuthorityManager.findByAccountId(account.getId());
        for (AccountAuthority source : sourceList) {
            //不区分大小写
            if (source.getOperation() != null && permission != null &&
                    source.getOperation().toLowerCase().equals(permission.toString().toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

}


