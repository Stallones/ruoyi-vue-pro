package com.sta.module.blog.controller.admin.content;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.content.vo.CategoryRespVO;
import com.sta.module.blog.controller.admin.content.vo.CategorySaveReqVO;
import com.sta.module.blog.dal.dataobject.content.CategoryDO;
import com.sta.module.blog.service.content.CategoryService;
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
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @PostMapping("/create")
    @Operation(summary = "创建分类")
    @PreAuthorize("@ss.hasPermission('blog:category:create')")
    public CommonResult<Long> createCategory(@Valid @RequestBody CategorySaveReqVO createReqVO) {
        return success(categoryService.createCategory(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新分类")
    @PreAuthorize("@ss.hasPermission('blog:category:update')")
    public CommonResult<Boolean> updateCategory(@Valid @RequestBody CategorySaveReqVO updateReqVO) {
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
    public CommonResult<CategoryRespVO> getCategory(@RequestParam("id") Long id) {
        CategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, CategoryRespVO.class));
    }

}
