package com.bluesky.framework.domain.infrastructure.model.account.mapper;

import com.bluesky.framework.account.organization.Organization;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface OrganizationMapper {
    @Insert("insert into organization " +
            "(code, name, full_name, unified_social_credit_code,parent_id, area_code, region_id, status, sort, memo, created_at, updated_at) " +
            "values " +
            "(#{organization.code},#{organization.name},#{organization.fullName},#{organization.unifiedSocialCreditCode},#{organization.parentId},#{organization.areaCode},#{organization.regionId},#{organization.status},#{organization.sort},#{organization.memo},now(),now())")
    @SelectKey(before = false, keyProperty = "organization.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void insert(@Param("organization") Organization organization);


    @ResultMap("OrganizationResultMap")
    @Select("select * from organization where id = #{id}")
    Organization findOne(@Param("id") long id);


    void update(@Param("organization") Organization organization);


    /**
     * 根据主键ids查找
     */
    List<Organization> findByIds(@Param("ids") List<Long> ids);


    /**
     * 根据上级查找下级
     *
     * @param id 主键
     * @return 返回结果
     */
    @ResultMap("OrganizationResultMap")
    @Select("select * from organization where status != -10 and parent_id = #{id}")
    List<Organization> findChildren(@Param("id") Long id);


    /**
     * 根据省市区id查询单位
     *
     * @param regionId 省市区id
     * @param name     单位名称
     * @return 返回page
     */
    List<Organization> findByCondition(@Param("regionId") long regionId, @Param("name") String name);

}