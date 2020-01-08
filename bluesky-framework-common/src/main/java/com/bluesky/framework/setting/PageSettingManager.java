package com.bluesky.framework.setting;


import com.bluesky.framework.setting.page.PageSetting;

import java.util.List;

public interface PageSettingManager {

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return 页面
     */
    PageSetting findOne(Long id);

    /**
     * 查询全部
     *
     * @return 页面
     */
    List<PageSetting> findAll();

    /**
     * 根据页面查询
     *
     * @param page page
     * @return 页面
     */
    List<PageSetting> findByPage(String page);

    /**
     * 默认上传路径
     */
    void updateDefaultUploadDir(PageSetting pageSetting);

    /**
     * 更新内容
     */
    void updateValueById(Long id, String value);
}