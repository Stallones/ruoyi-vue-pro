package com.sta.module.blog.controller.app.article;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.app.article.vo.AppArticlePageReqVO;
import com.sta.module.blog.controller.app.article.vo.AppArticleRespVO;
import com.sta.module.blog.dal.dataobject.BlogArticleDO;
import com.sta.module.blog.service.BlogArticleService;
import com.sta.module.blog.enums.ArchiveTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客文章")
@RestController
@RequestMapping("/blog/article")
@Validated
@PermitAll
public class AppArticleController {

    @Resource
    private BlogArticleService articleService;

    @GetMapping("/get")
    @Operation(summary = "获得文章详情")
    @Parameter(name = "id", description = "文章编号", required = true, example = "1")
    public CommonResult<AppArticleRespVO> getArticle(@RequestParam("id") Long id) {
        BlogArticleDO article = articleService.getArticle(id);
        return success(articleService.convertToAppVO(article));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文章分页（仅公开文章）")
    public CommonResult<PageResult<AppArticleRespVO>> getArticlePage(@Valid AppArticlePageReqVO pageReqVO) {
        PageResult<BlogArticleDO> pageResult = articleService.getFrontPage(pageReqVO);
        return success(articleService.convertToAppPage(pageResult));
    }

    @GetMapping("/time-line")
    @Operation(summary = "获得时间轴文章列表")
    public CommonResult<List<AppArticleRespVO>> getTimelineList() {
        List<BlogArticleDO> list = articleService.getTimelineList();
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/archive")
    @Operation(summary = "获得分类/标签下的文章列表")
    @Parameters({
            @Parameter(name = "archiveType", description = "归档类型（category 分类 / tag 标签）", required = true, example = "category"),
            @Parameter(name = "archiveId", description = "分类或标签ID", required = true)
    })
    public CommonResult<List<AppArticleRespVO>> getArchiveArticleList(
            @RequestParam("archiveType") String archiveType,
            @RequestParam("archiveId") Long archiveId) {
        List<BlogArticleDO> list = articleService.getArchiveArticleList(archiveType, archiveId);
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/recommend")
    @Operation(summary = "获得推荐文章")
    public CommonResult<List<AppArticleRespVO>> getRecommendArticleList() {
        List<BlogArticleDO> list = articleService.getRecommendArticleList();
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/random")
    @Operation(summary = "获得随机文章")
    @Parameter(name = "limit", description = "数量", example = "5")
    public CommonResult<List<AppArticleRespVO>> getRandomArticleList(
            @RequestParam(value = "limit", defaultValue = "5") Integer limit) {
        List<BlogArticleDO> list = articleService.getRandomArticleList(limit);
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/related")
    @Operation(summary = "获得相关文章（同分类）")
    @Parameters({
            @Parameter(name = "categoryId", description = "分类ID", required = true),
            @Parameter(name = "articleId", description = "当前文章ID（排除自身）", required = true)
    })
    public CommonResult<List<AppArticleRespVO>> getRelatedArticleList(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("articleId") Long articleId) {
        List<BlogArticleDO> list = articleService.getRelatedArticleList(categoryId, articleId);
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/search-title")
    @Operation(summary = "获得搜索标题列表（初始化搜索）")
    public CommonResult<List<AppArticleRespVO>> getSearchTitleList() {
        List<BlogArticleDO> list = articleService.getSearchTitleList();
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/visit")
    @Operation(summary = "文章访问量+1")
    @Parameter(name = "id", description = "文章ID", required = true)
    public CommonResult<Boolean> addVisitCount(@RequestParam("id") Long id) {
        articleService.addVisitCount(id);
        return success(true);
    }

    @GetMapping("/search-content")
    @Operation(summary = "搜索文章（按标题/内容）")
    @Parameter(name = "keyword", description = "搜索关键词", required = true)
    public CommonResult<List<AppArticleRespVO>> searchArticleByContent(
            @NotEmpty(message = "搜索关键词不能为空")
            @Length(min = 1, max = 15, message = "搜索关键词长度应在1-15之间")
            @RequestParam("keyword") String keyword) {
        List<BlogArticleDO> list = articleService.searchArticleByContent(keyword);
        return success(articleService.convertToAppList(list));
    }

}
