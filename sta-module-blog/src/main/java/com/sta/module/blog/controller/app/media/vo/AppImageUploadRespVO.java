package com.sta.module.blog.controller.app.media.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 App - 图片上传 Response VO")
@Data
public class AppImageUploadRespVO {

    @Schema(description = "图片访问 URL", requiredMode = Schema.RequiredMode.REQUIRED,
            example = "http://192.168.20.128:9000/yudao/blog/avatar/20260722/xxx.jpg")
    private String url;

}
