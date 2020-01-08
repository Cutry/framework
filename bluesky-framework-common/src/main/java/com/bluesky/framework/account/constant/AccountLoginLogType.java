package com.bluesky.framework.account.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 账号登录日志类型
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountLoginLogType {

    LOGIN(1, "登录"),

    LOGOUT(2, "注销");

    private Integer code;
    private String value;

}