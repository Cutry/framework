package com.bluesky.framework.domain.infrastructure.model.setting;

import com.bluesky.framework.domain.infrastructure.model.setting.mapper.MenuSourceMapper;
import com.bluesky.framework.domain.model.setting.MenuSourceRepository;
import com.bluesky.framework.setting.menu.MenuSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
class MenuSourceRepositoryImpl implements MenuSourceRepository {

    @Autowired
    private MenuSourceMapper menuSourceMapper;

    @Override
    public void sortMenuSource(Map<Long, Integer> data) {
        menuSourceMapper.sortMenuSource(data);
    }

    @Override
    public long save(MenuSource menuSource) {
        menuSourceMapper.save(menuSource);
        return menuSource.getId();
    }

    @Override
    public void delete(long id) {
        menuSourceMapper.delete(id);
    }

    @Override
    public void deleteByParentId(long parentId) {
        menuSourceMapper.deleteByParentId(parentId);
    }

    @Override
    public MenuSource findOne(long id) {
        return menuSourceMapper.findOne(id);
    }

    @Override
    public MenuSource findByCode(String code) {
        return menuSourceMapper.findByCode(code);
    }

    @Override
    public void updateName(long id, String name) {
        menuSourceMapper.updateName(id, name);
    }

    @Override
    public void updateMenuSourceNameUrl(long id, String name, String url) {
        menuSourceMapper.updateMenuSourceNameUrl(id, name, url);
    }

    @Override
    public void updateStyle(MenuSource menuSource) {
        menuSourceMapper.updateStyle(menuSource);
    }

    @Override
    public List<MenuSource> findByParentId(long parentId) {
        return menuSourceMapper.findByParentId(parentId);
    }

    @Override
    public List<MenuSource> findByIds(List<Long> ids) {
        return menuSourceMapper.findByIds(ids);
    }
}