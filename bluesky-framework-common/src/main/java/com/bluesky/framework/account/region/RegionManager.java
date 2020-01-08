package com.bluesky.framework.account.region;


import java.util.List;

/**
 * 省市区管理Manager
 */
public interface RegionManager {

    long insert(Region region);


    Region findOne(long id);


    void update(Region region);


    /**
     * 检查code是否已经存在
     *
     * @param code 区域代码
     * @return true 存在，false不存在
     */
    boolean isCodeExist(String code);


    /**
     * 根据父级id查询
     *
     * @param parentId 父级id
     * @return 所有子级集合
     */
    List<Region> findByParentId(long parentId);


    /**
     * 查询是否已经初始化
     *
     * @return true已经初始化，false未初始化
     */
    boolean isInit();


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


    /**
     * 查询全部
     */
    List<Region> findAll();


    /**
     * 更新系统为省级/市级/或者县级系统
     *
     * @param level 级别
     */
    void updateSystemLevel(int level);


    /**
     * 查询系统级别
     *
     * @return 返回当前系统级别
     */
    int findSystemLevel();


    /**
     * 逻辑删除
     *
     * @param id 主键
     */
    void delete(long id);


    /**
     * 根据级别查询省市区
     *
     * @param level 级别
     */
    List<Region> findByLevel(int level);
}