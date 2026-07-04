package com.sta.module.blog.controller.app.interaction.vo;

import com.sta.module.blog.enums.TypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 博客收藏 Response VO")
@Data
public class AppFavoriteRespVO {

    @Schema(description = "收藏编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "收藏类型（1文章 2留言板）", example = "1")
    private TypeEnum type;

    @Schema(description = "目标ID", example = "1")
    private Long dataId;

    @Schema(description = "是否有效（0否 1是）", example = "1")
    private Integer isCheck;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
