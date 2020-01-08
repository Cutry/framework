package com.bluesky.framework.article;

import com.bluesky.common.vo.Page;

import java.util.List;

public interface ArticleManager {
    /**
     * 查询最新的count条信息
     *
     * @param count 条数
     * @return
     */
    List<Article> findCountArticle(Integer count);

    /**
     * 查找单片文章
     *
     * @param id 文章主键
     * @return
     */
    Article findById(Long id);

    /**
     * 通过标题搜索文章（查询所有信息）
     *
     * @param title 标题
     * @return
     */
    Page<Article> findBySearch(String title, Integer publishFlag, Integer pageNum, Integer pageSize);

    /**
     * 插入新的文章
     *
     * @param article 文章实体
     * @return
     */
    boolean insertArticle(Article article);

    /**
     * 更新文章
     *
     * @param article
     * @return
     */
    int updateArticle(Article article);

    /**
     * 保存草稿
     *
     * @param article 草稿文章
     * @return
     */
    void save(Article article);

    /**
     * 删除文章
     *
     * @param id 文章主键
     * @return
     */
    int deleteArticleLogic(Long id);

    /**
     * 反转文章发布状态
     * @param id 文章主键
     * @return
     */
    int publishArticle(Long id);
}
