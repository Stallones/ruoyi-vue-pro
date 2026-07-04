package com.sta.module.blog.controller.app.interaction.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.sta.module.blog.enums.TypeEnum;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "用户 APP - 博客留言 Response VO")
@Data
public class AppMessageRespVO {

    @Schema(description = "留言编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "留言类型（30留言 31留言回复）", example = "30")
    private TypeEnum type;

    @Schema(description = "父级ID", example = "0")
    private Long parentId;

    @Schema(description = "根节点ID（顶级留言为0，回复指向根留言）", example = "0")
    private Long rootId;

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
    private Integer status;

    @Schema(description = "IP属地")
    private String ipLocation;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "点赞量", example = "10")
    private Long likeCount;

    @Schema(description = "当前用户是否已点赞")
    private Boolean isLiked;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "回复数", example = "5")
    private Long replyCount;

    @Schema(description = "回复列表（嵌套树结构）")
    private List<AppMessageRespVO> replies;

}
