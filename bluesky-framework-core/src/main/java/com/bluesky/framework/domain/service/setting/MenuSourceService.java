package com.bluesky.framework.domain.service.setting;

import com.bluesky.framework.setting.menu.MenuSource;
import com.bluesky.framework.setting.menu.MenuSourceOperation;

import java.util.Map;

public interface MenuSourceService {

    /**
     * 保存菜单
     */
    long saveMenuSource(MenuSource menuSource);

    /**
     * 保存菜单操作项
     */
    long saveMenuSourceOperation(MenuSourceOperation menuSourceOperation);

    /**
     * 删除菜单
     */
    void deleteMenuSource(long id);

    /**
     * 菜单排序
     */
    void sortMenuSource(Map<Long, Integer> data);

    /**
     * 更新菜单名称
     */
    void updateMenuSourceName(long menuId, String name);

    /**
     * 更新叶子菜单名称和URL
     */
    void updateMenuSourceNameUrl(long menuId, String name, String url);

    /**
     * 更新菜单首页展现样式
     */
    void updateMenuSourceStyle(MenuSource menuSource);

    /**
     * 更新操作项
     */
    void updateOperation(long id, String operation, String operationName);

    /**
     * 更新操作项
     */
    void updateOperationName(long id, String operationName);

    /**
     * 删除菜单操作项
     */
    void deleteMenuSourceOperation(long id);
}