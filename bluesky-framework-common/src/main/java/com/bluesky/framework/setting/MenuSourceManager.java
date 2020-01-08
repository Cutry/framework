package com.bluesky.framework.setting;


import com.bluesky.framework.setting.menu.MenuSource;
import com.bluesky.framework.setting.menu.MenuSourceOperation;

import java.util.List;
import java.util.Map;

/**
 * 菜单manager接口
 */
public interface MenuSourceManager {

    /**
     * 新增菜单
     */
    void saveMenuSource(MenuSource menuSource);

    /**
     * 更新菜单名称
     */
    void updateMenuSourceName(Long menuId, String name);

    /**
     * 更新叶子菜单名称和URL
     */
    void updateMenuSourceNameUrl(Long menuId, String name, String url);

    /**
     * 菜单排序
     */
    void sortMenuSource(Map<Long, Integer> data);

    /**
     * 更新菜单首页展现样式
     */
    void updateMenuSourceStyle(MenuSource menuSource);

    /**
     * 参数菜单
     */
    void deleteMenuSource(Long id);

    /**
     * 新增菜单操作项
     */
    void saveMenuSourceOperation(MenuSourceOperation menuSourceOperation);

    /**
     * 更新操作项
     */
    void updateOperationName(Long id, String operationName);

    /**
     * 删除菜单操作项
     */
    void deleteMenuSourceOperation(Long id);

    /**
     * 根据ID查询
     */
    MenuSource findOne(Long id);

    /**
     * 根据菜单父ID查询
     */
    List<MenuSource> findByParentId(Long parentId);

    /**
     * 根据菜单父ID查询
     */
    List<MenuSourceOperation> findOperationByMenuId(Long menuId);

    /**
     * 查询全部菜单
     */
    List<MenuSource> findAllMenu();

    /**
     * 查询全部菜单及操作项
     */
    List<MenuSource> findAllMenuAndOperation();

    /**
     * 根据操作项IDs查询全部菜单及操作项
     */
    List<MenuSource> findByOperationIds(List<Long> operationIds);

    /**
     * 操作项编码唯一性检查
     * 不存在返回true，否则返回false
     */
    Boolean isOperationUnique(String operation);

    /**
     * 菜单编码唯一性检查
     * 不存在返回true，否则返回false
     */
    Boolean isMenuCodeUnique(String code);
}