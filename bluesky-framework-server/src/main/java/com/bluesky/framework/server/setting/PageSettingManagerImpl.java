package com.bluesky.framework.server.setting;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.framework.domain.model.setting.PageSettingRepository;
import com.bluesky.framework.domain.service.setting.PageSettingService;
import com.bluesky.framework.setting.PageSettingManager;
import com.bluesky.framework.setting.page.PageSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * manager
 *
 * @author linjiang
 */
@Component
@DubboService
public class PageSettingManagerImpl implements PageSettingManager {
    @Autowired
    private PageSettingService pageSettingService;
    @Autowired
    private PageSettingRepository pageSettingRepository;

    @Override
    public PageSetting findOne(Long id) {
        return pageSettingRepository.findOne(id);
    }

    @Override
    public List<PageSetting> findAll() {
        return pageSettingRepository.findAll();
    }

    @Override
    public List<PageSetting> findByPage(String page) {
        return pageSettingRepository.findByPage(page);
    }

    @Override
    public void updateDefaultUploadDir(PageSetting pageSetting) {
        pageSettingService.updateDefaultUploadDir(pageSetting);
    }

    @Override
    public void updateValueById(Long id, String value) {
        pageSettingService.updateValueById(id, value);
    }
}