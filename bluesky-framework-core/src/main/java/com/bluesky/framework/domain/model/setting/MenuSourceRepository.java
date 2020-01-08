package com.bluesky.framework.domain.model.setting;

import com.bluesky.framework.setting.menu.MenuSource;

import java.util.List;
import java.util.Map;

/**
 * Repository
 *
 * @author linjiang
 */
public interface MenuSourceRepository {
    /**
     * 保存
     *
     * @param menuSource MenuSource
     */
    long save(MenuSource menuSource);

    /**
     * 删除
     */
    void delete(long id);

    /**
     * 根据父ID删除
     */
    void deleteByParentId(long parentId);

    /**
     * 菜单排序
     */
    void sortMenuSource(Map<Long, Integer> data);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id 记录id
     * @return 数据库记录
     */
    MenuSource findOne(long id);

    /**
     * 根据code一条数据库记录
     *
     * @param code 编号
     * @return 数据库记录
     */
    MenuSource findByCode(String code);

    /**
     * 根据主键来更新数据库记录
     *
     * @param id   ID
     * @param name 名称
     */
    void updateName(long id, String name);

    /**
     * 更新叶子菜单名称和URL
     */
    void updateMenuSourceNameUrl(long id, String name, String url);

    /**
     * 根据主键来更新数据库记录
     *
     * @param menuSource 数据库记录
     */
    void updateStyle(MenuSource menuSource);

    /**
     * 获取全部数据库记录
     *
     * @return 数据库记录列表
     */
    List<MenuSource> findByParentId(long parentId);

    /**
     * 获取全部数据库记录
     *
     * @return 数据库记录列表
     */
    List<MenuSource> findByIds(List<Long> ids);

}