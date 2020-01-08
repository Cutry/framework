package com.bluesky.framework.account.constant;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 账户状态
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AccountStatus {
    /**
     * 正常
     */
    NORMAl(0, "启用"),
    /**
     * 禁用
     */
    DISABLE(-5, "禁用"),
    /**
     * 锁定
     */
    LOCKED(-10, "锁定"),
    /**
     * 删除
     */
    DELETE(-15, "删除");

    private Integer code;
    private String value;


    public static String getName(int code) {
        for (AccountStatus c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                return c.value;
            }
        }
        return null;
    }


    public static boolean isCodeLegal(int code) {
        boolean find = false;
        for (AccountStatus c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                find = true;
                break;
            }
        }
        return find;
    }
}