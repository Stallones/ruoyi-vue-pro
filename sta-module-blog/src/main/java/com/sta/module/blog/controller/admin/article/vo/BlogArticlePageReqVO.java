package com.sta.module.blog.controller.admin.article.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 博客文章分页 Request VO")
@Data
public class BlogArticlePageReqVO extends PageParam {

    @Schema(description = "文章标题", example = "Java")
    private String title;

    @Schema(description = "分类编号", example = "1")
    private Long categoryId;

    @Schema(description = "文章状态", example = "1")
    private Integer status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
