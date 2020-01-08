package com.bluesky.framework.domain.infrastructure.model.setting;

import com.bluesky.framework.domain.infrastructure.model.setting.mapper.MenuSourceOperationMapper;
import com.bluesky.framework.domain.model.setting.MenuSourceOperationRepository;
import com.bluesky.framework.setting.menu.MenuSourceOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MenuSourceOperationRepositoryImpl implements MenuSourceOperationRepository {

    @Autowired
    private MenuSourceOperationMapper menuSourceOperationMapper;

    @Override
    public long save(MenuSourceOperation menuSourceOperation) {
        menuSourceOperationMapper.save(menuSourceOperation);
        return menuSourceOperation.getId();
    }

    @Override
    public MenuSourceOperation findOne(long id) {
        return menuSourceOperationMapper.findOne(id);
    }

    @Override
    public void updateOperation(long id, String operation, String operationName) {
        menuSourceOperationMapper.updateOperation(id, operation, operationName);
    }

    @Override
    public void updateOperationName(long id, String operationName) {
        menuSourceOperationMapper.updateOperationName(id, operationName);
    }

    @Override
    public void delete(long id) {
        menuSourceOperationMapper.delete(id);
    }

    @Override
    public void deleteByMenuIds(List<Long> menuIds) {
        menuSourceOperationMapper.deleteByMenuIds(menuIds);
    }

    @Override
    public List<MenuSourceOperation> findByMenuId(long menuId) {
        return menuSourceOperationMapper.findByMenuId(menuId);
    }

    @Override
    public List<MenuSourceOperation> findByIds(List<Long> ids) {
        return menuSourceOperationMapper.findByIds(ids);
    }

    @Override
    public MenuSourceOperation findByOperation(String operation) {
        return menuSourceOperationMapper.findByOperation(operation);
    }
}