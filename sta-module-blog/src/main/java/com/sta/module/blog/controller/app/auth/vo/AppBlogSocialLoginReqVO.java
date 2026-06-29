package com.sta.module.blog.controller.app.auth.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "用户 APP - 社交登录 Request VO")
@Data
public class AppBlogSocialLoginReqVO {

    @Schema(description = "社交平台类型（对应 SocialTypeEnum）", requiredMode = Schema.RequiredMode.REQUIRED, example = "20")
    @NotNull(message = "社交平台类型不能为空")
    private Integer type;

    @Schema(description = "授权码", requiredMode = Schema.RequiredMode.REQUIRED, example = "gh_xxxx")
    @NotEmpty(message = "授权码不能为空")
    private String code;

    @Schema(description = "state", requiredMode = Schema.RequiredMode.REQUIRED, example = "uuid")
    @NotEmpty(message = "state 不能为空")
    private String state;

}
