package com.sta.module.blog.controller.admin.site.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 博客友链分页 Request VO")
@Data
public class LinkPageReqVO extends PageParam {

    @Schema(description = "网站名称", example = "test")
    private String name;

    @Schema(description = "审核状态", example = "1")
    private Integer isCheck;

}
