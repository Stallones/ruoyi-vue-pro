package com.sta.module.blog.controller.app.media;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.controller.app.media.vo.AppImageRespVO;
import com.sta.module.blog.controller.app.media.vo.AppImageUploadRespVO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.enums.TypeEnum;
import com.sta.module.blog.service.media.BlogFileService;
import com.sta.module.blog.service.media.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static com.sta.module.blog.enums.ErrorCodeConstants.IMAGE_TYPE_INVALID;

@Tag(name = "用户 APP - 博客图片")
@RestController
@RequestMapping("/blog/image")
@Validated
public class AppImageController {

    @Resource
    private ImageService imageService;

    @Resource
    private BlogFileService blogFileService;

    @GetMapping("/list")
    @Operation(summary = "获得图片列表（按类型）")
    @Parameter(name = "type", description = "图片类型（51封面图 52轮播图 53banner图 54头像）", example = "51")
    @PermitAll
    public CommonResult<List<AppImageRespVO>> getImageList(
            @RequestParam(value = "type", required = false) Integer type) {
        List<ImageDO> list = imageService.getImageListByType(TypeEnum.of(type), null);
        return success(BeanUtils.toBean(list, AppImageRespVO.class));
    }

    @PostMapping("/upload")
    @Operation(summary = "上传图片（后端转发到 MinIO 并记录 infra_file）")
    public CommonResult<AppImageUploadRespVO> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") Integer type) throws IOException {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Assert.notNull(loginUserId, "登录用户不能为空");
        String directory = resolveDirectory(TypeEnum.of(type));
        String url = blogFileService.uploadAndRecord(file.getBytes(), file.getOriginalFilename(), directory);
        return success(new AppImageUploadRespVO().setUrl(url));
    }

    /**
     * 根据图片类型推导上传目录
     */
    private String resolveDirectory(TypeEnum type) {
        return switch (type) {
            case IMG_AVATAR -> "blog/avatar";
            case IMG_COVER -> "blog/article";
            case IMG_HOME, IMG_PAGE -> "blog/other";
            default -> throw exception(IMAGE_TYPE_INVALID);
        };
    }

}
