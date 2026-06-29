package com.sta.module.blog.controller.admin.websiteinfo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.sta.module.blog.controller.admin.websiteinfo.vo.BlogWebsiteInfoSaveReqVO;
import com.sta.module.blog.service.BlogWebsiteInfoService;
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
public class BlogWebsiteInfoController {

    @Resource
    private BlogWebsiteInfoService websiteInfoService;

    @PutMapping("/update")
    @Operation(summary = "更新网站信息")
    @PreAuthorize("@ss.hasPermission('blog:website-info:update')")
    public CommonResult<Boolean> updateWebsiteInfo(@Valid @RequestBody BlogWebsiteInfoSaveReqVO updateReqVO) {
        websiteInfoService.updateWebsiteInfo(updateReqVO);
        return success(true);
    }

}
