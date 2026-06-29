package com.sta.module.blog.controller.admin.comment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.sta.module.blog.enums.BlogTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客评论 Response VO")
@Data
public class BlogCommentRespVO {

    @Schema(description = "评论编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "评论类型（20评论 21评论回复）", example = "20")
    private BlogTypeEnum type;

    @Schema(description = "文章ID", example = "1")
    private Long articleId;

    @Schema(description = "父级ID（RE必填，指向被回复的评论/回复量）", example = "0")
    private Long parentId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "被回复用户ID", example = "0")
    private Long replyUserId;

    @Schema(description = "是否通过（0否 1是）", example = "1")
    private Integer isCheck;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
