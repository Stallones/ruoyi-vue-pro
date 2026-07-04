package com.sta.module.blog.controller.app.site;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.app.site.vo.AppWebsiteInfoRespVO;
import com.sta.module.blog.dal.dataobject.site.WebsiteInfoDO;
import com.sta.module.blog.service.site.WebsiteInfoService;
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
    private WebsiteInfoService websiteInfoService;

    @GetMapping("/get")
    @Operation(summary = "获得网站信息")
    public CommonResult<AppWebsiteInfoRespVO> getWebsiteInfo() {
        WebsiteInfoDO info = websiteInfoService.getWebsiteInfo();
        return success(BeanUtils.toBean(info, AppWebsiteInfoRespVO.class));
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查")
    public CommonResult<Boolean> healthCheck() {
        return success(websiteInfoService.healthCheck());
    }

}
