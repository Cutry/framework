package com.bluesky.framework.article;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/*
 * @Description TODO 动态信息实体
 * @Author Raindrop
 * @Date 2019/8/2
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Article implements Serializable {
    /**
     * 主键
     */
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     * 作者
     */
    private String author;

    /**
     * 封面图片
     */
    private String coverImg;
    /**
     * 发布日期
     */
    private String createTime;
    /**
     * 编辑日期
     */
    private String updateTime;
    /**
     * 内容
     */
    private String content;
    /**
     * 删除标志位
     */
    private boolean deleteFlag;
    /**
     * 发布标志位
     */
    private boolean publishFlag;
    /**
     * 管理员主键
     */
    private Long accountId;
    /**
     * 阅读数
     */
    private Long readCount;
}
