package com.bluesky.framework.domain.model.setting;

import com.bluesky.framework.setting.menu.MenuSourceOperation;

import java.util.List;

/**
 * Repository
 *
 * @author linjiang
 */
public interface MenuSourceOperationRepository {
    /**
     * 保存
     *
     * @param menuSourceOperation MenuSourceOperation
     */
    long save(MenuSourceOperation menuSourceOperation);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id 记录id
     * @return 数据库记录
     */
    MenuSourceOperation findOne(long id);

    /**
     * 根据主键来更新数据库记录
     *
     * @param id ID
     */
    void updateOperation(long id, String operation, String operationName);

    /**
     * 根据主键来更新数据库记录
     *
     * @param id            ID
     * @param operationName 名称
     */
    void updateOperationName(long id, String operationName);

    /**
     * 根据主键来删除
     *
     * @param id ID
     */
    void delete(long id);

    /**
     * 根据菜单IDs来删除
     *
     * @param menuIds 菜单IDs
     */
    void deleteByMenuIds(List<Long> menuIds);

    /**
     * 根据menuId获取全部数据库记录
     *
     * @return 数据库记录列表
     */
    List<MenuSourceOperation> findByMenuId(long menuId);

    /**
     * 根据menuId获取全部数据库记录
     *
     * @return 数据库记录列表
     */
    List<MenuSourceOperation> findByIds(List<Long> ids);

    /**
     * 根据操作项查询，用于校验是否唯一
     */
    MenuSourceOperation findByOperation(String operation);
}