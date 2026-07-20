package com.sta.module.blog.controller.app.site.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 博客网站统计 Response VO")
@Data
public class AppWebsiteInfoRespVO {

    @Schema(description = "文章数量")
    private Long articleCount;

    @Schema(description = "评论数量")
    private Long commentCount;

    @Schema(description = "留言数量")
    private Long messageCount;

    @Schema(description = "点赞数量（全站）")
    private Long likeCount;

    @Schema(description = "收藏数量（全站）")
    private Long favoriteCount;

    @Schema(description = "分类数量")
    private Long categoryCount;

    @Schema(description = "标签数量")
    private Long tagCount;

    @Schema(description = "总访问量")
    private Long visitCount;

    @Schema(description = "最后更新时间")
    private LocalDateTime lastUpdateTime;

}
