package com.bluesky.framework.domain.infrastructure.service.setting;

import com.bluesky.framework.domain.model.setting.PageSettingRepository;
import com.bluesky.framework.domain.service.setting.PageSettingService;
import com.bluesky.framework.setting.page.PageSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PageSettingServiceImpl implements PageSettingService {
    @Autowired
    private PageSettingRepository pageSettingRepository;

    @Override
    public long save(PageSetting pageSetting) {
        return pageSettingRepository.save(pageSetting);
    }

    @Override
    public void updateValueById(long id, String value) {
        PageSetting pageSetting = PageSetting.builder().id(id).value(value).build();
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateDefaultUploadDir(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.DEFAULT.getCode());
        pageSetting.setType(PageSetting.Type.UPLOAD_DIR.getCode());
        List<PageSetting> pageSettingList = pageSettingRepository.findByPage(PageSetting.Page.DEFAULT.getCode());
        if (!pageSettingList.isEmpty()) {
            pageSetting.setId(pageSettingList.get(0).getId());
            pageSettingRepository.updateValue(pageSetting);
        } else {
            pageSettingRepository.save(pageSetting);
        }
    }

    @Override
    public void updateLoginLogo(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.LOGIN.getCode());
        pageSetting.setType(PageSetting.Type.LOGO.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateLoginBackground(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.LOGIN.getCode());
        pageSetting.setType(PageSetting.Type.BACKGROUND.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateLoginType(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.LOGIN.getCode());
        pageSetting.setType(PageSetting.Type.LOGIN_TYPE.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateIndexLogo(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.INDEX.getCode());
        pageSetting.setType(PageSetting.Type.LOGO.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateIndexQueryBackground(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.INDEX.getCode());
        pageSetting.setType(PageSetting.Type.QUERY_BACKGROUND.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateWorkLogo(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.WORK.getCode());
        pageSetting.setType(PageSetting.Type.LOGO.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateWorkTheme(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.WORK.getCode());
        pageSetting.setType(PageSetting.Type.THEME_COLOR.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateCopyRightOrganizer(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.COPY_RIGHT.getCode());
        pageSetting.setType(PageSetting.Type.ORGANIZER.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }

    @Override
    public void updateCopyRightSupport(PageSetting pageSetting) {
        pageSetting.setPage(PageSetting.Page.COPY_RIGHT.getCode());
        pageSetting.setType(PageSetting.Type.SUPPORT.getCode());
        pageSettingRepository.updateValue(pageSetting);
    }
}