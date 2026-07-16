package com.sta.module.blog.controller.app.media;

import cn.hutool.core.lang.Assert;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.controller.app.media.vo.AppImageRespVO;
import com.sta.module.blog.controller.app.media.vo.AppImageUploadReqVO;
import com.sta.module.blog.controller.app.media.vo.AppImageUploadRespVO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.enums.TypeEnum;
import com.sta.module.blog.service.media.ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客图片")
@RestController
@RequestMapping("/blog/image")
@Validated
public class AppImageController {

    @Resource
    private ImageService imageService;

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
    @Operation(summary = "上传图片")
    @Parameter(name = "file", description = "图片附件", required = true,
            schema = @Schema(type = "string", format = "binary"))
    public CommonResult<AppImageUploadRespVO> uploadImage(@Valid AppImageUploadReqVO uploadReqVO) throws Exception {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        Assert.notNull(loginUserId, "登录用户不能为空");
        ImageDO image = imageService.uploadImage(uploadReqVO.getFile(), TypeEnum.of(uploadReqVO.getType()),
                uploadReqVO.getDataId(), loginUserId);
        return success(new AppImageUploadRespVO().setUrl(image.getPath()).setId(image.getId()));
    }

}
