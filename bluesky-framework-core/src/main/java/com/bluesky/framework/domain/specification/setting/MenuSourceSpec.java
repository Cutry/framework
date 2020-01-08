package com.bluesky.framework.domain.specification.setting;


/**
 * setting spec
 *
 * @author linjiang
 */
public interface MenuSourceSpec {

    /**
     * 操作项唯一性检查
     * 不存在返回true，否则返回false
     */
    boolean isOperationUnique(String operation);

    /**
     * 菜单编码唯一性检查
     * 不存在返回true，否则返回false
     */
    boolean isMenuCodeUnique(String code);

}