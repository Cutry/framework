package com.bluesky.framework.setting.menu;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 子菜单操作项
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuSourceOperation implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 菜单
     */
    private Long menuId;
    /**
     * 父菜单ids
     */
    private String parentMenuIds;
    /**
     * 操作
     */
    private String operation;
    /**
     * 操作名称
     */
    private String operationName;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
}