package com.bluesky.framework.server.article;

import com.alibaba.dubbo.config.annotation.DubboService;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.article.Article;
import com.bluesky.framework.article.ArticleManager;
import com.bluesky.framework.domain.model.article.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DubboService
public class ArticleManagerImpl implements ArticleManager {
    @Autowired
    ArticleRepository articleRepository;

    @Override
    public List<Article> findCountArticle(Integer count) {
        return articleRepository.findCountArticle(count);
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id);
    }

    @Override
    public Page<Article> findBySearch(String title, Integer publishFlag, Integer pageNum, Integer pageSize) {
        return articleRepository.findBySearch(title, publishFlag, pageNum, pageSize);
    }

    @Override
    public boolean insertArticle(Article article) {
        articleRepository.insertArticle(article);
        return article.getId() > 0 ? true : false;
    }

    @Override
    public int updateArticle(Article article) {
        return articleRepository.updateArticle(article);
    }

    @Override
    public void save(Article article) {
        articleRepository.save(article);
    }

    @Override
    public int deleteArticleLogic(Long id) {
        return articleRepository.deleteArticleLogic(id);
    }

    @Override
    public int publishArticle(Long id) {
        return articleRepository.publishArticle(id);
    }
}
