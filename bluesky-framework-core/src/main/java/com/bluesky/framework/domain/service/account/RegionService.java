package com.bluesky.framework.domain.service.account;

import com.bluesky.framework.account.region.Region;

/**
 * 省市区管理Service
 */
public interface RegionService {

    long insert(Region region);


    void update(Region region);


    /**
     * 更新系统为省级/市级/或者县级系统
     *
     * @param level 级别
     */
    void updateSystemLevel(int level);


    /**
     * 逻辑删除
     *
     * @param id 主键
     */
    void delete(long id);
}