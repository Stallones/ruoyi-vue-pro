package com.sta.module.blog.controller.app.link.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Schema(description = "用户 APP - 博客友链申请 Request VO")
@Data
public class AppLinkApplyReqVO {

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

}
