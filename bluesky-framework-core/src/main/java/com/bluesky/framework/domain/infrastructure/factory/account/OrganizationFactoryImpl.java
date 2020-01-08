package com.bluesky.framework.domain.infrastructure.factory.account;

import com.bluesky.framework.account.organization.Organization;
import com.bluesky.framework.domain.factory.account.OrganizationFactory;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.OrganizationMapper;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class OrganizationFactoryImpl implements OrganizationFactory {
    @Autowired
    private OrganizationMapper organizationMapper;


    @Override
    public List<Organization> findParentList(long currentOrganizationId) {
        List<Organization> list = new ArrayList<>();
        this.recursiveFindParent(currentOrganizationId, list);
        return list.stream().distinct().collect(Collectors.toList());
    }


    /**
     * 递归查询上级
     *
     * @param id   当前id
     * @param list 返回集合
     */
    private void recursiveFindParent(long id, List<Organization> list) {
        val organization = organizationMapper.findOne(id);
        if (organization == null) {
            return;
        }
        list.add(organization);
        if (organization.getParentId() == null) return;
        this.recursiveFindParent(organization.getParentId(), list);
    }
}