package com.bluesky.framework.server.account;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.framework.account.role.Role;
import com.bluesky.framework.account.role.RoleManager;
import com.bluesky.framework.domain.model.account.RoleRepository;
import com.bluesky.framework.domain.service.account.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@DubboService
public class RoleManagerImpl implements RoleManager {
    @Autowired
    private RoleService roleService;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Long insert(Role role) {
        return roleService.insert(role);
    }

    @Override
    public Role findOne(Long id) {
        return roleRepository.findOne(id);
    }

    @Override
    public List<Role> findByIds(List<Long> ids) {
        return roleRepository.findByIds(ids);
    }

    @Override
    public List<Role> findGovernmentLowerLevelRoles(Integer level) {
        return roleRepository.findGovernmentLowerLevelRoles(level);
    }

    @Override
    public List<Role> findGovernmentRolesByLevel(Integer level) {
        return roleRepository.findGovernmentRolesByLevel(level);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public void update(Role role) {
        roleService.update(role);
    }

    @Override
    public void delete(Long id) {
        roleService.delete(id);
    }
}