package com.bluesky.framework.api.controller.article;


import com.bluesky.common.vo.DataResponse;
import com.bluesky.common.vo.Page;
import com.bluesky.framework.account.account.Account;
import com.bluesky.framework.article.Article;
import com.bluesky.framework.article.ArticleManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/article")
@RestController
public class ArticleController {
    @Autowired
    private ArticleManager articleManager;

    /**
     * 放回最新count条数据
     *
     * @param count 条数
     * @return
     */
    @GetMapping("/findByCount")
    public DataResponse findByCount(@RequestParam(value = "count", required = true) Integer count) {
        DataResponse dataResponse = new DataResponse();
        List<Article> list = articleManager.findCountArticle(count);
        if (list.size() == 0 || list == null) {
            return dataResponse.setFalseAndMessage("动态信息为空");
        }

        dataResponse.addData("list", list);
        return dataResponse;
    }

    /**
     * 查询单片文章
     *
     * @param id 文章主键
     * @return
     */
    @GetMapping("/findById")
    public DataResponse findById(@RequestParam(value = "id", required = true) Long id) {
        DataResponse dataResponse = new DataResponse();
        Article article = articleManager.findById(id);

        if (article == null) {
            return dataResponse.setFalseAndMessage("文章为空");
        }

        dataResponse.addData("article", article);
        return dataResponse;
    }

    /**
     * 搜索文章
     *
     * @param title       标题
     * @param publishFlag 发布标志位
     * @param pageNum     页码
     * @param pageSize    每页显示数量
     * @return
     */
    @GetMapping("/search")
    public DataResponse findBySearch(@RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "publishFlag", required = true) Integer publishFlag,
                                      @RequestParam(value = "pageNum", required = false) Integer pageNum,
                                      @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        DataResponse dataResponse = new DataResponse();
        //System.out.println("Title:" + title + " publishFlag:" + publishFlag + " pageNum: " + pageNum + " pageSize:" + pageSize);
        Page page = articleManager.findBySearch(title, publishFlag, pageNum, pageSize);

        if (page.getList().size() == 0 || page.getList() == null) {
            return dataResponse.setFalseAndMessage("搜索结果为空");
        }

        dataResponse.addData("page", page);
        return dataResponse;
    }

    /**
     * 插入数据
     *
     * @param article
     * @return
     */
    @PostMapping("/insert")
    @PreAuthorize("hasPermission('crm-api','sm_article_manage')")
    public DataResponse insertArticle(@AuthenticationPrincipal Account account,
                                      HttpServletRequest request,
                                      @RequestBody Article article) {
        DataResponse dataResponse = new DataResponse();
        if (articleManager.insertArticle(article)) {
            dataResponse.setMessage("添加成功");
        } else {
            dataResponse.setFalseAndMessage("添加失败,请稍后重试");
        }

        return dataResponse;
    }

    /**
     * 保存草稿
     *
     * @param article
     * @return
     */
    @PostMapping("/save")
    @PreAuthorize("hasPermission('crm-api','sm_article_manage')")
    public DataResponse saveArticle(@AuthenticationPrincipal Account account,
                                    HttpServletRequest request,
                                    @RequestBody Article article) {
        DataResponse dataResponse = new DataResponse();
        article.setAccountId(account.getId());
        articleManager.save(article);
        dataResponse.setMessage("保存成功");

        return dataResponse;
    }

    /**
     * 更新数据
     *
     * @param article
     * @return
     */
    @PostMapping("/update")
    @PreAuthorize("hasPermission('crm-api','sm_article_manage')")
    public DataResponse updateArticle(@AuthenticationPrincipal Account account,
                                      HttpServletRequest request,
                                      @RequestBody Article article) {
        DataResponse dataResponse = new DataResponse();
        if (articleManager.updateArticle(article) == 1) {
            dataResponse.setMessage("更新成功");
        } else {
            dataResponse.setFalseAndMessage("更新失败,请稍后重试");
        }

        return dataResponse;
    }

    /**
     * 删除文章
     *
     * @param id 文章主键
     * @return
     */
    @GetMapping("/delete")
    @PreAuthorize("hasPermission('crm-api','sm_article_manage')")
    public DataResponse deleteArticleLogic(@AuthenticationPrincipal Account account,
                                           HttpServletRequest request,
                                           @RequestParam(value = "id", required = true) Long id) {

        DataResponse dataResponse = new DataResponse();
        if (articleManager.deleteArticleLogic(id) == 1) {
            dataResponse.setMessage("删除成功");
        } else {
            dataResponse.setFalseAndMessage("删除失败,请稍后重试");
        }

        return dataResponse;
    }

    /**
     * 反转文章发布状态
     *
     * @param id 文章主键
     * @return
     */
    @GetMapping("/publish")
    @PreAuthorize("hasPermission('crm-api','sm_article_manage')")
    public DataResponse publishArticle(@AuthenticationPrincipal Account account,
                                       HttpServletRequest request,
                                       @RequestParam(value = "id", required = true) Long id) {

        DataResponse dataResponse = new DataResponse();
        if (articleManager.publishArticle(id) == 1) {
            dataResponse.setMessage("操作成功");
        } else {
            dataResponse.setFalseAndMessage("操作失败,请稍后重试");
        }

        return dataResponse;
    }

}
