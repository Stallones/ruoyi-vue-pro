package com.sta.module.blog.controller.app.media.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 App - 博客图片上传 Response VO")
@Data
public class AppImageUploadRespVO {

    @Schema(description = "图片访问 URL", requiredMode = Schema.RequiredMode.REQUIRED, example = "http://127.0.0.1:9000/yudao/blog/avatar/20260716/xxx.jpg")
    private String url;

    @Schema(description = "图片编号", example = "1")
    private Long id;

}
