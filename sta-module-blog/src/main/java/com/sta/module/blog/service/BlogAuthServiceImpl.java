package com.sta.module.blog.service;

import cn.hutool.core.util.RandomUtil;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthLoginRespVO;
import cn.iocoder.yudao.module.member.convert.auth.AuthConvert;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.dal.mysql.user.MemberUserMapper;
import cn.iocoder.yudao.module.member.service.auth.MemberAuthService;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import cn.iocoder.yudao.module.system.api.logger.LoginLogApi;
import cn.iocoder.yudao.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.yudao.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.yudao.module.system.enums.logger.LoginResultEnum;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import com.sta.module.blog.controller.app.auth.vo.*;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.module.member.enums.ErrorCodeConstants.*;

/**
 * 博客认证 Service 实现类
 */
@Service
@Validated
public class BlogAuthServiceImpl implements BlogAuthService {

    @Resource
    private MemberUserMapper memberUserMapper;

    @Resource
    private MemberUserService memberUserService;

    @Resource
    private MemberAuthService memberAuthService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private OAuth2TokenCommonApi oauth2TokenApi;

    @Resource
    private LoginLogApi loginLogApi;

    private final java.util.Map<String, String> emailCodeCache = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public AppAuthLoginRespVO emailLogin(AppBlogEmailLoginReqVO reqVO) {
        MemberUserDO user = memberUserMapper.selectByEmail(reqVO.getEmail());
        if (user == null) {
            createLoginLog(null, reqVO.getEmail(), LoginLogTypeEnum.LOGIN_MOBILE, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (CommonStatusEnum.isDisable(user.getStatus())) {
            createLoginLog(user.getId(), reqVO.getEmail(), LoginLogTypeEnum.LOGIN_MOBILE, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        if (!memberUserService.isPasswordMatch(reqVO.getPassword(), user.getPassword())) {
            createLoginLog(user.getId(), reqVO.getEmail(), LoginLogTypeEnum.LOGIN_MOBILE, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        return createTokenAfterLoginSuccess(user, reqVO.getEmail(), LoginLogTypeEnum.LOGIN_MOBILE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long emailRegister(AppBlogEmailRegisterReqVO reqVO) {
        String cacheKey = reqVO.getEmail() + ":register";
        String cachedCode = emailCodeCache.get(cacheKey);
        if (cachedCode == null || !cachedCode.equals(reqVO.getCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }
        if (memberUserMapper.selectByEmail(reqVO.getEmail()) != null) {
            throw new RuntimeException("该邮箱已被注册");
        }
        MemberUserDO user = new MemberUserDO();
        user.setEmail(reqVO.getEmail());
        user.setNickname(reqVO.getUsername());
        user.setPassword(passwordEncoder.encode(reqVO.getPassword()));
        user.setStatus(CommonStatusEnum.ENABLE.getStatus());
        user.setRegisterIp(getClientIP());
        memberUserMapper.insert(user);
        emailCodeCache.remove(cacheKey);
        return user.getId();
    }

    @Override
    public void sendEmailCode(AppBlogSendEmailCodeReqVO reqVO) {
        String code = RandomUtil.randomNumbers(6);
        String cacheKey = reqVO.getEmail() + ":" + reqVO.getScene();
        emailCodeCache.put(cacheKey, code);
        System.out.println("[Email Code] to=" + reqVO.getEmail() + " scene=" + reqVO.getScene() + " code=" + code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(AppBlogResetPasswordReqVO reqVO) {
        String cacheKey = reqVO.getEmail() + ":reset";
        String cachedCode = emailCodeCache.get(cacheKey);
        if (cachedCode == null || !cachedCode.equals(reqVO.getCode())) {
            throw new RuntimeException("验证码错误或已过期");
        }
        MemberUserDO user = memberUserMapper.selectByEmail(reqVO.getEmail());
        if (user == null) {
            throw exception(USER_MOBILE_NOT_EXISTS);
        }
        memberUserMapper.updateById(MemberUserDO.builder()
                .id(user.getId())
                .password(passwordEncoder.encode(reqVO.getPassword()))
                .build());
        emailCodeCache.remove(cacheKey);
    }

    @Override
    public AppAuthLoginRespVO socialLogin(AppBlogSocialLoginReqVO reqVO) {
        cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthSocialLoginReqVO memberReqVO =
                cn.iocoder.yudao.module.member.controller.app.auth.vo.AppAuthSocialLoginReqVO.builder()
                        .type(reqVO.getType())
                        .code(reqVO.getCode())
                        .state(reqVO.getState())
                        .build();
        return memberAuthService.socialLogin(memberReqVO);
    }

    @Override
    public String getSocialAuthorizeUrl(Integer type, String redirectUri) {
        return memberAuthService.getSocialAuthorizeUrl(type, redirectUri);
    }

    @Override
    public void logout(String token) {
        memberAuthService.logout(token);
    }

    @Override
    public AppAuthLoginRespVO refreshToken(String refreshToken) {
        return memberAuthService.refreshToken(refreshToken);
    }

    // ========== 内部方法 ==========

    private AppAuthLoginRespVO createTokenAfterLoginSuccess(MemberUserDO user, String loginName,
                                                            LoginLogTypeEnum logType) {
        createLoginLog(user.getId(), loginName, logType, LoginResultEnum.SUCCESS);
        OAuth2AccessTokenRespDTO tokenResp = oauth2TokenApi.createAccessToken(
                new OAuth2AccessTokenCreateReqDTO()
                        .setUserId(user.getId())
                        .setUserType(UserTypeEnum.MEMBER.getValue())
                        .setClientId(OAuth2ClientConstants.CLIENT_ID_DEFAULT));
        return AuthConvert.INSTANCE.convert(tokenResp, null);
    }

    private void createLoginLog(Long userId, String loginName, LoginLogTypeEnum logType, LoginResultEnum loginResult) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logType.getType());
        reqDTO.setTraceId(cn.iocoder.yudao.framework.common.util.monitor.TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(UserTypeEnum.MEMBER.getValue());
        reqDTO.setUsername(loginName);
        reqDTO.setUserAgent(cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getUserAgent());
        reqDTO.setUserIp(getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogApi.createLoginLog(reqDTO);
    }

}
