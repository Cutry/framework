package com.bluesky.framework.domain.model.account;


import com.bluesky.framework.account.region.Region;

import java.util.List;

/**
 * 省市区管理Repository
 */
public interface RegionRepository {

    long insert(Region region);


    Region findOne(long id);


    void delete(long id);


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
     * 根据父级的id修改父类的code
     *
     * @param parentCode 父级code
     * @param parentId   父级id
     */
    void updateParentCodeByParentId(long parentId, String parentCode);


    /**
     * 查询所有区域
     *
     * @return 返回所有省市区集合
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
     * 根据级别查询省市区
     *
     * @param level 级别
     */
    List<Region> findByLevel(int level);

}