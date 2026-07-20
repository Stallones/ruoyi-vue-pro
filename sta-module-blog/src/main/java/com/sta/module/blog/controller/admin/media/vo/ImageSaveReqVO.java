package com.sta.module.blog.controller.admin.media.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import com.sta.module.blog.enums.TypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - 博客图片新增/修改 Request VO")
@Data
public class ImageSaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "图类型（51封面图 52轮播图 53banner图 54头像）", requiredMode = Schema.RequiredMode.REQUIRED, example = "51")
    @NotNull(message = "图类型不能为空")
    private TypeEnum type;

    @Schema(description = "目标ID", example = "1")
    private Long dataId;

    @Schema(description = "图片路径", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com/img.jpg")
    @NotEmpty(message = "图片路径不能为空")
    private String path;

    @Schema(description = "图片大小（字节）", example = "1024")
    private Long size;

    @Schema(description = "图片MIME类型", example = "image/jpeg")
    private String extension;

    @Schema(description = "排序", example = "0")
    private Integer sort;

}
