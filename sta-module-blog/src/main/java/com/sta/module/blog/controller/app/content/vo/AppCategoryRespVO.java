package com.sta.module.blog.controller.app.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 博客分类 Response VO")
@Data
public class AppCategoryRespVO {

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Java")
    private String categoryName;

    @Schema(description = "文章数量", example = "10")
    private Integer articleCount;

}
