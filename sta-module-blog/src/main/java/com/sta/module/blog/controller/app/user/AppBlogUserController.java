package com.sta.module.blog.controller.app.user;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.sta.module.blog.controller.app.user.vo.AppUserInfoRespVO;
import com.sta.module.blog.controller.app.user.vo.AppUserUpdateReqVO;
import com.sta.module.blog.dal.dataobject.user.BlogUserDO;
import com.sta.module.blog.service.user.BlogUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "用户 APP - 博客用户信息")
@RestController
@RequestMapping("/blog/user")
@Validated
public class AppBlogUserController {

    @Resource
    private BlogUserService blogUserService;

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    public CommonResult<AppUserInfoRespVO> getUserInfo() {
        BlogUserDO user = blogUserService.getUser(getLoginUserId());
        return success(convertToVO(user));
    }

    @PutMapping("/update")
    @Operation(summary = "修改基本信息")
    public CommonResult<Boolean> updateUser(@RequestBody @Valid AppUserUpdateReqVO reqVO) {
        blogUserService.updateUser(getLoginUserId(), reqVO);
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
        return vo;
    }

}
