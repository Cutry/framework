package com.bluesky.framework.SysParam;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SysParamVO extends com.bluesky.framework.sysParam.SysParam {

    /**
     * 子级区域
     */
    private List<SysParamVO> children;


    public SysParamVO(com.bluesky.framework.sysParam.SysParam param) {
        if (param != null) {
            BeanUtils.copyProperties(param, this);
        }
    }


    public SysParamVO() {
    }
}
