package com.sta.module.blog.service.user;

import cn.hutool.core.util.RandomUtil;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.OAuth2TokenCommonApi;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.enums.UserTypeEnum;
import cn.iocoder.yudao.framework.common.util.monitor.TracerUtils;
import cn.iocoder.yudao.framework.common.util.servlet.ServletUtils;
import cn.iocoder.yudao.module.system.api.logger.LoginLogApi;
import cn.iocoder.yudao.module.system.api.logger.dto.LoginLogCreateReqDTO;
import cn.iocoder.yudao.module.system.api.mail.MailSendApi;
import cn.iocoder.yudao.module.system.api.mail.dto.MailSendSingleToUserReqDTO;
import cn.iocoder.yudao.module.system.api.social.SocialClientApi;
import cn.iocoder.yudao.module.system.api.social.SocialUserApi;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserBindReqDTO;
import cn.iocoder.yudao.module.system.api.social.dto.SocialUserRespDTO;
import cn.iocoder.yudao.module.system.enums.logger.LoginLogTypeEnum;
import cn.iocoder.yudao.module.system.enums.logger.LoginResultEnum;
import cn.iocoder.yudao.module.system.enums.oauth2.OAuth2ClientConstants;
import com.sta.module.blog.controller.app.user.vo.*;
import com.sta.module.blog.convert.auth.AuthConvert;
import com.sta.module.blog.dal.dataobject.user.BlogUserDO;
import com.sta.module.blog.dal.mysql.user.BlogUserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.sta.module.blog.enums.ErrorCodeConstants.*;

/**
 * 博客认证 Service 实现类
 *
 * @author blog
 */
@Service
@Validated
public class BlogAuthServiceImpl implements BlogAuthService {

    @Resource
    private BlogUserMapper blogUserMapper;

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private OAuth2TokenCommonApi oauth2TokenApi;

    @Resource
    private LoginLogApi loginLogApi;

    @Resource
    private MailSendApi mailSendApi;

    @Resource
    private SocialUserApi socialUserApi;

    @Resource
    private SocialClientApi socialClientApi;

    private final java.util.Map<String, String> emailCodeCache = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    public AppAuthLoginRespVO emailLogin(AppEmailLoginReqVO reqVO) {
        BlogUserDO user = blogUserMapper.selectByEmail(reqVO.getEmail());
        if (user == null) {
            createLoginLog(null, reqVO.getEmail(), LoginLogTypeEnum.LOGIN_MOBILE, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (CommonStatusEnum.isDisable(user.getStatus())) {
            createLoginLog(user.getId(), reqVO.getEmail(), LoginLogTypeEnum.LOGIN_MOBILE, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        if (!blogUserService.isPasswordMatch(reqVO.getPassword(), user.getPassword())) {
            createLoginLog(user.getId(), reqVO.getEmail(), LoginLogTypeEnum.LOGIN_MOBILE, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        return createTokenAfterLoginSuccess(user, reqVO.getEmail(), LoginLogTypeEnum.LOGIN_MOBILE);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long emailRegister(AppEmailRegisterReqVO reqVO) {
        String cacheKey = reqVO.getEmail() + ":register";
        String cachedCode = emailCodeCache.get(cacheKey);
        if (cachedCode == null || !cachedCode.equals(reqVO.getCode())) {
            throw exception(AUTH_CODE_INVALID_OR_EXPIRED);
        }
        if (blogUserMapper.selectByEmail(reqVO.getEmail()) != null) {
            throw exception(USER_EMAIL_USED);
        }
        BlogUserDO user = new BlogUserDO();
        user.setEmail(reqVO.getEmail());
        user.setNickname(reqVO.getUsername());
        user.setPassword(blogUserService.encodePassword(reqVO.getPassword()));
        user.setStatus(CommonStatusEnum.ENABLE.getStatus());
        user.setRegisterIp(getClientIP());
        blogUserMapper.insert(user);
        emailCodeCache.remove(cacheKey);
        return user.getId();
    }

    @Override
    public void sendEmailCode(AppSendEmailCodeReqVO reqVO) {
        // 重置密码场景：校验邮箱是否存在于系统中
        if ("reset".equals(reqVO.getScene())) {
            BlogUserDO user = blogUserMapper.selectByEmail(reqVO.getEmail());
            if (user == null) {
                throw exception(USER_EMAIL_NOT_EXISTS);
            }
        }
        // 注册 / 修改邮箱场景：校验邮箱未被其他用户占用
        if ("register".equals(reqVO.getScene()) || "resetEmail".equals(reqVO.getScene())) {
            BlogUserDO existingUser = blogUserMapper.selectByEmail(reqVO.getEmail());
            if (existingUser != null) {
                throw exception(USER_EMAIL_USED);
            }
        }
        String code = RandomUtil.randomNumbers(6);
        String cacheKey = reqVO.getEmail() + ":" + reqVO.getScene();
        emailCodeCache.put(cacheKey, code);

        String templateCode = getMailTemplateCode(reqVO.getScene());
        if (templateCode == null) {
            return;
        }

        try {
            mailSendApi.sendSingleMailToMember(new MailSendSingleToUserReqDTO()
                    .setToMails(List.of(reqVO.getEmail()))
                    .setTemplateCode(templateCode)
                    .setTemplateParams(Map.of(
                            "code", code,
                            "expirationTime", "5 分钟",
                            "toUrl", "https://your-blog-domain.com",
                            "openSourceAddress", "https://github.com"
                    )));
        } catch (Exception e) {
            System.err.println("[Email Send Failed] to=" + reqVO.getEmail() + " scene=" + reqVO.getScene() + " error=" + e.getMessage());
        }
    }

    private String getMailTemplateCode(String scene) {
        return switch (scene) {
            case "register" -> "blog-register-code";
            case "reset" -> "blog-reset-password-code";
            case "resetEmail" -> "blog-reset-email-code";
            default -> null;
        };
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(AppResetPasswordReqVO reqVO) {
        String cacheKey = reqVO.getEmail() + ":reset";
        String cachedCode = emailCodeCache.get(cacheKey);
        if (cachedCode == null || !cachedCode.equals(reqVO.getCode())) {
            throw exception(AUTH_CODE_INVALID_OR_EXPIRED);
        }
        BlogUserDO user = blogUserMapper.selectByEmail(reqVO.getEmail());
        if (user == null) {
            throw exception(USER_EMAIL_NOT_EXISTS);
        }
        blogUserMapper.updateById(BlogUserDO.builder()
                .id(user.getId())
                .password(passwordEncoder.encode(reqVO.getPassword()))
                .build());
        emailCodeCache.remove(cacheKey);
    }

    @Override
    public void verifyEmailCode(String email, String scene, String code) {
        String cacheKey = email + ":" + scene;
        String cachedCode = emailCodeCache.get(cacheKey);
        if (cachedCode == null || !cachedCode.equals(code)) {
            throw exception(AUTH_CODE_INVALID_OR_EXPIRED);
        }
        emailCodeCache.remove(cacheKey);
    }

    // ========== 社交登录（照搬 MemberAuthServiceImpl） ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AppAuthLoginRespVO socialLogin(AppSocialLoginReqVO reqVO) {
        // 使用 code 授权码，进行登录。然后，获得到绑定的用户编号
        SocialUserRespDTO socialUser = socialUserApi.getSocialUserByCode(UserTypeEnum.MEMBER.getValue(), reqVO.getType(),
                reqVO.getCode(), reqVO.getState());
        if (socialUser == null) {
            throw exception(AUTH_SOCIAL_USER_NOT_FOUND);
        }

        // 情况一：已绑定，直接读取用户信息
        BlogUserDO user;
        if (socialUser.getUserId() != null) {
            user = blogUserService.getUser(socialUser.getUserId());
        // 情况二：未绑定，注册用户 + 绑定用户
        } else {
            user = blogUserService.createUser(socialUser.getNickname(), socialUser.getAvatar(), getClientIP());
            socialUserApi.bindSocialUser(new SocialUserBindReqDTO(user.getId(), UserTypeEnum.MEMBER.getValue(),
                    reqVO.getType(), reqVO.getCode(), reqVO.getState()));
        }
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }

        // 校验是否禁用
        if (CommonStatusEnum.isDisable(user.getStatus())) {
            createLoginLog(user.getId(), user.getNickname(), LoginLogTypeEnum.LOGIN_SOCIAL, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user, user.getEmail() != null ? user.getEmail() : user.getNickname(),
                LoginLogTypeEnum.LOGIN_SOCIAL);
    }

    @Override
    public String getSocialAuthorizeUrl(Integer type, String redirectUri) {
        return socialClientApi.getAuthorizeUrl(type, UserTypeEnum.MEMBER.getValue(), redirectUri);
    }

    @Override
    public void logout(String token) {
        // 删除访问令牌
        OAuth2AccessTokenRespDTO accessTokenRespDTO = oauth2TokenApi.removeAccessToken(token);
        if (accessTokenRespDTO == null) {
            return;
        }
        // 删除成功，则记录登出日志
        createLogoutLog(accessTokenRespDTO.getUserId());
    }

    @Override
    public AppAuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenRespDTO accessTokenRespDTO = oauth2TokenApi.refreshAccessToken(refreshToken,
                OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return AuthConvert.INSTANCE.convert(accessTokenRespDTO, null);
    }

    // ========== 内部方法 ==========

    private AppAuthLoginRespVO createTokenAfterLoginSuccess(BlogUserDO user, String loginName,
                                                            LoginLogTypeEnum logType) {
        createLoginLog(user.getId(), loginName, logType, LoginResultEnum.SUCCESS);
        OAuth2AccessTokenRespDTO tokenResp = oauth2TokenApi.createAccessToken(
                new OAuth2AccessTokenCreateReqDTO()
                        .setUserId(user.getId())
                        .setUserType(UserTypeEnum.MEMBER.getValue())
                        .setClientId(OAuth2ClientConstants.CLIENT_ID_DEFAULT));
        // 更新登录信息
        blogUserService.updateUserLogin(user.getId(), getClientIP());
        return AuthConvert.INSTANCE.convert(tokenResp, null);
    }

    private void createLoginLog(Long userId, String loginName, LoginLogTypeEnum logType, LoginResultEnum loginResult) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logType.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(UserTypeEnum.MEMBER.getValue());
        reqDTO.setUsername(loginName);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogApi.createLoginLog(reqDTO);
    }

    private void createLogoutLog(Long userId) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(LoginLogTypeEnum.LOGOUT_SELF.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(UserTypeEnum.MEMBER.getValue());
        reqDTO.setUsername(getNickname(userId));
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(getClientIP());
        reqDTO.setResult(LoginResultEnum.SUCCESS.getResult());
        loginLogApi.createLoginLog(reqDTO);
    }

    private String getNickname(Long userId) {
        if (userId == null) {
            return null;
        }
        BlogUserDO user = blogUserService.getUser(userId);
        return user != null ? user.getNickname() : null;
    }

}
