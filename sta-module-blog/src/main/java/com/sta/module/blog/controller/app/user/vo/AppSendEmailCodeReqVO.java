package com.sta.module.blog.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 发送邮箱验证码 Request VO")
@Data
public class AppSendEmailCodeReqVO {

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "user@example.com")
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "发送场景（register 注册 / reset 重置密码 / resetEmail 修改邮箱）",
            requiredMode = Schema.RequiredMode.REQUIRED, example = "register")
    @NotNull(message = "发送场景不能为空")
    private String scene;

}
