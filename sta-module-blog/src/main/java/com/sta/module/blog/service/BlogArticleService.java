package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.article.vo.BlogArticlePageReqVO;
import com.sta.module.blog.controller.admin.article.vo.BlogArticleSaveReqVO;
import com.sta.module.blog.controller.app.article.vo.AppArticlePageReqVO;
import com.sta.module.blog.controller.app.article.vo.AppArticleRespVO;
import com.sta.module.blog.dal.dataobject.BlogArticleDO;

import java.util.List;

/**
 * 博客文章 Service 接口
 */
public interface BlogArticleService {

    Long createArticle(BlogArticleSaveReqVO createReqVO);

    void updateArticle(BlogArticleSaveReqVO updateReqVO);

    void deleteArticle(Long id);

    BlogArticleDO getArticle(Long id);

    PageResult<BlogArticleDO> getArticlePage(BlogArticlePageReqVO pageReqVO);

    List<BlogArticleDO> getArticleListByStatus(Integer status);

    void updateArticleStatus(Long id, Integer status);

    /**
     * App - 文章访问量+1
     */
    void addVisitCount(Long id);

    /**
     * App - 前端文章分页（仅公开状态，admin VO）
     */
    PageResult<BlogArticleDO> getFrontPage(BlogArticlePageReqVO pageReqVO);

    /**
     * App - 前端文章分页（仅公开状态，app VO）
     */
    PageResult<BlogArticleDO> getFrontPage(AppArticlePageReqVO pageReqVO);

    // ========== App 专用方法 ==========

    /**
     * App - 获取时间轴文章列表
     */
    List<BlogArticleDO> getTimelineList();

    /**
     * App - 按分类/标签获取文章列表
     */
    List<BlogArticleDO> getArchiveArticleList(String archiveType, Long archiveId);

    /**
     * App - 获取推荐文章
     */
    List<BlogArticleDO> getRecommendArticleList();

    /**
     * App - 获取随机文章
     */
    List<BlogArticleDO> getRandomArticleList(Integer limit);

    /**
     * App - 获取相关文章（同分类）
     */
    List<BlogArticleDO> getRelatedArticleList(Long categoryId, Long articleId);

    /**
     * App - 获取搜索标题列表（初始化搜索）
     */
    List<BlogArticleDO> getSearchTitleList();

    /**
     * App - 搜索文章（按标题/内容）
     */
    List<BlogArticleDO> searchArticleByContent(String keyword);

    // ========== App VO 装配 ==========

    /**
     * 将单篇 BlogArticleDO 转换为装配完整的 AppArticleRespVO
     */
    AppArticleRespVO convertToAppVO(BlogArticleDO article);

    /**
     * 将 BlogArticleDO 列表批量转换为装配完整的 AppArticleRespVO 列表
     */
    List<AppArticleRespVO> convertToAppList(List<BlogArticleDO> list);

    /**
     * 将 BlogArticleDO 分页结果批量转换为装配完整的 AppArticleRespVO 分页结果
     */
    PageResult<AppArticleRespVO> convertToAppPage(PageResult<BlogArticleDO> pageResult);

}
