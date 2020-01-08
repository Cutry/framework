package com.bluesky.framework.domain.service.account;

import com.bluesky.framework.account.role.Role;

public interface RoleService {

    long insert(Role role);


    void update(Role role);


    /**
     * 逻辑删除
     *
     * @param id 主键
     */
    void delete(long id);
}