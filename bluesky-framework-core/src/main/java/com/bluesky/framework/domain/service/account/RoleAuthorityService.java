package com.bluesky.framework.domain.service.account;

import com.bluesky.framework.account.auth.RoleAuthority;

import java.util.List;

public interface RoleAuthorityService {

    /**
     * 保存角色权限
     */
    void saveAuthorities(long roleId, List<RoleAuthority> roleAuthorityList);
}