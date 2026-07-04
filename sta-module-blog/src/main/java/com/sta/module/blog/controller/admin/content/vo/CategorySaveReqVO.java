package com.sta.module.blog.controller.admin.content.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "管理后台 - 博客分类新增/修改 Request VO")
@Data
public class CategorySaveReqVO {

    @Schema(description = "分类编号", example = "1")
    private Long id;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Java")
    @NotEmpty(message = "分类名称不能为空")
    private String categoryName;

}
