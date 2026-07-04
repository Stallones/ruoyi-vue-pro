package com.sta.module.blog.controller.admin.content.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 博客标签分页 Request VO")
@Data
public class TagPageReqVO extends PageParam {

    @Schema(description = "标签名称", example = "Java")
    private String tagName;

}
