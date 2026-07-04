package com.sta.module.blog.controller.admin.interaction.vo;

import com.sta.module.blog.enums.TypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客收藏 Response VO")
@Data
public class FavoriteRespVO {

    @Schema(description = "收藏编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "收藏类型（10文章）", example = "10")
    private TypeEnum type;

    @Schema(description = "目标ID", example = "1")
    private Long dataId;

    @Schema(description = "用户ID", example = "1")
    private Long userId;

    @Schema(description = "状态（0取消 1有效）", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
