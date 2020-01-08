package com.bluesky.framework.domain.infrastructure.model.article.mapper;

import com.bluesky.framework.article.Article;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.StatementType;
import org.springframework.security.access.method.P;

import java.util.List;

public interface ArticleMapper {

    /**
     * 查询最新的count条信息
     * @param count 条数
     * @return
     */
    @ResultMap("ArticleMap")
    @Select("select id,title,author,create_time,update_time,read_count,account_id,cover_img " +
            " from tbl_article where delete_flag = 0 and publish_flag = 1 order by create_time desc limit 0,#{count}")
    List<Article> findCountArticle(@Param("count") Integer count);

    /**
     * 查找单片作品
     * @param id 作品主键
     * @return
     */
    @ResultMap("ArticleMap")
    @Select("select id,title,author,create_time,update_time,content,read_count,account_id,cover_img " +
            " from tbl_article where id=#{id} and delete_flag = 0")
    Article findById(@Param("id") Long id);

    /**
     * 通过标题搜索文章（查询所有信息）
     * @param title 标题
     * @return
     */
    List<Article> findBySearch(@Param("title") String title, @Param("publishFlag") Integer publishFlag);

    /**
     * 增加阅读量
     * @param id
     */
    @Update("update tbl_article set read_count=read_count+1 where id=#{id}")
    void addReadCount(@Param("id") Long id);

    /**
     * 插入新的文章
     * @param article 文章实体
     * @return
     */
    @Insert("insert into tbl_article(title,author,create_time,content,read_count,account_id,delete_flag,publish_flag,cover_img) " +
            " values (#{article.title},#{article.author},now(),#{article.content},1,#{article.accountId},0,#{article.publishFlag},#{article.coverImg})")
    @SelectKey(before = false, keyProperty = "article.id", resultType = Long.class, statementType = StatementType.STATEMENT, statement = "SELECT LAST_INSERT_ID() AS id")
    Long insertArticle(@Param("article") Article article);

    /**
     * 更新文章
     * @param article
     * @return
     */
    @Update("update tbl_article set title=#{article.title},author=#{article.author},content=#{article.content},update_time=now(),publish_flag=#{article.publishFlag} where id=#{article.id}")
    int updateArticle(@Param("article") Article article);

    /**
     * 删除文章（物理）
     * @param id 文章主键
     * @return
     */
    @Delete("delete from tbl_article where id=#{id}")
    int deleteArticle(@Param("id") Long id);

    /**
     * 删除文章（逻辑）
     * @param id 文章主键
     * @return
     */
    @Update("update tbl_article set delete_flag = 1 where id=#{id}")
    int deleteArticleLogic(@Param("id") Long id);

    /**
     * 反转文章发布状态
     * @param id 文章主键
     * @return
     */
    @Update("update tbl_article set publish_flag = !publish_flag where id=#{id}")
    int publishArticle(@Param("id") Long id);


}
