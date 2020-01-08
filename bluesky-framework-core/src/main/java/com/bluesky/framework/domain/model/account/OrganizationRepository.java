package com.bluesky.framework.domain.model.account;

import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.organization.Organization;

import java.util.List;


/**
 * 组织单位管理Repository
 */
public interface OrganizationRepository {

    long insert(Organization organization);


    Organization findOne(long id);


    void update(Organization organization);


    /**
     * 根据主键ids查找
     */
    List<Organization> findByIds(List<Long> ids);


    /**
     * 根据上级查找下级
     *
     * @param id 主键
     * @return 返回结果
     */
    List<Organization> findChildren(long id);


    /**
     * 根据条件查询单位（分页）
     *
     * @param regionId 省市区id
     * @param name     单位名称
     * @param pageNum  页码
     * @param pageSize 页码大小
     * @return 返回page
     */
    Page<Organization> findPageByCondition(long regionId, String name, Integer pageNum, Integer pageSize);


    /**
     * 根据条件查询单位（不分页）
     *
     * @param regionId 省市区id
     * @param name     单位名称
     */
    List<Organization> findListByCondition(long regionId, String name);

}