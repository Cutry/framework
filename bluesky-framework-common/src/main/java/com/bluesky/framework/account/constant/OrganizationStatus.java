package com.bluesky.framework.account.constant;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 组织单位状态
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OrganizationStatus {
    /**
     * 启用
     */
    ENABLE(0, "启用"),
    /**
     * 禁用
     */
    DISABLE(-5, "禁用"),
    /**
     * 删除
     */
    DELETE(-10, "删除");

    private int code;
    private String value;


    public static String getName(int code) {
        for (OrganizationStatus c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                return c.value;
            }
        }
        return null;
    }


    public static boolean isCodeLegal(int code) {
        boolean find = false;
        for (OrganizationStatus c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                find = true;
                break;
            }
        }
        return find;
    }
}