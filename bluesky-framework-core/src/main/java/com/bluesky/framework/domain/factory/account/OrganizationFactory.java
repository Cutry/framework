package com.bluesky.framework.domain.factory.account;

import com.bluesky.framework.account.organization.Organization;

import java.util.List;


/**
 * 单位管理
 */
public interface OrganizationFactory {

    /**
     * 查询上级，上上级按顺序放入list，当前级别在最后
     *
     * @param currentOrganizationId 当前单位id
     */
    List<Organization> findParentList(long currentOrganizationId);

}