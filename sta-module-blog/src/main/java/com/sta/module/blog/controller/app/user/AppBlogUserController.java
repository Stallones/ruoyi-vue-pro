package com.sta.module.blog.controller.app.user;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.sta.module.blog.controller.app.user.vo.*;
import com.sta.module.blog.dal.dataobject.user.BlogUserDO;
import com.sta.module.blog.service.user.BlogAuthService;
import com.sta.module.blog.service.user.BlogUserService;
import org.springframework.util.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.sta.module.blog.enums.ErrorCodeConstants.USER_EMAIL_USED;

@Tag(name = "用户 APP - 博客用户信息")
@RestController
@RequestMapping("/blog/user")
@Validated
public class AppBlogUserController {

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private BlogAuthService blogAuthService;

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    public CommonResult<AppUserInfoRespVO> getUserInfo() {
        BlogUserDO user = blogUserService.getUser(getLoginUserId());
        return success(convertToVO(user));
    }

    @PutMapping("/update")
    @Operation(summary = "修改基本信息")
    public CommonResult<Boolean> updateUser(@RequestBody @Valid AppUserUpdateReqVO reqVO) {
        Long userId = getLoginUserId();
        // 如果修改了邮箱，需要验证验证码
        if (StringUtils.hasText(reqVO.getEmail())) {
            BlogUserDO currentUser = blogUserService.getUser(userId);
            if (currentUser != null && !reqVO.getEmail().equals(currentUser.getEmail())) {
                // 校验邮箱是否已被其他用户注册
                BlogUserDO existingUser = blogUserService.getUserByEmail(reqVO.getEmail());
                if (existingUser != null && !existingUser.getId().equals(userId)) {
                    throw exception(USER_EMAIL_USED);
                }
                blogAuthService.verifyEmailCode(reqVO.getEmail(), "resetEmail", reqVO.getCode());
            }
        }
        blogUserService.updateUser(userId, reqVO);
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "修改密码")
    public CommonResult<Boolean> updatePassword(@RequestBody @Valid AppUserUpdatePasswordReqVO reqVO) {
        blogUserService.changePassword(getLoginUserId(), reqVO.getOldPassword(), reqVO.getNewPassword());
        return success(true);
    }

    // ========== 内部方法 ==========

    private static AppUserInfoRespVO convertToVO(BlogUserDO user) {
        if (user == null) {
            return null;
        }
        AppUserInfoRespVO vo = new AppUserInfoRespVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setEmail(user.getEmail());
        vo.setSex(user.getSex());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }

}
