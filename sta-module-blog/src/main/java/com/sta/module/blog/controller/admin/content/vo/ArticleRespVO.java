package com.sta.module.blog.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 博客文章 Response VO")
@Data
public class ArticleRespVO {

    @Schema(description = "文章编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long categoryId;

    @Schema(description = "分类名称", example = "Java")
    private String categoryName;

    @Schema(description = "文章标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "标题")
    private String title;

    @Schema(description = "文章内容")
    private String content;

    @Schema(description = "文章状态（1通过 0不通过）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "访问量", example = "1024")
    private Long visitCount;

    @Schema(description = "标签编号列表")
    private List<Long> tagIds;

    @Schema(description = "标签名称列表")
    private List<String> tagNames;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
