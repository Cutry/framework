package com.bluesky.framework.account.constant;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 行政级别
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum AdministrativeLevel {
    /**
     * 无
     */
    NONE(-1, "无"),
    /**
     * 省级
     */
    PROVINCIAL_LEVEL(1, "省级"),
    /**
     * 市级
     */
    MUNICIPAL_LEVEL(2, "市级"),
    /**
     * 区县级
     */
    DISTRICT_LEVEL(3, "区县级");


    private Integer code;
    private String value;


    public static String getName(int code) {
        for (AdministrativeLevel c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                return c.value;
            }
        }
        return null;
    }


    public static boolean isCodeLegal(int code) {
        boolean find = false;
        for (AdministrativeLevel c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                find = true;
                break;
            }
        }
        return find;
    }

}