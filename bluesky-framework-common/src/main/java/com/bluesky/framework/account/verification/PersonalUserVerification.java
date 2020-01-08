package com.bluesky.framework.account.verification;

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
public class PersonalUserVerification implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 账号id,核验通过生成账号id
     */
    private Long accountId;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 名族
     */
    private String nationality;
    /**
     * 联系地址
     */
    private String connectAddress;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 身份证照片
     */
    private String idCardImg;

    /**
     * 手持身份证照片
     */
    private Integer idCardHandImg;
    /**
     * 核验时间
     */
    private Integer verificationAt;
    /**
     * 状态:0待核验，-5核验失败，-5核验通过
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