package com.bluesky.framework.domain.infrastructure.model.account;

import com.bluesky.framework.account.auth.RoleAuthority;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.RoleAuthorityMapper;
import com.bluesky.framework.domain.model.account.RoleAuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class RoleAuthorityRepositoryImpl implements RoleAuthorityRepository {
    @Autowired
    private RoleAuthorityMapper roleAuthorityMapper;


    @Override
    public void saveAuthorities(List<RoleAuthority> roleAuthorityList) {
        roleAuthorityMapper.saveAuthorities(roleAuthorityList);
    }

    @Override
    public List<RoleAuthority> findByRoleId(long roleId) {
        return roleAuthorityMapper.findByRoleId(roleId);
    }

    @Override
    public List<RoleAuthority> findByRoleIds(List<Long> roleIds) {
        return roleAuthorityMapper.findByRoleIds(roleIds);
    }

    @Override
    public void deleteByRoleId(long roleId) {
        roleAuthorityMapper.deleteByRoleId(roleId);
    }
}