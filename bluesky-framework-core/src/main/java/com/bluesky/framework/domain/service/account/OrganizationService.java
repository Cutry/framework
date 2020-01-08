package com.bluesky.framework.domain.service.account;

import com.bluesky.framework.account.organization.Organization;

/**
 * 组织单位Service
 */
public interface OrganizationService {

    long insert(Organization organization);


    void update(Organization organization);
}