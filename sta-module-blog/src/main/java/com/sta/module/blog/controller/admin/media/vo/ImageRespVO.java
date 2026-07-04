package com.sta.module.blog.controller.admin.media.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.sta.module.blog.enums.TypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客图片 Response VO")
@Data
public class ImageRespVO {

    @Schema(description = "图片编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "图类型（51封面图 52轮播图 53banner图）", example = "51")
    private TypeEnum type;

    @Schema(description = "目标ID", example = "1")
    private Long dataId;

    @Schema(description = "图片路径", example = "https://example.com/img.jpg")
    private String path;

    @Schema(description = "图片大小（字节）", example = "1024")
    private Long size;

    @Schema(description = "图片MIME类型", example = "image/jpeg")
    private String extension;

    @Schema(description = "排序", example = "0")
    private Integer sort;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
