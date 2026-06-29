package com.sta.module.blog.controller.app.article.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 博客文章 Response VO")
@Data
public class AppArticleRespVO {

    @Schema(description = "文章编号", example = "1")
    private Long id;

    @Schema(description = "分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "分类名称", example = "Java")
    private String categoryName;

    @Schema(description = "封面图路径")
    private String coverPath;

    @Schema(description = "文章标题")
    private String title;

    @Schema(description = "文章内容（仅详情接口返回）")
    private String content;

    @Schema(description = "省略内容（列表接口返回，截取前N个字符）")
    private String summary;

    @Schema(description = "字数统计")
    private Integer wordCount;

    @Schema(description = "访问量", example = "1024")
    private Long visitCount;

    @Schema(description = "点赞量", example = "100")
    private Long likeCount;

    @Schema(description = "收藏量", example = "50")
    private Long favoriteCount;

    @Schema(description = "评论量", example = "10")
    private Long commentCount;

    @Schema(description = "当前用户是否已点赞")
    private Boolean isLiked;

    @Schema(description = "当前用户是否已收藏")
    private Boolean isFavorited;

    @Schema(description = "标签编号列表")
    private List<Long> tagIds;

    @Schema(description = "标签名称列表")
    private List<String> tagNames;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
