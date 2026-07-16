package com.sta.module.blog.controller.app.media.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Schema(description = "用户 App - 博客图片上传 Request VO")
@Data
public class AppImageUploadReqVO {

    @Schema(description = "图片文件", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "图片文件不能为空")
    private MultipartFile file;

    @Schema(description = "图片类型（54头像 51封面 52轮播 53banner）", requiredMode = Schema.RequiredMode.REQUIRED, example = "54")
    @NotNull(message = "图片类型不能为空")
    private Integer type;

    @Schema(description = "关联数据 ID（头像上传无需填写，后端取当前登录用户）", example = "1")
    private Long dataId;

}
