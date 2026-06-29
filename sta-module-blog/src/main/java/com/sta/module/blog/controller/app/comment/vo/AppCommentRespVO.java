package com.sta.module.blog.controller.app.comment.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.sta.module.blog.enums.BlogTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 博客评论 Response VO")
@Data
public class AppCommentRespVO {

    @Schema(description = "评论编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "评论类型（10文章 20评论 21评论回复 30留言 31留言回复 51封面图 52轮播图 53banner图）", example = "20")
    private BlogTypeEnum type;

    @Schema(description = "文章ID", example = "1")
    private Long articleId;

    @Schema(description = "父级ID", example = "0")
    private Long parentId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "用户昵称")
    private String userNickname;

    @Schema(description = "用户头像")
    private String userAvatar;

    @Schema(description = "被回复用户ID", example = "0")
    private Long toUserId;

    @Schema(description = "被回复用户昵称")
    private String toUserNickname;

    @Schema(description = "是否通过（0否 1是）", example = "1")
    private Integer isCheck;

    @Schema(description = "点赞量", example = "10")
    private Long likeCount;

    @Schema(description = "当前用户是否已点赞")
    private Boolean isLiked;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "回复列表（嵌套树结构）")
    private List<AppCommentRespVO> replies;

}
