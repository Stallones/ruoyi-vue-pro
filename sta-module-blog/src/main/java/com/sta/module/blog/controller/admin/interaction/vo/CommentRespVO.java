package com.sta.module.blog.controller.admin.interaction.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.sta.module.blog.enums.TypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客评论 Response VO")
@Data
public class CommentRespVO {

    @Schema(description = "评论编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "评论类型（20评论 21评论回复）", example = "20")
    private TypeEnum type;

    @Schema(description = "文章ID", example = "1")
    private Long articleId;

    @Schema(description = "父级ID（保留层级关系，当前业务不查）", example = "0")
    private Long parentId;

    @Schema(description = "根节点ID（顶级评论为0，回复指向根评论）", example = "0")
    private Long rootId;

    @Schema(description = "内容")
    private String content;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "被回复用户ID", example = "0")
    private Long toUserId;

    @Schema(description = "是否通过（0否 1是）", example = "1")
    private Integer isCheck;

    @Schema(description = "IP属地")
    private String ipLocation;

    @Schema(description = "浏览器")
    private String browser;

    @Schema(description = "操作系统")
    private String os;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
