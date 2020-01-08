package com.bluesky.framework.domain.infrastructure.factory.account;

import com.bluesky.framework.account.region.Region;
import com.bluesky.framework.account.region.RegionVO;
import com.bluesky.framework.domain.factory.account.RegionFactory;
import com.bluesky.framework.domain.model.account.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Component
public class RegionFactoryImpl implements RegionFactory {
    @Autowired
    private RegionRepository regionRepository;


    @Override
    public List<Region> findAllTree(boolean underSystemLevel) {
        List<Region> all = regionRepository.findAll().stream().sorted(Comparator.comparing(Region::getLevel)).collect(Collectors.toList());

        // 检查是否需要查询全部树结构，false时一直查询到parentId=0的省级区域，true时查到top=true的系统级别就可以结束
        List<RegionVO> result;
        if (underSystemLevel) {
            result = all.stream().filter(Region::getTop).map(RegionVO::new).collect(Collectors.toList());
        } else {
            result = all.stream().filter((it) -> Objects.equals(0L, it.getParentId())).map(RegionVO::new).collect(Collectors.toList());
        }
        // 递归查询每个区域的下级
        result.forEach(it -> this.recursivePackage(it, all));
        return new ArrayList<>(result);
    }


    /**
     * 递归组装所有的省市区
     *
     * @param region 当前区域
     * @param all    全部区域
     */
    private void recursivePackage(RegionVO region, List<Region> all) {
        if (region.getLeaf()) return;
        List<RegionVO> children = all.stream()
                .filter(it -> Objects.equals(it.getParentId(), region.getId()))
                .map(RegionVO::new)
                .collect(Collectors.toList());

        children.forEach(it -> recursivePackage(it, all));

        region.setChildren(new ArrayList<>(children));
    }


    @Override
    public Region findParentTree(long currentRegionId, boolean underSystemLevel) {
        // 用户单位所在省市区
        RegionVO region = new RegionVO(regionRepository.findOne(currentRegionId));
        List<Region> all = regionRepository.findAll();
        // 递归查询到最顶级
        return this.recursiveFindHigherLevelRegion(region, all, underSystemLevel);
    }


    /**
     * 递归往上级查询区域
     *
     * @param regionVO         当前区域
     * @param allRegion        全部区域
     * @param underSystemLevel false时市级系统会返回：省->市->区。true时只查系统级别下的：市->区
     * @return 返回最顶级区域
     */
    private RegionVO recursiveFindHigherLevelRegion(RegionVO regionVO, List<Region> allRegion, boolean underSystemLevel) {
        // 检查是否需要查询全部树结构，false时一直查询到parentId=0的省级区域，true时查到top=true的系统级别就可以结束
        if (underSystemLevel) {
            if (regionVO.getTop()) return regionVO;
        } else {
            if (Objects.equals(regionVO.getParentId(), 0L)) return regionVO;
        }
        RegionVO parentRegion = new RegionVO(allRegion.stream().filter(it -> Objects.equals(it.getId(), regionVO.getParentId())).collect(Collectors.toList()).get(0));
        List<Region> temp = new ArrayList<>();
        temp.add(regionVO);
        parentRegion.setChildren(temp);
        recursiveFindHigherLevelRegion(parentRegion, allRegion, underSystemLevel);
        return parentRegion;
    }


    @Override
    public List<Region> findParentList(long currentRegionId, boolean underSystemLevel) {
        // 用户单位所在省市区
        RegionVO region = new RegionVO(regionRepository.findOne(currentRegionId));
        List<Region> all = regionRepository.findAll();
        // 递归查询到最顶级
        RegionVO regionTree = this.recursiveFindHigherLevelRegion(region, all, underSystemLevel);

        List<Region> list = new ArrayList<>();
        this.recursivePackageTreeToSortList(regionTree, list);
        return list;
    }


    /**
     * 把树状结构的上上级部门按顺序放入list中
     *
     * @param region 树状结构的区域
     */
    private void recursivePackageTreeToSortList(RegionVO region, List<Region> list) {
        List<Region> children = region.getChildren();
        region.setChildren(null);
        list.add(region);

        if (children == null || children.isEmpty()) return;
        this.recursivePackageTreeToSortList((RegionVO) children.get(0), list);
    }

}