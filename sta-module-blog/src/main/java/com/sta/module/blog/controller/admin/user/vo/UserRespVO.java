package com.sta.module.blog.controller.admin.user.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 用户信息 Response VO")
@Data
public class UserRespVO {

    @Schema(description = "用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "邮箱", example = "user@example.com")
    private String email;

    @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String nickname;

    @Schema(description = "头像", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://example.com/avatar.png")
    private String avatar;

    @Schema(description = "性别（0-未知，1-男，2-女）", example = "1")
    private Integer sex;

    @Schema(description = "状态（0-禁用，1-启用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer status;

    @Schema(description = "注册 IP", requiredMode = Schema.RequiredMode.REQUIRED, example = "127.0.0.1")
    private String registerIp;

    @Schema(description = "最后登录 IP", example = "127.0.0.1")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private LocalDateTime loginDate;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}
