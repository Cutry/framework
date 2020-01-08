package com.bluesky.framework.setting.page;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * 页面设置
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageSetting implements Serializable {

    /**
     * 主键
     */
    private Long id;
    /**
     * 页面
     */
    private String page;
    /**
     * 页面内的类型
     */
    private String type;
    /**
     * 配置的值
     */
    private String value;
    /**
     * 操作人ID
     */
    private Long operatorId;
    /**
     * 创建时间
     */
    private Date createdAt;
    /**
     * 更新时间
     */
    private Date updatedAt;


    /**
     * 类型
     */
    @AllArgsConstructor
    @Getter
    public enum Page {
        /**
         * 默认配置
         */
        DEFAULT("default", "默认配置"),
        /**
         * 登录页
         */
        LOGIN("login", "登录页"),

        /**
         * 首页
         */
        INDEX("index", "首页"),

        /**
         * 工作页
         */
        WORK("work", "工作页"),

        /**
         * 版权信息
         */
        COPY_RIGHT("copyRight", "版权信息");

        private String code;
        private String value;


    }


    /**
     * 页面中类型
     */
    @AllArgsConstructor
    @Getter
    public enum Type {
        /**
         * 默认上传路径，设置为web根路径
         */
        UPLOAD_DIR("upload_dir", "默认上传路径"),
        /**
         * 顶部logo图
         */
        LOGO("logo", "logo"),

        /**
         * 背景图
         */
        BACKGROUND("background", "背景图"),

        /**
         * 背景颜色
         */
        BACKGROUND_COLOR("background_color", "背景颜色"),

        /**
         * 登录方式
         */
        LOGIN_TYPE("login_type", "登录方式"),

        /**
         * 查询背景图
         */
        QUERY_BACKGROUND("query_background", "查询背景图"),

        /**
         * 查询背景颜色
         */
        QUERY_BACKGROUND_COLOR("query_background_color", "查询背景颜色"),

        /**
         * 主题颜色
         */
        THEME_COLOR("theme_color", "查询背景图"),

        /**
         * 技术支持
         */
        SUPPORT("support", "技术支持"),

        /**
         * 主办单位
         */
        ORGANIZER("organizer", "主办单位");

        private String code;
        private String value;
    }
}