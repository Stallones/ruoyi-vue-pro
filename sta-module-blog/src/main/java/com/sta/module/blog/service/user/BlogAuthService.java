package com.sta.module.blog.service.user;

import com.sta.module.blog.controller.app.user.vo.*;

/**
 * 博客认证 Service 接口（邮箱登录/注册/社交登录）
 *
 * @author blog
 */
public interface BlogAuthService {

    AppAuthLoginRespVO emailLogin(AppEmailLoginReqVO reqVO);

    Long emailRegister(AppEmailRegisterReqVO reqVO);

    void sendEmailCode(AppSendEmailCodeReqVO reqVO);

    void resetPassword(AppResetPasswordReqVO reqVO);

    AppAuthLoginRespVO socialLogin(AppSocialLoginReqVO reqVO);

    String getSocialAuthorizeUrl(Integer type, String redirectUri);

    void logout(String token);

    AppAuthLoginRespVO refreshToken(String refreshToken);

}
