package com.sta.module.blog.service.content;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.content.vo.ArticlePageReqVO;
import com.sta.module.blog.controller.admin.content.vo.ArticleSaveReqVO;
import com.sta.module.blog.controller.app.content.vo.AppArticlePageReqVO;
import com.sta.module.blog.controller.app.content.vo.AppArticleRespVO;
import com.sta.module.blog.dal.dataobject.content.ArticleDO;

import java.util.List;

/**
 * 博客文章 Service 接口
 */
public interface ArticleService {

    Long createArticle(ArticleSaveReqVO createReqVO);

    void updateArticle(ArticleSaveReqVO updateReqVO);

    void deleteArticle(Long id);

    ArticleDO getArticle(Long id);

    PageResult<ArticleDO> getArticlePage(ArticlePageReqVO pageReqVO);

    List<ArticleDO> getArticleListByStatus(Integer status);

    void updateArticleStatus(Long id, Integer status);

    /**
     * App - 文章访问量+1
     */
    void addVisitCount(Long id);

    /**
     * App - 前端文章分页（仅公开状态，admin VO）
     */
    PageResult<ArticleDO> getArticlePageAdmin(ArticlePageReqVO pageReqVO);

    /**
     * App - 前端文章分页（仅公开状态，app VO）
     */
    PageResult<ArticleDO> getArticlePageApp(AppArticlePageReqVO pageReqVO);

    // ========== App 专用方法 ==========

    /**
     * App - 获取时间轴文章列表（全量）
     */
    List<ArticleDO> getArticleListByCreateTime();

    /**
     * App - 获取时间轴文章分页（每页 10 条）
     */
    PageResult<ArticleDO> getArticlePageByCreateTime(AppArticlePageReqVO pageReqVO);

    /**
     * App - 按分类获取文章列表
     */
    List<ArticleDO> getArticleListByCategory(Long categoryId);

    /**
     * App - 按标签获取文章列表
     */
    List<ArticleDO> getArticleListByTag(Long tagId);

    /**
     * App - 获取推荐文章
     */
    List<ArticleDO> getArticleListByVisitCount();

    /**
     * App - 获取相关文章（同分类）
     */
    List<ArticleDO> getArticleListByCategoryId(Long categoryId, Long articleId);

    /**
     * App - 搜索文章（按标题/内容）
     */
    List<ArticleDO> getArticleListByTitleAndContent(String keyword);

    // ========== App VO 装配 ==========

    /**
     * 将单篇 ArticleDO 转换为装配完整的 AppArticleRespVO
     */
    AppArticleRespVO convertToAppVO(ArticleDO article);

    /**
     * 将 ArticleDO 列表批量转换为装配完整的 AppArticleRespVO 列表
     */
    List<AppArticleRespVO> convertToAppList(List<ArticleDO> list);

    /**
     * 将 ArticleDO 分页结果批量转换为装配完整的 AppArticleRespVO 分页结果
     */
    PageResult<AppArticleRespVO> convertToAppPage(PageResult<ArticleDO> pageResult);

}
