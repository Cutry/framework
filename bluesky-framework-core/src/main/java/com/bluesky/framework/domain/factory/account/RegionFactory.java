package com.bluesky.framework.domain.factory.account;

import com.bluesky.framework.account.region.Region;

import java.util.List;


/**
 * 省市区管理Factory
 */
public interface RegionFactory {

    /**
     * 查询全部区域，包装成树状结构返回
     *
     * @param underSystemLevel false时市级系统会返回：省->市->区。true时只查系统级别下的：市->区
     */
    List<Region> findAllTree(boolean underSystemLevel);


    /**
     * 查询上级区域，上上级区域树，包装成树状结构返回
     *
     * @param underSystemLevel false时市级系统会返回：省->市->区。true时只查系统级别下的：市->区
     * @param currentRegionId  当前区域id
     */
    Region findParentTree(long currentRegionId, boolean underSystemLevel);


    /**
     * 查询上级，上上级按顺序放入list，当前级别在最后
     *
     * @param currentRegionId  当前区域id
     * @param underSystemLevel false时市级系统会返回：省->市->区。true时只查系统级别下的：市->区
     */
    List<Region> findParentList(long currentRegionId, boolean underSystemLevel);

}