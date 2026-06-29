package com.sta.module.blog.controller.admin.tag.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "管理后台 - 博客标签新增/修改 Request VO")
@Data
public class BlogTagSaveReqVO {

    @Schema(description = "标签编号", example = "1")
    private Long id;

    @Schema(description = "标签名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "Java")
    @NotEmpty(message = "标签名称不能为空")
    private String tagName;

}
