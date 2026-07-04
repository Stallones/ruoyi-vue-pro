package com.sta.module.blog.controller.app.interaction.vo;

import com.sta.module.blog.enums.TypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 博客收藏 Request VO")
@Data
public class AppFavoriteReqVO {

    @Schema(description = "收藏类型（10文章）", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    @NotNull(message = "收藏类型不能为空")
    private TypeEnum type;

    @Schema(description = "目标ID（文章ID）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "目标ID不能为空")
    private Long typeId;

}
