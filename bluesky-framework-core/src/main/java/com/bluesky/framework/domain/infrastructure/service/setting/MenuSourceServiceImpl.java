package com.bluesky.framework.domain.infrastructure.service.setting;

import com.bluesky.framework.domain.model.setting.MenuSourceOperationRepository;
import com.bluesky.framework.domain.model.setting.MenuSourceRepository;
import com.bluesky.framework.domain.service.setting.MenuSourceService;
import com.bluesky.framework.setting.menu.MenuSource;
import com.bluesky.framework.setting.menu.MenuSourceOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class MenuSourceServiceImpl implements MenuSourceService {
    @Autowired
    private MenuSourceRepository menuSourceRepository;
    @Autowired
    private MenuSourceOperationRepository menuSourceOperationRepository;

    @Override
    public long saveMenuSource(MenuSource menuSource) {
        //保存菜单
        return menuSourceRepository.save(menuSource);
    }

    @Override
    public void sortMenuSource(Map<Long, Integer> data) {
        menuSourceRepository.sortMenuSource(data);
    }

    @Override
    public long saveMenuSourceOperation(MenuSourceOperation menuSourceOperation) {
        return menuSourceOperationRepository.save(menuSourceOperation);
    }

    @Override
    public void deleteMenuSource(long id) {
        MenuSource menuSource = menuSourceRepository.findOne(id);
        if (menuSource == null) {
            return;
        }
        //系统菜单不能删除，自定义的菜单才可以
        if (!menuSource.getSystemMenu()) {
            //删除自己
            menuSourceRepository.delete(id);
            //获取子菜单
            if (Objects.equals(menuSource.getParentId(), 0L)) {
                List<MenuSource> children = menuSourceRepository.findByParentId(id);
                if (!children.isEmpty()) {
                    //删除子菜单
                    menuSourceRepository.deleteByParentId(id);
                    List<Long> menuIds = children.stream().map(MenuSource::getId).collect(Collectors.toList());
                    menuSourceOperationRepository.deleteByMenuIds(menuIds);
                }
            } else {
                //删除的是子菜单
                List<Long> menuIds = Collections.singletonList(id);
                menuSourceOperationRepository.deleteByMenuIds(menuIds);
            }
        }
    }

    @Override
    public void updateMenuSourceName(long menuId, String name) {
        menuSourceRepository.updateName(menuId, name);
    }

    @Override
    public void updateMenuSourceNameUrl(long menuId, String name, String url) {
        menuSourceRepository.updateMenuSourceNameUrl(menuId, name, url);
    }

    @Override
    public void updateMenuSourceStyle(MenuSource menuSource) {
        if (StringUtils.isNotEmpty(menuSource.getBackgroundColor()) || StringUtils.isNotEmpty(menuSource.getIcon()) ||
                StringUtils.isNotEmpty(menuSource.getIconColor()) || StringUtils.isNotEmpty(menuSource.getColor())) {
            menuSourceRepository.updateStyle(menuSource);
        }
    }

    @Override
    public void updateOperation(long id, String operation, String operationName) {
        menuSourceOperationRepository.updateOperation(id, operation, operationName);
    }

    @Override
    public void updateOperationName(long id, String operationName) {
        menuSourceOperationRepository.updateOperationName(id, operationName);
    }

    @Override
    public void deleteMenuSourceOperation(long id) {
        MenuSourceOperation operation = menuSourceOperationRepository.findOne(id);
        if (operation == null) {
            return;
        }
        MenuSource menuSource = menuSourceRepository.findOne(operation.getMenuId());
        if (menuSource == null) {
            return;
        }
        //系统菜单的操作项不能删除
        if (!menuSource.getSystemMenu()) {
            menuSourceOperationRepository.delete(id);
        }
    }
}