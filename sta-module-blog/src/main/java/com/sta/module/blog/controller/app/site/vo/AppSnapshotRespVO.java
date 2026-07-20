package com.sta.module.blog.controller.app.site.vo;

import com.sta.module.blog.controller.app.content.vo.AppArticleRespVO;
import com.sta.module.blog.controller.app.content.vo.AppCategoryRespVO;
import com.sta.module.blog.controller.app.content.vo.AppTagRespVO;
import com.sta.module.blog.controller.app.media.vo.AppImageRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "用户 APP - 博客全站离线快照 Response VO")
@Data
public class AppSnapshotRespVO {

    @Schema(description = "网站信息")
    private AppWebsiteInfoRespVO websiteInfo;

    @Schema(description = "文章列表（含摘要，不含全文）")
    private List<AppArticleRespVO> articles;

    @Schema(description = "分类列表")
    private List<AppCategoryRespVO> categories;

    @Schema(description = "标签列表")
    private List<AppTagRespVO> tags;

    @Schema(description = "图片列表")
    private List<AppImageRespVO> images;

    @Schema(description = "友链列表（已审核）")
    private List<AppLinkRespVO> links;

}
