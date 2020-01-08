package com.bluesky.framework.server.account;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.framework.account.region.Region;
import com.bluesky.framework.account.region.RegionManager;
import com.bluesky.framework.domain.factory.account.RegionFactory;
import com.bluesky.framework.domain.model.account.RegionRepository;
import com.bluesky.framework.domain.service.account.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DubboService
public class RegionManagerImpl implements RegionManager {
    @Autowired
    private RegionService regionService;
    @Autowired
    private RegionRepository regionRepository;
    @Autowired
    private RegionFactory regionFactory;


    @Override
    public long insert(Region region) {
        return regionService.insert(region);
    }


    @Override
    public Region findOne(long id) {
        return regionRepository.findOne(id);
    }


    @Override
    public void update(Region region) {
        regionService.update(region);
    }


    @Override
    public boolean isCodeExist(String code) {
        return regionRepository.isCodeExist(code);
    }


    @Override
    public List<Region> findByParentId(long parentId) {
        return regionRepository.findByParentId(parentId);
    }


    @Override
    public boolean isInit() {
        return regionRepository.isInit();
    }


    @Override
    public List<Region> findAllTree(boolean underSystemLevel) {
        return regionFactory.findAllTree(underSystemLevel);
    }


    @Override
    public Region findParentTree(long currentRegionId, boolean underSystemLevel) {
        return regionFactory.findParentTree(currentRegionId, underSystemLevel);
    }


    @Override
    public List<Region> findParentList(long currentRegionId, boolean underSystemLevel) {
        return regionFactory.findParentList(currentRegionId, underSystemLevel);
    }


    @Override
    public List<Region> findAll() {
        return regionRepository.findAll();
    }


    @Override
    public void updateSystemLevel(int level) {
        regionService.updateSystemLevel(level);
    }


    @Override
    public int findSystemLevel() {
        return regionRepository.findSystemLevel();
    }


    @Override
    public void delete(long id) {
        regionService.delete(id);
    }


    @Override
    public List<Region> findByLevel(int level) {
        return regionRepository.findByLevel(level);
    }
}