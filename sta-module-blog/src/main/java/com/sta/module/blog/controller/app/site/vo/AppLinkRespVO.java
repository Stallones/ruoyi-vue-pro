package com.sta.module.blog.controller.app.site.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "用户 APP - 博客友链 Response VO")
@Data
public class AppLinkRespVO {

    @Schema(description = "友链编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Long id;

    @Schema(description = "网站名称", example = "test")
    private String name;

    @Schema(description = "网站地址", example = "https://example.com")
    private String url;

    @Schema(description = "网站描述")
    private String description;

    @Schema(description = "网站背景图")
    private String background;

    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "审核状态", example = "1")
    private Integer isCheck;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
