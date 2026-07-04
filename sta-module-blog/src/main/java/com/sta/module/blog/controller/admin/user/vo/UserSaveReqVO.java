package com.sta.module.blog.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Schema(description = "管理后台 - 用户创建/更新 Request VO")
@Data
public class UserSaveReqVO {

    @Schema(description = "用户编号", example = "1")
    private Long id;

    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "user@example.com")
    @NotEmpty(message = "邮箱不能为空", groups = {Create.class})
    @Email(message = "邮箱格式不正确")
    @Size(max = 50, message = "邮箱长度不能超过 50 个字符")
    private String email;

    @Schema(description = "密码（创建时必填，更新时留空不修改）", example = "123456")
    @NotEmpty(message = "密码不能为空", groups = {Create.class})
    @Size(min = 6, max = 20, message = "密码长度为 6-20 位")
    private String password;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "用户昵称不能为空")
    @Size(max = 30, message = "用户昵称长度不能超过 30 个字符")
    private String nickname;

    @Schema(description = "头像", example = "https://example.com/avatar.png")
    @URL(message = "头像必须是 URL 格式")
    @Size(max = 512, message = "头像长度不能超过 512 个字符")
    private String avatar;

    @Schema(description = "性别（0-未知，1-男，2-女）", example = "1")
    private Integer sex;

    @Schema(description = "状态（0-禁用，1-启用）", example = "1")
    private Integer status;

    /**
     * 创建分组
     */
    public interface Create {}

    /**
     * 更新分组
     */
    public interface Update {}

}
