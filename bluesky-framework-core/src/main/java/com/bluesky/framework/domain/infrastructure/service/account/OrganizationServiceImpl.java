package com.bluesky.framework.domain.infrastructure.service.account;

import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.domain.model.account.OrganizationRepository;
import com.bluesky.framework.domain.service.account.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;


    @Override
    public long insert(Organization organization) {
        return organizationRepository.insert(organization);
    }


    @Override
    public void update(Organization organization) {
        organizationRepository.update(organization);
    }
}