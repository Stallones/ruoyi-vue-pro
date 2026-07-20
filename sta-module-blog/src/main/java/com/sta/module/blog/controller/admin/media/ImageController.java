package com.sta.module.blog.controller.admin.media;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.media.vo.ImageRespVO;
import com.sta.module.blog.controller.admin.media.vo.ImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.enums.TypeEnum;
import com.sta.module.blog.service.media.ImageService;
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
public class ImageController {

    @Resource
    private ImageService imageService;

    @PostMapping("/create")
    @Operation(summary = "创建图片")
    @PreAuthorize("@ss.hasPermission('blog:image:create')")
    public CommonResult<Long> createImage(@Valid @RequestBody ImageSaveReqVO createReqVO) {
        return success(imageService.createImage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新图片")
    @PreAuthorize("@ss.hasPermission('blog:image:update')")
    public CommonResult<Boolean> updateImage(@Valid @RequestBody ImageSaveReqVO updateReqVO) {
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
    public CommonResult<ImageRespVO> getImage(@RequestParam("id") Long id) {
        ImageDO image = imageService.getImage(id);
        return success(BeanUtils.toBean(image, ImageRespVO.class));
    }

    @PutMapping("/update-sort")
    @Operation(summary = "批量更新图片排序")
    @PreAuthorize("@ss.hasPermission('blog:image:update')")
    public CommonResult<Boolean> updateSort(@RequestBody List<ImageSaveReqVO> sortList) {
        imageService.updateSort(sortList);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获得图片列表（按类型过滤）")
    @Parameter(name = "type", description = "图片类型（51封面图 52轮播图 53banner图 54头像）")
    @PreAuthorize("@ss.hasPermission('blog:image:query')")
    public CommonResult<List<ImageRespVO>> getImageList(
            @RequestParam(value = "type", required = false) Integer type) {
        List<ImageDO> list = imageService.getImageListByType(
                type != null ? TypeEnum.of(type) : null, null);
        return success(BeanUtils.toBean(list, ImageRespVO.class));
    }

}
