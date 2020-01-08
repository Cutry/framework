package com.bluesky.framework.account.region;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RegionVO extends Region {
    /**
     * 子级区域
     */
    private List<Region> children;


    public RegionVO(Region region) {
        if (region != null) {
            BeanUtils.copyProperties(region, this);
        }
    }


    public RegionVO() {
    }
}