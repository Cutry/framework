package com.bluesky.framework.sysParam;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysParam implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 值
     */
    private String param;
    /**
     * 标识
     */
    private String mark;
    /**
     * 说明
     */
    private String desc;
    /**
     * 说明
     */
    private String sort;
    /**
     * 说明
     */
    private String isleaf;
}
