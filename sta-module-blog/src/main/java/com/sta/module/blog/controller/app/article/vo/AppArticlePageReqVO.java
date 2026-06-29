package com.sta.module.blog.controller.app.article.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "用户 APP - 博客文章分页 Request VO")
@Data
public class AppArticlePageReqVO extends PageParam {

    @Schema(description = "分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "标签编号", example = "1")
    private Long tagId;

    @Schema(description = "省略内容长度（默认200）", example = "200")
    private Integer summaryLength;

}
