package com.bluesky.framework.domain.model.setting;

import com.bluesky.framework.setting.page.PageSetting;

import java.util.List;

/**
 * 数据目录Repository
 *
 * @author linjiang
 */
public interface PageSettingRepository {
    /**
     * 保存
     *
     * @param pageSetting PageSetting
     */
    long save(PageSetting pageSetting);

    /**
     * 根据主键获取一条数据库记录
     *
     * @param id 记录id
     * @return 数据库记录
     */
    PageSetting findOne(long id);

    /**
     * 根据主键来更新数据库记录
     *
     * @param pageSetting 数据库记录
     */
    void updateValue(PageSetting pageSetting);

    /**
     * 获取全部数据库记录
     *
     * @return 数据库记录列表
     */
    List<PageSetting> findAll();

    /**
     * 获取全部数据库记录
     *
     * @return 数据库记录列表
     */
    List<PageSetting> findByPage(String page);

}