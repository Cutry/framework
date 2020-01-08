package com.bluesky.framework.account.organization;

import com.bluesky.common.vo.Page;

import java.util.List;

public interface OrganizationManager {

    Long insert(Organization organization);


    Organization findOne(Long id);


    void update(Organization organization);


    /**
     * 根据主键ids查找
     */
    List<Organization> findByIds(List<Long> ids);


    /**
     * 查询上级，上上级按顺序放入list，当前级别在最后
     *
     * @param currentOrganizationId 当前单位id
     */
    List<Organization> findParentList(Long currentOrganizationId);


    /**
     * 根据上级查找下级
     *
     * @param id 主键
     * @return 返回结果
     */
    List<Organization> findChildren(Long id);


    /**
     * 根据条件查询单位（分页）
     *
     * @param regionId 省市区id
     * @param name     单位名称
     * @param pageNum  页码
     * @param pageSize 页码大小
     * @return 返回page
     */
    Page<Organization> findPageByCondition(Long regionId, String name, Integer pageNum, Integer pageSize);


    /**
     * 根据条件查询单位（不分页）
     *
     * @param regionId 省市区id
     * @param name     单位名称
     */
    List<Organization> findListByCondition(Long regionId, String name);
}