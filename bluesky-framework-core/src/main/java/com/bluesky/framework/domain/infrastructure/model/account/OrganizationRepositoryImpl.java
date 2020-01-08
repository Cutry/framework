package com.bluesky.framework.domain.infrastructure.model.account;

import com.bluesky.common.vo.Page;
import com.bluesky.core.common.PageBeanUtils;
import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.OrganizationMapper;
import com.bluesky.framework.domain.model.account.OrganizationRepository;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

import static java.util.Collections.emptyList;


@Repository
public class OrganizationRepositoryImpl implements OrganizationRepository {
    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public long insert(Organization organization) {
        organizationMapper.insert(organization);
        return organization.getId();
    }


    @Override
    public Organization findOne(long id) {
        return organizationMapper.findOne(id);
    }


    @Override
    public void update(Organization organization) {
        organizationMapper.update(organization);
    }


    @Override
    public List<Organization> findByIds(List<Long> ids) {
        if (ids.isEmpty()) return emptyList();
        return organizationMapper.findByIds(ids);
    }


    @Override
    public List<Organization> findChildren(long id) {
        return organizationMapper.findChildren(id);
    }


    @Override
    public Page<Organization> findPageByCondition(long regionId, String name, Integer pageNum, Integer pageSize) {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 10;
        PageHelper.startPage(pageNum, pageSize);
        List<Organization> list = organizationMapper.findByCondition(regionId, name);
        return PageBeanUtils.copyPageProperties(list);
    }


    @Override
    public List<Organization> findListByCondition(long regionId, String name) {
        return organizationMapper.findByCondition(regionId, name);
    }
}