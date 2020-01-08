package com.bluesky.framework.account.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 账户
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 头像
     */
    private String headImg;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 类型：1平台系统/2政务单位/3个人认证/4企业认证
     */
    private Integer roleType;
    /**
     * 用户角色id
     */
    private Long roleId;
    /**
     * 用户角色名称(不存数据库，连表查询)
     */
    private String roleName;
    /**
     * 单位id
     */
    private Long organizationId;
    /**
     * 单位名称(不存数据库，连表查询)
     */
    private String organizationName;
    /**
     * 所在部门
     */
    private String department;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 密码
     */
    private String password;
    /**
     * 上次登录ip
     */
    private String lastLoginIp;
    /**
     * 上次登录时间
     */
    private Date lastLoginTime;
    /**
     * 角色权限
     */
    private String authoritiesText;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 状态:0启用，-5禁用，-10锁定，-15删除
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
    /**
     * 所属用信单位
     */
    private Long yxdwId;
    /**
     * 所属企业
     */
    private Long companyId;
}
