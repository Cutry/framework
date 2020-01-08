package com.bluesky.framework.account.organization;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


/**
 * 组织单位
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Organization implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 部门编码
     */
    private String code;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 全称
     */
    private String fullName;
    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;
    /**
     * 上级单位id
     */
    private Long parentId;
    /**
     * 地区编码
     */
    private String areaCode;
    /**
     * 省市区id
     */
    private Long regionId;
    /**
     * 状态:0启用，-5禁用,-10 删除
     */
    private Integer status;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 备注信息
     */
    private String memo;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;
    /**
     * 上级单位名称
     */
    private String parentName;
}
