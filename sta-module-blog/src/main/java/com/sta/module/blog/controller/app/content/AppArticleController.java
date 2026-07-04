package com.sta.module.blog.controller.app.content;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.app.content.vo.AppArticlePageReqVO;
import com.sta.module.blog.controller.app.content.vo.AppArticleRespVO;
import com.sta.module.blog.dal.dataobject.content.ArticleDO;
import com.sta.module.blog.service.content.ArticleService;
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
    private ArticleService articleService;

    @GetMapping("/get")
    @Operation(summary = "获得文章详情")
    @Parameter(name = "id", description = "文章编号", required = true, example = "1")
    public CommonResult<AppArticleRespVO> getArticle(@RequestParam("id") Long id) {
        ArticleDO article = articleService.getArticle(id);
        return success(articleService.convertToAppVO(article));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文章分页（仅公开文章）")
    public CommonResult<PageResult<AppArticleRespVO>> getArticlePage(@Valid AppArticlePageReqVO pageReqVO) {
        PageResult<ArticleDO> pageResult = articleService.getArticlePageApp(pageReqVO);
        return success(articleService.convertToAppPage(pageResult));
    }

    @GetMapping("/time-line")
    @Operation(summary = "获得时间轴文章列表（全量）")
    public CommonResult<List<AppArticleRespVO>> getArticleListByCreateTime() {
        List<ArticleDO> list = articleService.getArticleListByCreateTime();
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/time-line/page")
    @Operation(summary = "获得时间轴文章分页")
    public CommonResult<PageResult<AppArticleRespVO>> getArticlePageByCreateTime(@Valid AppArticlePageReqVO pageReqVO) {
        PageResult<ArticleDO> pageResult = articleService.getArticlePageByCreateTime(pageReqVO);
        return success(articleService.convertToAppPage(pageResult));
    }

    @PermitAll
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "获得分类下的文章列表")
    @Parameter(name = "categoryId", description = "分类ID", required = true, example = "1")
    public CommonResult<List<AppArticleRespVO>> getArticleListByCategory(
            @PathVariable("categoryId") Long categoryId) {
        List<ArticleDO> list = articleService.getArticleListByCategory(categoryId);
        return success(articleService.convertToAppList(list));
    }

    @PermitAll
    @GetMapping("/tag/{tagId}")
    @Operation(summary = "获得标签下的文章列表")
    @Parameter(name = "tagId", description = "标签ID", required = true, example = "1")
    public CommonResult<List<AppArticleRespVO>> getArticleListByTag(
            @PathVariable("tagId") Long tagId) {
        List<ArticleDO> list = articleService.getArticleListByTag(tagId);
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/recommend")
    @Operation(summary = "获得推荐文章")
    public CommonResult<List<AppArticleRespVO>> getArticleListByVisitCount() {
        List<ArticleDO> list = articleService.getArticleListByVisitCount();
        return success(articleService.convertToAppList(list));
    }

    @GetMapping("/related")
    @Operation(summary = "获得相关文章（同分类）")
    @Parameters({
            @Parameter(name = "categoryId", description = "分类ID", required = true),
            @Parameter(name = "articleId", description = "当前文章ID（排除自身）", required = true)
    })
    public CommonResult<List<AppArticleRespVO>> getArticleListByCategoryId(
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("articleId") Long articleId) {
        List<ArticleDO> list = articleService.getArticleListByCategoryId(categoryId, articleId);
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
    public CommonResult<List<AppArticleRespVO>> getArticleListByTitleAndContent(
            @NotEmpty(message = "搜索关键词不能为空")
            @Length(min = 1, max = 15, message = "搜索关键词长度应在1-15之间")
            @RequestParam("keyword") String keyword) {
        List<ArticleDO> list = articleService.getArticleListByTitleAndContent(keyword);
        return success(articleService.convertToAppList(list));
    }

}
