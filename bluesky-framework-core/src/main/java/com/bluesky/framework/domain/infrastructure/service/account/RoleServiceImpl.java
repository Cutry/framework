package com.bluesky.framework.domain.infrastructure.service.account;

import com.bluesky.framework.account.constant.RoleStatus;
import com.bluesky.framework.account.role.Role;
import com.bluesky.framework.domain.model.account.RoleRepository;
import com.bluesky.framework.domain.service.account.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;


    @Override
    public long insert(Role role) {
        return roleRepository.insert(role);
    }


    @Override
    public void update(Role role) {
        Role newRole = Role.builder().id(role.getId()).name(role.getName()).build();
        roleRepository.update(newRole);
    }


    @Override
    public void delete(long id) {
        Role newRole = Role.builder().id(id).status(RoleStatus.DELETE.getCode()).build();
        roleRepository.update(newRole);
    }
}