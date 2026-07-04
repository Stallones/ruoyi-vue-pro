package com.sta.module.blog.controller.app.content;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.app.content.vo.AppCategoryRespVO;
import com.sta.module.blog.dal.dataobject.content.ArticleDO;
import com.sta.module.blog.dal.dataobject.content.CategoryDO;
import com.sta.module.blog.dal.mysql.content.ArticleMapper;
import com.sta.module.blog.service.content.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客分类")
@RestController
@RequestMapping("/blog/category")
@Validated
@PermitAll
public class AppCategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleMapper articleMapper;

    @GetMapping("/get")
    @Operation(summary = "获得分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    public CommonResult<AppCategoryRespVO> getCategory(@RequestParam("id") Long id) {
        CategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, AppCategoryRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得分类列表")
    public CommonResult<List<AppCategoryRespVO>> getCategoryList() {
        List<CategoryDO> list = categoryService.getCategoryList();
        List<AppCategoryRespVO> result = BeanUtils.toBean(list, AppCategoryRespVO.class);
        for (AppCategoryRespVO vo : result) {
            Long count = articleMapper.selectCount(
                    new LambdaQueryWrapperX<ArticleDO>()
                            .eq(ArticleDO::getCategoryId, vo.getId())
                            .eq(ArticleDO::getStatus, 1));
            vo.setArticleCount(count.intValue());
        }
        
        return success(result.stream()
            .filter(vo -> vo.getArticleCount() != null && vo.getArticleCount() > 0)
            .sorted(Comparator.comparing(AppCategoryRespVO::getArticleCount).reversed())
            .collect(Collectors.toList()));
    }

}
