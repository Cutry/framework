package com.bluesky.framework.domain.infrastructure.model.article;

import com.bluesky.common.vo.Page;
import com.bluesky.core.common.PageBeanUtils;
import com.bluesky.framework.article.Article;
import com.bluesky.framework.domain.infrastructure.model.article.mapper.ArticleMapper;
import com.bluesky.framework.domain.model.article.ArticleRepository;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Repository
public class ArticleRepositoryImpl implements ArticleRepository {
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    @Transactional
    public List<Article> findCountArticle(Integer count) {
        List<Article> list = articleMapper.findCountArticle(count);
        for (Article a : list)
            DateTransform(a);
        return list;
    }

    @Override
    @Transactional
    public Article findById(Long id) {
        articleMapper.addReadCount(id);
        Article article = articleMapper.findById(id);
        DateTransform(article);
        return article;
    }

    @Override
    @Transactional
    public Page<Article> findBySearch(String title, Integer publishFlag, Integer pageNum, Integer pageSize) {
        if (pageNum == null) pageNum = 1;
        if (pageSize == null) pageSize = 15;
        PageHelper.startPage(pageNum, pageSize);
        List<Article> list = articleMapper.findBySearch(title, publishFlag);
        return PageBeanUtils.copyPageProperties(list);
    }

    @Override
    @Transactional
    public Long insertArticle(Article article) {
        articleMapper.insertArticle(article);
        return article.getId();
    }

    @Override
    @Transactional
    public int updateArticle(Article article) {
        return articleMapper.updateArticle(article);
    }

    @Override
    @Transactional
    public void save(Article article) {
        articleMapper.deleteArticle(article.getId());
        articleMapper.insertArticle(article);
    }

    @Override
    @Transactional
    public int deleteArticleLogic(Long id) {
        return articleMapper.deleteArticleLogic(id);
    }

    @Override
    @Transactional
    public int publishArticle(Long id) {
        return articleMapper.publishArticle(id);
    }

    private void DateTransform(Article article){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try{
            Date date = sdf.parse(article.getCreateTime());
            article.setCreateTime(sdf.format(date));
        }catch(ParseException e){
            e.printStackTrace();
        }
    }
}
