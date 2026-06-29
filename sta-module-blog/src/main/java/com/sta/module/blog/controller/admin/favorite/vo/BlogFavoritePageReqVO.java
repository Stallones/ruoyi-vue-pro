package com.sta.module.blog.controller.admin.favorite.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.sta.module.blog.enums.BlogTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 博客收藏分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BlogFavoritePageReqVO extends PageParam {

    @Schema(description = "收藏类型", example = "1")
    private BlogTypeEnum type;

    @Schema(description = "目标ID", example = "1")
    private Long dataId;

}
