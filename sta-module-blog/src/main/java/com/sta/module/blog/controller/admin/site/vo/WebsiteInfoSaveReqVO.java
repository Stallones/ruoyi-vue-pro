package com.sta.module.blog.controller.admin.site.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 博客网站信息修改 Request VO")
@Data
public class WebsiteInfoSaveReqVO {

    @Schema(description = "网站信息编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "网站信息编号不能为空")
    private Long id;

    @Schema(description = "站长头像")
    private String webmasterAvatar;

    @Schema(description = "站长名称")
    private String webmasterName;

    @Schema(description = "站长文案")
    private String webmasterCopy;

    @Schema(description = "站长资料卡背景图")
    private String webmasterProfileBackground;

    @Schema(description = "Gitee链接")
    private String giteeLink;

    @Schema(description = "GitHub链接")
    private String githubLink;

    @Schema(description = "网站名称")
    private String websiteName;

    @Schema(description = "头部通知")
    private String headerNotification;

    @Schema(description = "侧面公告")
    private String sidebarAnnouncement;

    @Schema(description = "备案信息")
    private String recordInfo;

    @Schema(description = "开始运行时间")
    private LocalDateTime startTime;

}
