package com.sta.module.blog.service;

import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthLoginRespVO;
import com.sta.module.blog.controller.app.auth.vo.*;

/**
 * 博客认证 Service 接口（邮箱登录/注册/社交登录）
 */
public interface BlogAuthService {

    AppAuthLoginRespVO emailLogin(AppBlogEmailLoginReqVO reqVO);

    Long emailRegister(AppBlogEmailRegisterReqVO reqVO);

    void sendEmailCode(AppBlogSendEmailCodeReqVO reqVO);

    void resetPassword(AppBlogResetPasswordReqVO reqVO);

    AppAuthLoginRespVO socialLogin(AppBlogSocialLoginReqVO reqVO);

    String getSocialAuthorizeUrl(Integer type, String redirectUri);

    void logout(String token);

    AppAuthLoginRespVO refreshToken(String refreshToken);

}
