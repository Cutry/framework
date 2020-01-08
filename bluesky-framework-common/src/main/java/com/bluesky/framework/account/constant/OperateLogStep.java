package com.bluesky.framework.account.constant;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * 账户操作日志步骤
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OperateLogStep {

    REGION_ADD("region_add", "区域添加"),
    REGION_UPDATE("region_update", "区域修改"),
    REGION_UPDATE_SYSTEM_LEVEL("region_update_system_level", "区域修改系统级别"),
    REGION_DELETE("region_delete", "区域删除"),

    ROLE_ADD("user_add", "角色添加"),
    ROLE_UPDATE("role_update", "角色修改"),
    ROLE_DELETE("role_delete", "角色删除"),

    ORGANIZATION_ADD("organization_add", "单位新增"),
    ORGANIZATION_UPDATE("organization_update", "单位修改"),
    ORGANIZATION_DELETE("organization_delete", "单位删除"),
    ORGANIZATION_ENABLE("organization_enable", "单位启用"),
    ORGANIZATION_DISABLE("organization_disable", "单位停用"),

    USER_ADD("user_add", "用户添加"),
    USER_UPDATE("user_update", "用户修改"),
    USER_ENABLE("user_enable", "用户启用"),
    USER_DISABLE("user_disable", "用户禁用"),
    USER_DELETE("user_delete", "用户删除"),
    USER_RESET_PASSWORD("user_reset_password", "用户重置密码");

    private String code;
    private String value;


    public static String getName(String code) {
        for (OperateLogStep c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                return c.value;
            }
        }
        return null;
    }


    public static boolean isCodeLegal(String code) {
        boolean find = false;
        for (OperateLogStep c : values()) {
            if (Objects.equals(c.getCode(), code)) {
                find = true;
                break;
            }
        }
        return find;
    }
}