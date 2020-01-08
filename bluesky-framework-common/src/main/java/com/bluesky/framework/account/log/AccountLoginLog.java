package com.bluesky.framework.account.log;

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
public class AccountLoginLog implements Serializable {
    private Long id;

    /**
     * 账号id
     */
    private Long accountId;
    /**
     * 账号名称
     */
    private String accountName;
    /**
     * 单位id
     */
    private Long organizationId;
    /**
     * 单位名称
     */
    private String organizationName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * ip地址
     */
    private String ip;
    /**
     * ip地址所在区域
     */
    private String ipArea;
    /**
     * 1登录/2注销
     */
    private Integer type;
    /**
     * 1成功/-1失败
     */
    private Integer success;
    /**
     * 说明
     */
    private String memo;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 修改时间
     */
    private Date updatedAt;
}