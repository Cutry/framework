package com.bluesky.framework.setting.menu;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户个性化
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomIndex implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 账户ID
     */
    private Long accountId;
    /**
     * 自定义的首页菜单应用，逗号分隔，包含顺序
     */
    private String menuIds;
    /**
     * 创建时间
     */
    private Date createdAt;
}