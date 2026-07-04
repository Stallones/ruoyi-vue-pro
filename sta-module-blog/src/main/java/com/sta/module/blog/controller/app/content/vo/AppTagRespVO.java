package com.sta.module.blog.controller.app.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 博客标签 Response VO")
@Data
public class AppTagRespVO {

    @Schema(description = "标签编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "标签名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Java")
    private String tagName;

    @Schema(description = "文章数量", example = "10")
    private Integer articleCount;

}
