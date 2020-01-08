package com.bluesky.framework.domain.model.account;

import com.bluesky.framework.account.auth.RoleAuthority;

import java.util.List;

public interface RoleAuthorityRepository {

    /**
     * 保存权限信息
     */
    void saveAuthorities(List<RoleAuthority> roleAuthorityList);

    /**
     * 根据角色查询权限
     */
    List<RoleAuthority> findByRoleId(long roleId);

    /**
     * 根据角色ids查询
     */
    List<RoleAuthority> findByRoleIds(List<Long> roleIds);

    /**
     * 根据角色删除
     *
     * @param roleId 角色ID
     */
    void deleteByRoleId(long roleId);

}