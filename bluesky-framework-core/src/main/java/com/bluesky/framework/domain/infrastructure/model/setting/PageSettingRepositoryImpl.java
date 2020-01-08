package com.bluesky.framework.domain.infrastructure.model.setting;

import com.bluesky.framework.domain.infrastructure.model.setting.mapper.PageSettingMapper;
import com.bluesky.framework.domain.model.setting.PageSettingRepository;
import com.bluesky.framework.setting.page.PageSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PageSettingRepositoryImpl implements PageSettingRepository {

    @Autowired
    private PageSettingMapper pageSettingMapper;

    @Override
    public long save(PageSetting pageSetting) {
        pageSettingMapper.save(pageSetting);
        return pageSetting.getId();
    }

    @Override
    public PageSetting findOne(long id) {
        return pageSettingMapper.findOne(id);
    }

    @Override
    public void updateValue(PageSetting pageSetting) {
        pageSettingMapper.updateValue(pageSetting);
    }

    @Override
    public List<PageSetting> findAll() {
        return pageSettingMapper.findAll();
    }

    @Override
    public List<PageSetting> findByPage(String page) {
        return pageSettingMapper.findByPage(page);
    }
}