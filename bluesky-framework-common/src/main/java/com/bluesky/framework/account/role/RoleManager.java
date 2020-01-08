package com.bluesky.framework.account.role;

import java.util.List;

public interface RoleManager {

    Long insert(Role role);


    Role findOne(Long id);


    /**
     * 根据主键ids查找
     *
     * @param ids 主键
     * @return 返回集合
     */
    List<Role> findByIds(List<Long> ids);


    /**
     * 查找下级所有的角色
     *
     * @param level 行政级别，为0则查找所有政务单位角色（排除平台角色，社会认证角色）
     */
    List<Role> findGovernmentLowerLevelRoles(Integer level);


    /**
     * 查找当前level所有的角色
     *
     * @param level 行政级别，为0则查找所有政务单位角色（排除平台角色，社会认证角色）
     */
    List<Role> findGovernmentRolesByLevel(Integer level);


    /**
     * 查找所有角色，包括政务单位角色，社会认证角色，平台角色
     *
     * @return 返回集合
     */
    List<Role> findAll();


    void update(Role role);


    /**
     * 逻辑删除
     *
     * @param id 主键
     */
    void delete(Long id);
}