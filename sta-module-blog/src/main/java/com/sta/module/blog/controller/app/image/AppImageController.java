package com.sta.module.blog.controller.app.image;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.app.image.vo.AppImageRespVO;
import com.sta.module.blog.dal.dataobject.BlogImageDO;
import com.sta.module.blog.enums.BlogTypeEnum;
import com.sta.module.blog.service.BlogImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客图片")
@RestController
@RequestMapping("/blog/image")
@Validated
@PermitAll
public class AppImageController {

    @Resource
    private BlogImageService imageService;

    @GetMapping("/list")
    @Operation(summary = "获得图片列表（按类型）")
    @Parameter(name = "type", description = "图片类型（51封面图 52轮播图 53banner图）", example = "51")
    public CommonResult<List<AppImageRespVO>> getImageList(
            @RequestParam(value = "type", required = false) Integer type) {
        List<BlogImageDO> list = imageService.getImageList(BlogTypeEnum.of(type), null);
        return success(BeanUtils.toBean(list, AppImageRespVO.class));
    }

}
