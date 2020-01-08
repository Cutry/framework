package com.bluesky.framework.account.role;

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
public class Role implements Serializable {
    private Long id;
    /**
     * 类型：1平台系统/2政务单位/3个人认证/4企业认证
     */
    private Integer type;
    /**
     * 级别
     *
     * @see com.bluesky.framework.account.constant.AdministrativeLevel
     */
    private Integer level;
    /**
     * 名称
     */
    private String name;
    /**
     * 状态:0正常，-5删除
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