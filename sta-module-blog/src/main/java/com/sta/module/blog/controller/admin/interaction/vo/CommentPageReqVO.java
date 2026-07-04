package com.sta.module.blog.controller.admin.interaction.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.sta.module.blog.enums.TypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 博客评论分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class CommentPageReqVO extends PageParam {

    @Schema(description = "评论类型（20评论 21评论回复）", example = "20")
    private TypeEnum type;

    @Schema(description = "文章ID", example = "1")
    private Long articleId;

    @Schema(description = "是否通过（0否 1是）", example = "1")
    private Integer isCheck;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
