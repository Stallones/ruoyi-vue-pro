package com.sta.module.blog.controller.app.like.vo;

import com.sta.module.blog.enums.BlogTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 博客点赞 Request VO")
@Data
public class AppLikeReqVO {

    @Schema(description = "点赞类型（10文章 20评论 30留言）", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "点赞类型不能为空")
    private BlogTypeEnum type;

    @Schema(description = "目标ID（文章ID/评论ID/留言ID）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "目标ID不能为空")
    private Long typeId;

}
