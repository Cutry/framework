package com.bluesky.framework.domain.infrastructure.model.account.mapper;

import com.bluesky.framework.account.auth.RoleAuthority;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RoleAuthorityMapper {

    /**
     * 保存权限信息
     */
    void saveAuthorities(@Param("roleAuthorityList") List<RoleAuthority> roleAuthorityList);

    /**
     * 根据角色查询权限
     */
    @ResultMap("RoleAuthorityResultMap")
    @Select("select * from role_authority where role_id = #{roleId}")
    List<RoleAuthority> findByRoleId(@Param("roleId") long roleId);

    /**
     * 根据角色ids查询权限
     */
    List<RoleAuthority> findByRoleIds(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据角色删除
     *
     * @param roleId 角色ID
     */
    @Delete("delete from role_authority where role_id = #{roleId}")
    void deleteByRoleId(@Param("roleId") long roleId);

}