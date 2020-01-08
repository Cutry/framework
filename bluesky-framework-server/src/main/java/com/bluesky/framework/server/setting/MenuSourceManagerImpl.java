package com.bluesky.framework.server.setting;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.framework.domain.factory.setting.MenuSourceFactory;
import com.bluesky.framework.domain.model.setting.MenuSourceOperationRepository;
import com.bluesky.framework.domain.model.setting.MenuSourceRepository;
import com.bluesky.framework.domain.service.setting.MenuSourceService;
import com.bluesky.framework.domain.specification.setting.MenuSourceSpec;
import com.bluesky.framework.setting.MenuSourceManager;
import com.bluesky.framework.setting.menu.MenuSource;
import com.bluesky.framework.setting.menu.MenuSourceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * manager
 *
 * @author linjiang
 */
@Component
@DubboService
public class MenuSourceManagerImpl implements MenuSourceManager {
    @Autowired
    private MenuSourceRepository menuSourceRepository;
    @Autowired
    private MenuSourceOperationRepository menuSourceOperationRepository;
    @Autowired
    private MenuSourceService menuSourceService;
    @Autowired
    private MenuSourceFactory menuSourceFactory;
    @Autowired
    private MenuSourceSpec menuSourceSpec;

    @Override
    public void saveMenuSource(MenuSource menuSource) {
        menuSourceService.saveMenuSource(menuSource);
    }

    @Override
    public void updateMenuSourceName(Long menuId, String name) {
        menuSourceService.updateMenuSourceName(menuId, name);
    }

    @Override
    public void updateMenuSourceNameUrl(Long menuId, String name, String url) {
        menuSourceService.updateMenuSourceNameUrl(menuId, name, url);
    }

    @Override
    public void sortMenuSource(Map<Long, Integer> data) {
        menuSourceService.sortMenuSource(data);
    }

    @Override
    public void updateMenuSourceStyle(MenuSource menuSource) {
        menuSourceService.updateMenuSourceStyle(menuSource);
    }

    @Override
    public void deleteMenuSource(Long id) {
        menuSourceService.deleteMenuSource(id);
    }

    @Override
    public void saveMenuSourceOperation(MenuSourceOperation menuSourceOperation) {
        menuSourceService.saveMenuSourceOperation(menuSourceOperation);
    }

    @Override
    public void updateOperationName(Long id, String operationName) {
        menuSourceService.updateOperationName(id, operationName);
    }

    @Override
    public void deleteMenuSourceOperation(Long id) {
        menuSourceService.deleteMenuSourceOperation(id);
    }

    @Override
    public MenuSource findOne(Long id) {
        return menuSourceRepository.findOne(id);
    }

    @Override
    public List<MenuSource> findByParentId(Long parentId) {
        return menuSourceRepository.findByParentId(parentId);
    }

    @Override
    public List<MenuSourceOperation> findOperationByMenuId(Long menuId) {
        return menuSourceOperationRepository.findByMenuId(menuId);
    }

    @Override
    public List<MenuSource> findAllMenu() {
        return menuSourceFactory.findAllMenu();
    }

    @Override
    public List<MenuSource> findAllMenuAndOperation() {
        return menuSourceFactory.findAllMenuAndOperation();
    }

    @Override
    public List<MenuSource> findByOperationIds(List<Long> operationIds) {
        return menuSourceFactory.findByOperationIds(operationIds);
    }

    @Override
    public Boolean isOperationUnique(String operation) {
        return menuSourceSpec.isOperationUnique(operation);
    }

    @Override
    public Boolean isMenuCodeUnique(String code) {
        return menuSourceSpec.isMenuCodeUnique(code);
    }
}