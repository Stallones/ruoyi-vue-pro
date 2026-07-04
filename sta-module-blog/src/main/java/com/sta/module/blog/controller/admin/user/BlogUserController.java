package com.sta.module.blog.controller.admin.user;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.user.vo.UserPageReqVO;
import com.sta.module.blog.controller.admin.user.vo.UserRespVO;
import com.sta.module.blog.controller.admin.user.vo.UserSaveReqVO;
import com.sta.module.blog.dal.dataobject.user.BlogUserDO;
import com.sta.module.blog.dal.mysql.user.BlogUserMapper;
import com.sta.module.blog.service.user.BlogUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;

@Tag(name = "管理后台 - 博客用户")
@RestController
@RequestMapping("/blog/user")
@Validated
public class BlogUserController {

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private BlogUserMapper blogUserMapper;

    @PostMapping("/create")
    @Operation(summary = "创建用户")
    @PreAuthorize("@ss.hasPermission('blog:user:create')")
    public CommonResult<Long> createUser(@Validated(UserSaveReqVO.Create.class) @RequestBody UserSaveReqVO reqVO) {
        // 校验邮箱唯一
        if (blogUserMapper.selectByEmail(reqVO.getEmail()) != null) {
            throw cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil
                    .exception(com.sta.module.blog.enums.ErrorCodeConstants.USER_EMAIL_USED, reqVO.getEmail());
        }
        BlogUserDO user = new BlogUserDO();
        user.setEmail(reqVO.getEmail());
        user.setNickname(reqVO.getNickname());
        user.setAvatar(reqVO.getAvatar());
        user.setSex(reqVO.getSex());
        user.setPassword(blogUserService.encodePassword(reqVO.getPassword()));
        user.setStatus(reqVO.getStatus() != null ? reqVO.getStatus() : CommonStatusEnum.ENABLE.getStatus());
        user.setRegisterIp(getClientIP());
        blogUserMapper.insert(user);
        return success(user.getId());
    }

    @PutMapping("/update")
    @Operation(summary = "更新用户")
    @PreAuthorize("@ss.hasPermission('blog:user:update')")
    public CommonResult<Boolean> updateUser(@Validated(UserSaveReqVO.Update.class) @RequestBody UserSaveReqVO reqVO) {
        BlogUserDO updateObj = new BlogUserDO().setId(reqVO.getId());
        updateObj.setEmail(reqVO.getEmail());
        updateObj.setNickname(reqVO.getNickname());
        updateObj.setAvatar(reqVO.getAvatar());
        updateObj.setSex(reqVO.getSex());
        updateObj.setStatus(reqVO.getStatus());
        // 密码不为空则更新
        if (reqVO.getPassword() != null && !reqVO.getPassword().isEmpty()) {
            updateObj.setPassword(blogUserService.encodePassword(reqVO.getPassword()));
        }
        blogUserMapper.updateById(updateObj);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户")
    @Parameter(name = "id", description = "用户编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:user:delete')")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") Long id) {
        blogUserService.deleteUser(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('blog:user:query')")
    public CommonResult<UserRespVO> getUser(@RequestParam("id") Long id) {
        BlogUserDO user = blogUserService.getUser(id);
        return success(BeanUtils.toBean(user, UserRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户分页")
    @PreAuthorize("@ss.hasPermission('blog:user:query')")
    public CommonResult<PageResult<UserRespVO>> getUserPage(@Valid UserPageReqVO pageReqVO) {
        PageResult<BlogUserDO> pageResult = blogUserService.getUserPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UserRespVO.class));
    }

    @PutMapping("/update-status")
    @Operation(summary = "更新用户状态")
    @PreAuthorize("@ss.hasPermission('blog:user:update')")
    public CommonResult<Boolean> updateUserStatus(@RequestParam("id") Long id,
                                                   @RequestParam("status") Integer status) {
        blogUserService.updateUserStatus(id, status);
        return success(true);
    }

}
