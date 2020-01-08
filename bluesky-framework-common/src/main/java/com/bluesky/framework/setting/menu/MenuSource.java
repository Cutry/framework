package com.bluesky.framework.setting.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 菜单、子菜单
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MenuSource implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 菜单编码，唯一
     */
    private String code;
    /**
     * 菜单名称
     */
    private String name;
    /**
     * 链接
     */
    private String url;
    /**
     * 图标
     */
    private String icon;
    /**
     * 图标颜色
     */
    private String iconColor;
    /**
     * 首页展示背景颜色
     */
    private String backgroundColor;
    /**
     * 首页应用名称颜色
     */
    private String color;
    /**
     * 父ID
     */
    private Long parentId;
    /**
     * 顺序
     */
    private Integer seq;
    /**
     * 是否系统菜单
     */
    private Boolean systemMenu;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 子菜单
     */
    private List<MenuSource> childMenuSourceList;

    /**
     * 操作项
     */
    private List<MenuSourceOperation> operationList;
}