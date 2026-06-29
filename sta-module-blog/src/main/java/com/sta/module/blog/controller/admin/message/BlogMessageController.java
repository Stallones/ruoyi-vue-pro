package com.sta.module.blog.controller.admin.message;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.message.vo.BlogMessagePageReqVO;
import com.sta.module.blog.controller.admin.message.vo.BlogMessageRespVO;
import com.sta.module.blog.dal.dataobject.BlogMessageDO;
import com.sta.module.blog.service.BlogMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客留言")
@RestController
@RequestMapping("/blog/message")
@Validated
public class BlogMessageController {

    @Resource
    private BlogMessageService messageService;

    @GetMapping("/page")
    @Operation(summary = "获得留言分页")
    @PreAuthorize("@ss.hasPermission('blog:message:query')")
    public CommonResult<PageResult<BlogMessageRespVO>> getMessagePage(@Valid BlogMessagePageReqVO pageReqVO) {
        PageResult<BlogMessageDO> pageResult = messageService.getMessagePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BlogMessageRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得留言")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('blog:message:query')")
    public CommonResult<BlogMessageRespVO> getMessage(@RequestParam("id") Long id) {
        BlogMessageDO message = messageService.getMessage(id);
        return success(BeanUtils.toBean(message, BlogMessageRespVO.class));
    }

    @PutMapping("/update-check")
    @Operation(summary = "更新留言审核状态")
    @PreAuthorize("@ss.hasPermission('blog:message:update')")
    public CommonResult<Boolean> updateMessageCheck(@RequestParam("id") Long id,
                                                     @RequestParam("isCheck") Integer isCheck) {
        messageService.updateMessageCheck(id, isCheck);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除留言")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:message:delete')")
    public CommonResult<Boolean> deleteMessage(@RequestParam("id") Long id) {
        messageService.deleteMessage(id);
        return success(true);
    }

}
