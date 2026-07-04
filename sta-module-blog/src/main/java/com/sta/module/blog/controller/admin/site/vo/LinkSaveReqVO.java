package com.sta.module.blog.controller.admin.site.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "管理后台 - 博客友链新增/修改 Request VO")
@Data
public class LinkSaveReqVO {

    @Schema(description = "友链编号", example = "1")
    private Long id;

    @Schema(description = "网站名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
    @NotEmpty(message = "网站名称不能为空")
    private String name;

    @Schema(description = "网站地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com")
    @NotEmpty(message = "网站地址不能为空")
    private String url;

    @Schema(description = "网站描述", example = "这是一个测试网站")
    private String description;

    @Schema(description = "网站背景图", example = "https://example.com/bg.jpg")
    private String background;

    @Schema(description = "邮箱地址", example = "test@example.com")
    private String email;

    @Schema(description = "审核状态（0鏈€氳繃 1宸查€氳繃锛", example = "1")
    private Integer isCheck;

}
