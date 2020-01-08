package com.bluesky.framework.domain.infrastructure.model.setting.mapper;


import com.bluesky.framework.setting.menu.MenuSourceOperation;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;

import java.util.List;

public interface MenuSourceOperationMapper {
    @Insert("insert into menu_source_operation " +
            "(menu_id, parent_menu_ids, operation, operation_name, created_at, updated_at) " +
            "values " +
            "(#{menuSourceOperation.menuId},#{menuSourceOperation.parentMenuIds},#{menuSourceOperation.operation},#{menuSourceOperation.operationName},now(),now())")
    @SelectKey(before = false, keyProperty = "menuSourceOperation.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    void save(@Param("menuSourceOperation") MenuSourceOperation menuSourceOperation);

    @ResultMap("MenuSourceOperationResultMap")
    @Select("select * from menu_source_operation where id = #{id} limit 1")
    MenuSourceOperation findOne(@Param("id") long id);

    @ResultMap("MenuSourceOperationResultMap")
    @Select("select * from menu_source_operation where menu_id = #{menuId}")
    List<MenuSourceOperation> findByMenuId(@Param("menuId") long menuId);

    @ResultMap("MenuSourceOperationResultMap")
    @Select("select * from menu_source_operation where operation = #{operation} limit 1")
    MenuSourceOperation findByOperation(@Param("operation") String operation);

    /**
     * 更新
     */
    @Update("update menu_source_operation set operation=#{operation},operation_name=#{operationName} where id=#{id}")
    void updateOperation(@Param("id") long id, @Param("operation") String operation, @Param("operationName") String operationName);

    /**
     * 更新操作名称
     */
    @Update("update menu_source_operation set operation_name=#{operationName} where id=#{id}")
    void updateOperationName(@Param("id") long id, @Param("operationName") String operationName);

    /**
     * 删除
     */
    @Delete("delete from menu_source_operation where id=#{id}")
    void delete(@Param("id") long id);

    /**
     * 根据菜单IDs删除
     */
    void deleteByMenuIds(@Param("menuIds") List<Long> menuIds);

    /**
     * 根据主键ids查找
     *
     * @param ids 主键
     * @return 返回集合
     */
    List<MenuSourceOperation> findByIds(@Param("ids") List<Long> ids);
}