package com.bluesky.framework.account.region;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Region implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 级别：-1无/1省级/2市级/3区县级
     */
    private Integer level;
    /**
     * 行政区划代码
     */
    private String code;
    /**
     * 父级id
     */
    private Long parentId;
    /**
     * 父级编码
     */
    private String parentCode;
    /**
     * 顶级节点：0非最顶级节点，1是最顶级节点
     */
    private Boolean top;
    /**
     * 末节点:0非末节点，1末节点
     */
    private Boolean leaf;
    /**
     * 状态：0正常，-5删除
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
}