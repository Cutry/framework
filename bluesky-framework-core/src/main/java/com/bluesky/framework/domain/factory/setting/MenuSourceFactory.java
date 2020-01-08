package com.bluesky.framework.domain.factory.setting;

import com.bluesky.framework.setting.menu.MenuSource;

import java.util.List;


/**
 * setting factory
 *
 * @author linjiang
 */
public interface MenuSourceFactory {
    /**
     * 查询全部菜单及操作项
     */
    List<MenuSource> findAllMenuAndOperation();

    /**
     * 查询全部菜单
     */
    List<MenuSource> findAllMenu();

    /**
     * 查询全部菜单及操作项
     */
    List<MenuSource> findByOperationIds(List<Long> operationIds);
}