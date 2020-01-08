package com.bluesky.framework.account.constant;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 角色类型
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoleType {

    PLATFORM(1, "平台用户"),

    GOVERNMENT(2, "政务单位"),

    PERSONAL_AUTH(3, "个人认证"),

    COMPANY_AUTH(4, "企业认证");

    private int code;
    private String value;


    public static String getName(int code) {
        for (RoleType c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                return c.value;
            }
        }
        return null;
    }


    public static boolean isCodeLegal(int code) {
        boolean find = false;
        for (RoleType c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                find = true;
                break;
            }
        }
        return find;
    }
}