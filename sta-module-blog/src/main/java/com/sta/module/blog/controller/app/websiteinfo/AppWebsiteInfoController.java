package com.sta.module.blog.controller.app.websiteinfo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.app.websiteinfo.vo.AppWebsiteInfoRespVO;
import com.sta.module.blog.dal.dataobject.BlogWebsiteInfoDO;
import com.sta.module.blog.service.BlogWebsiteInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客网站信息")
@RestController
@RequestMapping("/blog/website-info")
@Validated
@PermitAll
public class AppWebsiteInfoController {

    @Resource
    private BlogWebsiteInfoService websiteInfoService;

    @GetMapping("/get")
    @Operation(summary = "获得网站信息")
    public CommonResult<AppWebsiteInfoRespVO> getWebsiteInfo() {
        BlogWebsiteInfoDO info = websiteInfoService.getWebsiteInfo();
        return success(BeanUtils.toBean(info, AppWebsiteInfoRespVO.class));
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查")
    public CommonResult<Boolean> healthCheck() {
        return success(websiteInfoService.healthCheck());
    }

}
