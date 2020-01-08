package com.bluesky.framework.domain.infrastructure.model.setting.mapper;

import com.bluesky.framework.setting.menu.MenuSource;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;
import java.util.Map;

public interface MenuSourceMapper {
    @Insert("insert into menu_source " +
            "(code, name, url, system_menu, parent_id, seq, created_at, updated_at) " +
            "values " +
            "(#{menuSource.code},#{menuSource.name}, #{menuSource.url}, #{menuSource.systemMenu},#{menuSource.parentId},#{menuSource.seq}, now(),now()) ")
    @SelectKey(before = false, keyProperty = "menuSource.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void save(@Param("menuSource") MenuSource menuSource);

    /**
     * 删除
     */
    @Delete("delete from menu_source where id=#{id}")
    void delete(@Param("id") long id);

    /**
     * 根据父ID删除
     */
    @Delete("delete from menu_source where parent_id=#{parentId}")
    void deleteByParentId(@Param("parentId") long parentId);

    /**
     * 根据主键查询
     */
    @ResultMap("MenuSourceResultMap")
    @Select("select * from menu_source where id = #{id}")
    MenuSource findOne(@Param("id") long id);

    /**
     * 根据父ID查询
     */
    @ResultMap("MenuSourceResultMap")
    @Select("select * from menu_source where parent_id = #{parentId} order by seq")
    List<MenuSource> findByParentId(@Param("parentId") long parentId);

    /**
     * 根据code查询，保证code不唯一
     */
    @ResultMap("MenuSourceResultMap")
    @Select("select * from menu_source where code = #{code} limit 1")
    MenuSource findByCode(@Param("code") String code);

    /**
     * 菜单排序
     */
    void sortMenuSource(@Param("data") Map<Long, Integer> data);

    /**
     * 更新菜单名称
     */
    @Update("update menu_source set name=#{name} where id=#{id}")
    void updateName(@Param("id") long id, @Param("name") String name);

    /**
     * 更新叶子菜单名称和URL
     */
    @Update("update menu_source set name=#{name},url=#{url} where id=#{id}")
    void updateMenuSourceNameUrl(@Param("id") long id, @Param("name") String name,
                                 @Param("url") String url);

    /**
     * 更新首页展示样式
     */
    void updateStyle(@Param("menuSource") MenuSource menuSource);

    /**
     * 根据主键ids查找
     *
     * @param ids 主键
     * @return 返回集合
     */
    List<MenuSource> findByIds(@Param("ids") List<Long> ids);
}