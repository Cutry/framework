package com.bluesky.framework.account.constant;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账号登录结果
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountLoginResult {

    SUCCESS(1, "成功"),

    FAIL(-1, "失败");


    private Integer code;
    private String value;
}