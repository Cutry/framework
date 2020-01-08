package com.bluesky.framework.domain.infrastructure.model.account;

import com.bluesky.framework.account.region.Region;
import com.bluesky.framework.domain.infrastructure.model.account.mapper.RegionMapper;
import com.bluesky.framework.domain.model.account.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class RegionRepositoryImpl implements RegionRepository {
    @Autowired
    public RegionMapper regionMapper;


    @Override
    public long insert(Region region) {
        regionMapper.insert(region);
        return region.getId();
    }


    @Override
    public Region findOne(long id) {
        return regionMapper.findOne(id);
    }


    @Override
    public void delete(long id) {
        regionMapper.delete(id);
    }


    @Override
    public void update(Region region) {
        regionMapper.update(region);
    }


    @Override
    public boolean isCodeExist(String code) {
        int count = regionMapper.isCodeExist(code);
        return count > 0;
    }


    @Override
    public List<Region> findByParentId(long parentId) {
        return regionMapper.findByParentId(parentId);
    }


    @Override
    public boolean isInit() {
        return regionMapper.isInit();
    }


    @Override
    public void updateParentCodeByParentId(long parentId, String parentCode) {
        regionMapper.updateParentCodeByParentId(parentId, parentCode);
    }


    @Override
    public List<Region> findAll() {
        return regionMapper.findAll();
    }


    @Override
    public void updateSystemLevel(int level) {
        regionMapper.updateSystemLevel(level);
    }


    @Override
    public int findSystemLevel() {
        return regionMapper.findSystemLevel();
    }


    @Override
    public List<Region> findByLevel(int level) {
        return regionMapper.findByLevel(level);
    }
}