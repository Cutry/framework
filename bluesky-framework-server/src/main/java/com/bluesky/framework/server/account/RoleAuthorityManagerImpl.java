package com.bluesky.framework.server.account;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.framework.account.auth.RoleAuthority;
import com.bluesky.framework.account.auth.RoleAuthorityManager;
import com.bluesky.framework.domain.model.account.RoleAuthorityRepository;
import com.bluesky.framework.domain.service.account.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@DubboService
public class RoleAuthorityManagerImpl implements RoleAuthorityManager {
    @Autowired
    private RoleAuthorityRepository roleAuthorityRepository;
    @Autowired
    private RoleAuthorityService roleAuthorityService;

    @Override
    public void saveAuthorities(Long roleId, List<RoleAuthority> roleAuthorityList) {
        roleAuthorityService.saveAuthorities(roleId, roleAuthorityList);
    }

    @Override
    public List<RoleAuthority> findByRoleId(Long roleId) {
        return roleAuthorityRepository.findByRoleId(roleId);
    }

    @Override
    public List<RoleAuthority> findByRoleIds(List<Long> roleIds) {
        return roleAuthorityRepository.findByRoleIds(roleIds);
    }
}