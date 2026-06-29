package com.sta.module.blog.controller.app.auth;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.security.config.SecurityProperties;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthLoginRespVO;
import com.sta.module.blog.controller.app.auth.vo.*;
import com.sta.module.blog.service.BlogAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客认证（邮箱）")
@RestController
@RequestMapping("/blog/auth")
@Validated
@PermitAll
public class AppBlogAuthController {

    @Resource
    private BlogAuthService blogAuthService;

    @Resource
    private SecurityProperties securityProperties;

    @PostMapping("/email-login")
    @Operation(summary = "邮箱 + 密码登录")
    public CommonResult<AppAuthLoginRespVO> emailLogin(@Valid @RequestBody AppBlogEmailLoginReqVO reqVO) {
        return success(blogAuthService.emailLogin(reqVO));
    }

    @PostMapping("/email-register")
    @Operation(summary = "邮箱 + 密码 + 验证码注册")
    public CommonResult<Long> emailRegister(@Valid @RequestBody AppBlogEmailRegisterReqVO reqVO) {
        return success(blogAuthService.emailRegister(reqVO));
    }

    @PostMapping("/send-email-code")
    @Operation(summary = "发送邮箱验证码")
    public CommonResult<Boolean> sendEmailCode(@Valid @RequestBody AppBlogSendEmailCodeReqVO reqVO) {
        blogAuthService.sendEmailCode(reqVO);
        return success(true);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "重置密码（邮箱 + 验证码 + 新密码）")
    public CommonResult<Boolean> resetPassword(@Valid @RequestBody AppBlogResetPasswordReqVO reqVO) {
        blogAuthService.resetPassword(reqVO);
        return success(true);
    }

    @PostMapping("/social-login")
    @Operation(summary = "社交登录（QQ/GitHub）")
    public CommonResult<AppAuthLoginRespVO> socialLogin(@Valid @RequestBody AppBlogSocialLoginReqVO reqVO) {
        return success(blogAuthService.socialLogin(reqVO));
    }

    @GetMapping("/social-auth-redirect")
    @Operation(summary = "获取社交授权跳转 URL")
    @Parameters({
            @Parameter(name = "type", description = "社交平台类型", required = true),
            @Parameter(name = "redirectUri", description = "回调路径", required = true)
    })
    public CommonResult<String> socialAuthRedirect(@RequestParam("type") Integer type,
                                                   @RequestParam("redirectUri") String redirectUri) {
        return success(blogAuthService.getSocialAuthorizeUrl(type, redirectUri));
    }

    @PostMapping("/logout")
    @Operation(summary = "登出系统")
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = SecurityFrameworkUtils.obtainAuthorization(request,
                securityProperties.getTokenHeader(), securityProperties.getTokenParameter());
        if (StrUtil.isNotBlank(token)) {
            blogAuthService.logout(token);
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    public CommonResult<AppAuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(blogAuthService.refreshToken(refreshToken));
    }

}
