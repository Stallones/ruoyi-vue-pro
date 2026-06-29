package com.sta.module.blog.controller.app.other;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.PermitAll;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客其他")
@RestController
@RequestMapping("/blog/other")
@Validated
@PermitAll
public class AppBlogOtherController {

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 一言转发接口
     * 默认使用 v1.hitokoto.cn 的 API，可通过环境变量 VITE_YIYAN_API 覆盖
     */
    @GetMapping("/yiyan")
    @Operation(summary = "获取一言")
    public CommonResult<String> getYiyan() {
        try {
            String yiYanUrl = "https://v1.hitokoto.cn/?c=a&encode=json";
            String envUrl = System.getenv("VITE_YIYAN_API");
            if (envUrl != null && !envUrl.isBlank()) {
                yiYanUrl = envUrl;
            }
            ResponseEntity<Map> response = restTemplate.getForEntity(yiYanUrl, Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> body = response.getBody();
                String content = (String) body.getOrDefault("hitokoto", "");
                String source = (String) body.getOrDefault("from", "");
                return success(content + (source.isEmpty() ? "" : " ——" + source));
            }
            return success("");
        } catch (Exception e) {
            return success("");
        }
    }

}
