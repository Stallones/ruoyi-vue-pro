package com.sta.module.blog.controller.admin.site;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.sta.module.blog.controller.admin.site.vo.WebsiteInfoSaveReqVO;
import com.sta.module.blog.service.site.WebsiteInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客网站信息")
@RestController
@RequestMapping("/blog/website-info")
@Validated
public class WebsiteInfoController {

    @Resource
    private WebsiteInfoService websiteInfoService;

    @PutMapping("/update")
    @Operation(summary = "更新网站信息")
    @PreAuthorize("@ss.hasPermission('blog:website-info:update')")
    public CommonResult<Boolean> updateWebsiteInfo(@Valid @RequestBody WebsiteInfoSaveReqVO updateReqVO) {
        websiteInfoService.updateWebsiteInfo(updateReqVO);
        return success(true);
    }

}
