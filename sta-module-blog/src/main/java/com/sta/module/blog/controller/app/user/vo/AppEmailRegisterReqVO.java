package com.sta.module.blog.controller.app.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Schema(description = "用户 APP - 邮箱注册 Request VO")
@Data
public class AppEmailRegisterReqVO {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "用户名不能为空")
    @Length(min = 2, max = 20, message = "用户名长度为 2-20 位")
    private String username;

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "user@example.com")
    @NotEmpty(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "密码不能为空")
    @Length(min = 6, max = 20, message = "密码长度为 6-20 位")
    private String password;

    @Schema(description = "邮箱验证码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    @NotEmpty(message = "验证码不能为空")
    @Length(min = 4, max = 6, message = "验证码长度为 4-6 位")
    private String code;

}
