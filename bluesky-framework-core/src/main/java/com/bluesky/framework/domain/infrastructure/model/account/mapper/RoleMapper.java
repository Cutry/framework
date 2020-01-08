package com.bluesky.framework.domain.infrastructure.model.account.mapper;

import com.bluesky.framework.account.role.Role;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface RoleMapper {
    @Insert("insert into role " +
            "(type, level, name, status,created_at, updated_at) " +
            "values " +
            "(#{role.type},#{role.level},#{role.name},0,now(),now())")
    @SelectKey(before = false, keyProperty = "role.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void insert(@Param("role") Role role);


    @ResultMap("RoleResultMap")
    @Select("select * from role where id = #{id} and status != -5")
    Role findOne(@Param("id") long id);


    /**
     * 根据主键ids查找
     *
     * @param ids 主键
     * @return 返回集合
     */
    List<Role> findByIds(@Param("ids") List<Long> ids);


    /**
     * 查找下级所有的角色
     *
     * @param level 行政级别，为0则查找所有政务单位角色（排除平台角色，社会认证角色）
     */
    List<Role> findGovernmentLowerLevelRoles(@Param("level") Integer level);


    /**
     * 查找当前level所有的角色
     *
     * @param level 行政级别，为0则查找所有政务单位角色（排除平台角色，社会认证角色）
     */
    List<Role> findGovernmentRolesByLevel(@Param("level") Integer level);


    /**
     * 查找所有角色，包括政务单位角色，社会认证角色，平台角色
     *
     * @return 返回集合
     */
    @ResultMap("RoleResultMap")
    @Select("select * from role where status != -5 order by level,type")
    List<Role> findAll();


    void update(@Param("role") Role role);

}