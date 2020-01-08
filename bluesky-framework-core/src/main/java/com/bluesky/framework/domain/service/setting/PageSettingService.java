package com.bluesky.framework.domain.service.setting;

import com.bluesky.framework.setting.page.PageSetting;

public interface PageSettingService {

    long save(PageSetting pageSetting);

    /**
     * 默认上传目录
     */
    void updateDefaultUploadDir(PageSetting pageSetting);

    /**
     * 更新内容
     */
    void updateValueById(long id, String value);

    /**
     * 登录页logo
     */
    void updateLoginLogo(PageSetting pageSetting);

    /**
     * 登录页背景
     */
    void updateLoginBackground(PageSetting pageSetting);

    /**
     * 登录方式
     */
    void updateLoginType(PageSetting pageSetting);

    /**
     * 首页logo
     */
    void updateIndexLogo(PageSetting pageSetting);

    /**
     * 首页查询背景
     */
    void updateIndexQueryBackground(PageSetting pageSetting);

    /**
     * 工作页LOGO
     */
    void updateWorkLogo(PageSetting pageSetting);

    /**
     * 工作页主题颜色
     */
    void updateWorkTheme(PageSetting pageSetting);

    /**
     * 版权主办方
     */
    void updateCopyRightOrganizer(PageSetting pageSetting);

    /**
     * 版权技术支持
     */
    void updateCopyRightSupport(PageSetting pageSetting);


}