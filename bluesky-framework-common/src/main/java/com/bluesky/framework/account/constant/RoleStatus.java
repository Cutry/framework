package com.bluesky.framework.account.constant;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 角色状态
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum RoleStatus {
    /**
     * 正常
     */
    NORMAl(0, "正常"),

    /**
     * 删除
     */
    DELETE(-5, "删除");

    private int code;
    private String value;


    public static String getName(int code) {
        for (RoleStatus c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                return c.value;
            }
        }
        return null;
    }


    public static boolean isCodeLegal(int code) {
        boolean find = false;
        for (RoleStatus c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                find = true;
                break;
            }
        }
        return find;
    }
}