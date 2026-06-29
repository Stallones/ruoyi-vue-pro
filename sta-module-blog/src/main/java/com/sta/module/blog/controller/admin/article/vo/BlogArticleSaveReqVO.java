package com.sta.module.blog.controller.admin.article.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 博客文章新增/修改 Request VO")
@Data
public class BlogArticleSaveReqVO {

    @Schema(description = "文章编号", example = "1")
    private Long id;

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "分类编号不能为空")
    private Long categoryId;

    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "标题")
    @NotEmpty(message = "文章标题不能为空")
    private String title;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "文章状态（1通过 0不通过）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "文章状态不能为空")
    private Integer status;

    @Schema(description = "标签编号列表")
    private List<Long> tagIds;

}
