package com.sta.module.blog.controller.admin.image;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.image.vo.BlogImageRespVO;
import com.sta.module.blog.controller.admin.image.vo.BlogImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogImageDO;
import com.sta.module.blog.service.BlogImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客图片")
@RestController
@RequestMapping("/blog/image")
@Validated
public class BlogImageController {

    @Resource
    private BlogImageService imageService;

    @PostMapping("/create")
    @Operation(summary = "创建图片")
    @PreAuthorize("@ss.hasPermission('blog:image:create')")
    public CommonResult<Long> createImage(@Valid @RequestBody BlogImageSaveReqVO createReqVO) {
        return success(imageService.createImage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新图片")
    @PreAuthorize("@ss.hasPermission('blog:image:update')")
    public CommonResult<Boolean> updateImage(@Valid @RequestBody BlogImageSaveReqVO updateReqVO) {
        imageService.updateImage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除图片")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:image:delete')")
    public CommonResult<Boolean> deleteImage(@RequestParam("id") Long id) {
        imageService.deleteImage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得图片")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('blog:image:query')")
    public CommonResult<BlogImageRespVO> getImage(@RequestParam("id") Long id) {
        BlogImageDO image = imageService.getImage(id);
        return success(BeanUtils.toBean(image, BlogImageRespVO.class));
    }

    @PutMapping("/update-sort")
    @Operation(summary = "批量更新图片排序")
    @PreAuthorize("@ss.hasPermission('blog:image:update')")
    public CommonResult<Boolean> updateSort(@RequestBody List<BlogImageSaveReqVO> sortList) {
        imageService.updateSort(sortList);
        return success(true);
    }

}
