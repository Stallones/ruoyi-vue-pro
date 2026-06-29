package com.sta.module.blog.controller.app.category;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.app.category.vo.AppCategoryRespVO;
import com.sta.module.blog.dal.dataobject.BlogCategoryDO;
import com.sta.module.blog.service.BlogCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客分类")
@RestController
@RequestMapping("/blog/category")
@Validated
@PermitAll
public class AppCategoryController {

    @Resource
    private BlogCategoryService categoryService;

    @GetMapping("/get")
    @Operation(summary = "获得分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    public CommonResult<AppCategoryRespVO> getCategory(@RequestParam("id") Long id) {
        BlogCategoryDO category = categoryService.getCategory(id);
        return success(BeanUtils.toBean(category, AppCategoryRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得分类列表")
    public CommonResult<List<AppCategoryRespVO>> getCategoryList() {
        List<BlogCategoryDO> list = categoryService.getCategoryList();
        return success(BeanUtils.toBean(list, AppCategoryRespVO.class));
    }

}
