package com.sta.module.blog.controller.admin.article;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.article.vo.BlogArticlePageReqVO;
import com.sta.module.blog.controller.admin.article.vo.BlogArticleRespVO;
import com.sta.module.blog.controller.admin.article.vo.BlogArticleSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogArticleDO;
import com.sta.module.blog.service.BlogArticleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客文章")
@RestController
@RequestMapping("/blog/article")
@Validated
public class BlogArticleController {

    @Resource
    private BlogArticleService articleService;

    @PostMapping("/create")
    @Operation(summary = "创建文章")
    @PreAuthorize("@ss.hasPermission('blog:article:create')")
    public CommonResult<Long> createArticle(@Valid @RequestBody BlogArticleSaveReqVO createReqVO) {
        return success(articleService.createArticle(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文章")
    @PreAuthorize("@ss.hasPermission('blog:article:update')")
    public CommonResult<Boolean> updateArticle(@Valid @RequestBody BlogArticleSaveReqVO updateReqVO) {
        articleService.updateArticle(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文章")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:article:delete')")
    public CommonResult<Boolean> deleteArticle(@RequestParam("id") Long id) {
        articleService.deleteArticle(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文章")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<BlogArticleRespVO> getArticle(@RequestParam("id") Long id) {
        BlogArticleDO article = articleService.getArticle(id);
        return success(BeanUtils.toBean(article, BlogArticleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文章分页")
    @PreAuthorize("@ss.hasPermission('blog:article:query')")
    public CommonResult<PageResult<BlogArticleRespVO>> getArticlePage(@Valid BlogArticlePageReqVO pageReqVO) {
        PageResult<BlogArticleDO> pageResult = articleService.getArticlePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BlogArticleRespVO.class));
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新文章状态")
    @PreAuthorize("@ss.hasPermission('blog:article:update')")
    public CommonResult<Boolean> updateArticleStatus(@RequestParam("id") Long id,
                                                      @RequestParam("status") Integer status) {
        articleService.updateArticleStatus(id, status);
        return success(true);
    }

}
