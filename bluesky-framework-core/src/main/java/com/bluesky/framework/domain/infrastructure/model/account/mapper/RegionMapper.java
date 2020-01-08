package com.bluesky.framework.domain.infrastructure.model.account.mapper;

import com.bluesky.framework.account.region.Region;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface RegionMapper {
    @Insert("insert into region " +
            "(name, level, code, parent_id, parent_code, top, leaf,status, created_at, updated_at) " +
            "values " +
            "(#{region.name},#{region.level},#{region.code},#{region.parentId},#{region.parentCode},#{region.top},#{region.leaf},0,now(),now())")
    @SelectKey(before = false, keyProperty = "region.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void insert(@Param("region") Region region);


    @ResultMap("RegionResultMap")
    @Select("select * from region where id = #{id} and status != -5")
    Region findOne(@Param("id") long id);


    @Delete("delete from region where id = #{id}")
    void delete(@Param("id") Long id);

    /**
     * 检查code是否已经存在
     *
     * @param code 区域代码
     * @return true 存在，false不存在
     */
    @Select("select count(*) from region where code = #{code} ")
    int isCodeExist(String code);


    void update(@Param("region") Region region);


    /**
     * 根据父级id查询
     *
     * @param parentId 父级id
     * @return 所有子级集合
     */
    @ResultMap("RegionResultMap")
    @Select("select * from region where parent_id = #{parentId} and status != -5 order by code")
    List<Region> findByParentId(Long parentId);


    /**
     * 查询是否已经初始化
     *
     * @return true已经初始化，false未初始化
     */
    @Select("select count(*) from region where status != -5 limit 1")
    boolean isInit();


    /**
     * 根据父级的id修改父类的code
     *
     * @param parentCode 父级code
     * @param parentId   父级id
     */
    @Update("update region set parent_code = #{parentCode} where parent_id = #{parentId}")
    void updateParentCodeByParentId(@Param("parentId") long parentId, @Param("parentCode") String parentCode);


    /**
     * 查询所有区域
     *
     * @return 返回所有省市区集合
     */
    @ResultMap("RegionResultMap")
    @Select("select * from region where status != -5")
    List<Region> findAll();


    void updateSystemLevel(@Param("level") int level);


    /**
     * 查询系统级别
     *
     * @return 返回当前系统级别
     */
    @Select("select level from region where top = 1 and status != -5 limit 1")
    int findSystemLevel();


    /**
     * 根据级别查询省市区
     *
     * @param level 级别
     */
    @ResultMap("RegionResultMap")
    @Select("select * from region where level = #{level} and  status != -5")
    List<Region> findByLevel(@Param("level") int level);
}