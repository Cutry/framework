package com.bluesky.framework.account.auth;

import java.util.List;

public interface RoleAuthorityManager {

    /**
     * 保存角色权限
     */
    void saveAuthorities(Long roleId, List<RoleAuthority> roleAuthorityList);

    /**
     * 根据角色查询
     */
    List<RoleAuthority> findByRoleId(Long roleId);

    /**
     * 根据角色查询
     */
    List<RoleAuthority> findByRoleIds(List<Long> roleIds);

}