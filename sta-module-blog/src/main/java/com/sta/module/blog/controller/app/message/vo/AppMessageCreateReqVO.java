package com.sta.module.blog.controller.app.message.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "用户 APP - 博客留言创建 Request VO")
@Data
public class AppMessageCreateReqVO {

    @Schema(description = "内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "留言内容不能为空")
    private String content;

    @Schema(description = "父级ID（留言传0，回复传被回复的留言/回复ID）", example = "0")
    private Long parentId = 0L;

    @Schema(description = "被回复用户ID（留言传0）", example = "0")
    private Long toUserId = 0L;

}
