package com.bluesky.framework.server.account;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.account.organization.OrganizationManager;
import com.bluesky.framework.domain.factory.account.OrganizationFactory;
import com.bluesky.framework.domain.model.account.OrganizationRepository;
import com.bluesky.framework.domain.service.account.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@DubboService
public class OrganizationManagerImpl implements OrganizationManager {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private OrganizationFactory organizationFactory;

    @Override
    public Long insert(Organization organization) {
        return organizationService.insert(organization);
    }

    @Override
    public Organization findOne(Long id) {
        return organizationRepository.findOne(id);
    }

    @Override
    public void update(Organization organization) {
        organizationRepository.update(organization);
    }

    @Override
    public List<Organization> findByIds(List<Long> ids) {
        return organizationRepository.findByIds(ids);
    }

    @Override
    public List<Organization> findParentList(Long currentOrganizationId) {
        return organizationFactory.findParentList(currentOrganizationId);
    }

    @Override
    public List<Organization> findChildren(Long id) {
        return organizationRepository.findChildren(id);
    }

    @Override
    public Page<Organization> findPageByCondition(Long regionId, String name, Integer pageNum, Integer pageSize) {
        return organizationRepository.findPageByCondition(regionId, name, pageNum, pageSize);
    }

    @Override
    public List<Organization> findListByCondition(Long regionId, String name) {
        return organizationRepository.findListByCondition(regionId, name);
    }
}