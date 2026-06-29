package com.sta.module.blog.controller.app.comment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 博客评论创建 Request VO")
@Data
public class AppCommentCreateReqVO {

    @Schema(description = "文章ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @Schema(description = "父级ID（评论传0，回复传被回复的评论/回复ID）", example = "0")
    private Long parentId = 0L;

    @Schema(description = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "评论内容不能为空")
    private String content;

    @Schema(description = "被回复用户ID（评论传0）", example = "0")
    private Long toUserId = 0L;

}
