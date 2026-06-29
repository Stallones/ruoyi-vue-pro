package com.sta.module.blog.controller.app.user;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.member.controller.app.user.vo.AppMemberUserUpdateReqVO;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import com.sta.module.blog.controller.app.user.vo.AppBlogUserInfoRespVO;
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
    private MemberUserService memberUserService;

    @GetMapping("/get")
    @Operation(summary = "获得基本信息")
    public CommonResult<AppBlogUserInfoRespVO> getUserInfo() {
        MemberUserDO user = memberUserService.getUser(getLoginUserId());
        return success(convertToVO(user));
    }

    @PutMapping("/update")
    @Operation(summary = "修改基本信息")
    public CommonResult<Boolean> updateUser(@RequestBody @Valid AppMemberUserUpdateReqVO reqVO) {
        memberUserService.updateUser(getLoginUserId(), reqVO);
        return success(true);
    }

    // ========== 内部方法 ==========

    private static AppBlogUserInfoRespVO convertToVO(MemberUserDO user) {
        if (user == null) {
            return null;
        }
        AppBlogUserInfoRespVO vo = new AppBlogUserInfoRespVO();
        vo.setId(user.getId());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        vo.setEmail(user.getEmail());
        vo.setSex(user.getSex());
        return vo;
    }

}
