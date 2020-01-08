package com.bluesky.framework.domain.infrastructure.model.account;

import com.bluesky.framework.account.role.Role;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.RoleMapper;
import com.bluesky.framework.domain.model.account.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.Collections.emptyList;


@Repository
public class RoleRepositoryImpl implements RoleRepository {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public long insert(Role role) {
        roleMapper.insert(role);
        return role.getId();
    }


    @Override
    public Role findOne(long id) {
        return roleMapper.findOne(id);
    }


    @Override
    public List<Role> findByIds(List<Long> ids) {
        if (ids.isEmpty()) return emptyList();
        return roleMapper.findByIds(ids);
    }


    @Override
    public List<Role> findGovernmentLowerLevelRoles(Integer level) {
        return roleMapper.findGovernmentLowerLevelRoles(level);
    }


    @Override
    public List<Role> findGovernmentRolesByLevel(Integer level) {
        return roleMapper.findGovernmentRolesByLevel(level);
    }


    @Override
    public List<Role> findAll() {
        return roleMapper.findAll();
    }


    @Override
    public void update(Role role) {
        roleMapper.update(role);
    }

}