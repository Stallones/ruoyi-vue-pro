package com.sta.module.blog.controller.admin.category;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.category.vo.BlogCategoryRespVO;
import com.sta.module.blog.controller.admin.category.vo.BlogCategorySaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogCategoryDO;
import com.sta.module.blog.service.BlogCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客分类")
@RestController
@RequestMapping("/blog/category")
@Validated
public class BlogCategoryController {

    @Resource
    private BlogCategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "创建分类")
    @PreAuthorize("@ss.hasPermission('blog:category:create')")
    public CommonResult<Long> createCategory(@Valid @RequestBody BlogCategorySaveReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新分类")
    @PreAuthorize("@ss.hasPermission('blog:category:update')")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody BlogCategorySaveReqVO updateReqVO) {
        categoryService.updateCategory(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:category:delete')")
    public CommonResult<Boolean> deleteCategory(@RequestParam("id") Long id) {
        categoryService.deleteCategory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('blog:category:query')")
    public CommonResult<BlogCategoryRespVO> getCategory(@RequestParam("id") Long id) {
        BlogCategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, BlogCategoryRespVO.class));
    }

}
