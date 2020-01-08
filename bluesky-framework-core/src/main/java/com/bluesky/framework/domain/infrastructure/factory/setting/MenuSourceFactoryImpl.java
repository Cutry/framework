package com.bluesky.framework.domain.infrastructure.factory.setting;

import com.bluesky.framework.domain.factory.setting.MenuSourceFactory;
import com.bluesky.framework.domain.model.setting.MenuSourceOperationRepository;
import com.bluesky.framework.domain.model.setting.MenuSourceRepository;
import com.bluesky.framework.setting.menu.MenuSource;
import com.bluesky.framework.setting.menu.MenuSourceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * setting factory
 *
 * @author linjiang
 */
@Component
public class MenuSourceFactoryImpl implements MenuSourceFactory {
    @Autowired
    private MenuSourceRepository menuSourceRepository;

    @Autowired
    private MenuSourceOperationRepository menuSourceOperationRepository;

    @Override
    public List<MenuSource> findAllMenuAndOperation() {
        List<MenuSource> topMenuSourceList = menuSourceRepository.findByParentId(0);
        findChildMenuAndOperation(topMenuSourceList);
        //过滤没有二级菜单的菜单，没有二级菜单不能进行页面跳转
        return topMenuSourceList.stream().filter(it -> it.getChildMenuSourceList() != null && !it.getChildMenuSourceList().isEmpty()).collect(Collectors.toList());
    }

    @Override
    public List<MenuSource> findAllMenu() {
        List<MenuSource> topMenuSourceList = menuSourceRepository.findByParentId(0);
        findChildMenu(topMenuSourceList);
        return topMenuSourceList;
    }

    @Override
    public List<MenuSource> findByOperationIds(List<Long> operationIds) {
        if (operationIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<MenuSourceOperation> operationList = menuSourceOperationRepository.findByIds(operationIds);
        Map<Long, List<MenuSourceOperation>> menuOperationListMap = operationList.stream().collect(Collectors.groupingBy(MenuSourceOperation::getMenuId));
        List<MenuSource> menuSourceList = menuSourceRepository.findByIds(new ArrayList<>(menuOperationListMap.keySet()));
        Map<Long, List<MenuSource>> menuSourceListMap = menuSourceList.stream().collect(Collectors.groupingBy(MenuSource::getParentId));

        List<MenuSource> topMenuSourceList = menuSourceRepository.findByIds(new ArrayList<>(menuSourceListMap.keySet()));
        //目前系统针对两级菜单栏
        topMenuSourceList.forEach((it) -> {
            it.setChildMenuSourceList(menuSourceListMap.get(it.getId()));
            if (it.getChildMenuSourceList() != null) {
                for (MenuSource childMenu : it.getChildMenuSourceList()) {
                    childMenu.setOperationList(menuOperationListMap.get(childMenu.getId()));
                }
            }
        });
        return topMenuSourceList;
    }

    /**
     * 查找子菜单及其操作项
     */
    private void findChildMenuAndOperation(List<MenuSource> menuSourceList) {
        for (MenuSource menuSource : menuSourceList) {
            List<MenuSource> childMenuSourceList = menuSourceRepository.findByParentId(menuSource.getId());
            if (!childMenuSourceList.isEmpty()) {
                menuSource.setChildMenuSourceList(childMenuSourceList);
                findChildMenuAndOperation(childMenuSourceList);
            } else {
                List<MenuSourceOperation> operationList = menuSourceOperationRepository.findByMenuId(menuSource.getId());
                menuSource.setOperationList(operationList);
            }
        }
    }

    /**
     * 查找子菜单及其操作项
     */
    private void findChildMenu(List<MenuSource> menuSourceList) {
        for (MenuSource menuSource : menuSourceList) {
            List<MenuSource> childMenuSourceList = menuSourceRepository.findByParentId(menuSource.getId());
            if (!childMenuSourceList.isEmpty()) {
                menuSource.setChildMenuSourceList(childMenuSourceList);
                findChildMenu(childMenuSourceList);
            }
        }
    }
}