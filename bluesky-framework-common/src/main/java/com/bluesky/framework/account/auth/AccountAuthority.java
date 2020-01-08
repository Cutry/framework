package com.bluesky.framework.account.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 账户权限
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountAuthority implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 角色ID
     */
    private Long accountId;
    /**
     * 操作权限ID
     */
    private Long operationId;
    /**
     * 操作权限
     */
    private String operation;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
}