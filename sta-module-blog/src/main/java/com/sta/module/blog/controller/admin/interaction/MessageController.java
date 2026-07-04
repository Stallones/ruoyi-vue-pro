package com.sta.module.blog.controller.admin.interaction;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.interaction.vo.MessagePageReqVO;
import com.sta.module.blog.controller.admin.interaction.vo.MessageRespVO;
import com.sta.module.blog.dal.dataobject.interaction.MessageDO;
import com.sta.module.blog.service.interaction.MessageService;
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
public class MessageController {

    @Resource
    private MessageService messageService;

    @GetMapping("/page")
    @Operation(summary = "获得留言分页")
    @PreAuthorize("@ss.hasPermission('blog:message:query')")
    public CommonResult<PageResult<MessageRespVO>> getMessagePage(@Valid MessagePageReqVO pageReqVO) {
        PageResult<MessageDO> pageResult = messageService.getMessagePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MessageRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得留言")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('blog:message:query')")
    public CommonResult<MessageRespVO> getMessage(@RequestParam("id") Long id) {
        MessageDO message = messageService.getMessage(id);
        return success(BeanUtils.toBean(message, MessageRespVO.class));
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
