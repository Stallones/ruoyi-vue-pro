package com.sta.module.blog.controller.app.site;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.sta.module.blog.controller.app.content.vo.AppArticleRespVO;
import com.sta.module.blog.controller.app.site.vo.AppSnapshotRespVO;
import com.sta.module.blog.controller.app.site.vo.AppWebsiteInfoRespVO;
import com.sta.module.blog.service.site.WebsiteInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${sta.blog.snapshot-key:sta-blog-snapshot-2026}")
    private String snapshotKey;

    @GetMapping("/get")
    @Operation(summary = "获取全站统计数据")
    public CommonResult<AppWebsiteInfoRespVO> getWebsiteInfo() {
        return success(websiteInfoService.getStats());
    }

    @GetMapping("/health")
    @Operation(summary = "健康检查")
    public CommonResult<Boolean> healthCheck() {
        return success(websiteInfoService.healthCheck());
    }

    @GetMapping("/snapshot")
    @Operation(summary = "获取全站离线快照")
    public CommonResult<AppSnapshotRespVO> getSnapshot(@RequestParam("key") String key) {
        if (!snapshotKey.equals(key)) {
            return CommonResult.error(403, "密钥错误");
        }
        return success(websiteInfoService.getSnapshot());
    }

    @GetMapping("/snapshot-article/{id}")
    @Operation(summary = "获取单篇文章完整内容（快照用）")
    @Parameter(name = "id", description = "文章编号", required = true)
    public CommonResult<AppArticleRespVO> getSnapshotArticle(@PathVariable("id") Long id,
                                                             @RequestParam("key") String key) {
        if (!snapshotKey.equals(key)) {
            return CommonResult.error(403, "密钥错误");
        }
        return success(websiteInfoService.getArticleFull(id));
    }

}
