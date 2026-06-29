package com.sta.module.blog.controller.app.message;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.controller.app.message.vo.AppMessageCreateReqVO;
import com.sta.module.blog.controller.app.message.vo.AppMessageRespVO;
import com.sta.module.blog.dal.dataobject.BlogMessageDO;
import com.sta.module.blog.service.BlogMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客留言")
@RestController
@RequestMapping("/blog/message")
@Validated
public class AppMessageController {

    @Resource
    private BlogMessageService messageService;

    @PostMapping("/create")
    @Operation(summary = "创建留言/回复")
    public CommonResult<Long> createMessage(@Valid @RequestBody AppMessageCreateReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(messageService.createMessage(userId, reqVO.getContent(), reqVO.getParentId(), reqVO.getToUserId()));
    }

    @GetMapping("/page")
    @PermitAll
    @Operation(summary = "获得留言分页")
    public CommonResult<PageResult<AppMessageRespVO>> getMessagePage(@Valid PageParam pageParam) {
        PageResult<BlogMessageDO> pageResult = messageService.getMessagePageByStatus(pageParam);
        return success(BeanUtils.toBean(pageResult, AppMessageRespVO.class));
    }

    @GetMapping("/tree")
    @PermitAll
    @Operation(summary = "获得留言树（嵌套结构）")
    public CommonResult<List<AppMessageRespVO>> getMessageTree() {
        List<BlogMessageDO> allMessages = messageService.getMessageTree();
        List<AppMessageRespVO> voTree = buildMessageTreeVO(allMessages);
        return success(voTree);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除自己的留言")
    @Parameter(name = "id", description = "留言编号", required = true)
    public CommonResult<Boolean> deleteMessage(@RequestParam("id") Long id) {
        messageService.deleteMessage(id);
        return success(true);
    }

    /**
     * 将平铺的 BlogMessageDO 列表构建为嵌套树 VO
     */
    private List<AppMessageRespVO> buildMessageTreeVO(List<BlogMessageDO> allMessages) {
        if (CollUtil.isEmpty(allMessages)) {
            return Collections.emptyList();
        }
        List<AppMessageRespVO> allVos = allMessages.stream()
                .map(message -> {
                    AppMessageRespVO vo = BeanUtils.toBean(message, AppMessageRespVO.class);
                    vo.setReplies(new ArrayList<>());
                    return vo;
                })
                .collect(Collectors.toList());

        Map<Long, AppMessageRespVO> voMap = allVos.stream()
                .collect(Collectors.toMap(AppMessageRespVO::getId, v -> v));

        List<AppMessageRespVO> roots = new ArrayList<>();
        for (AppMessageRespVO vo : allVos) {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                roots.add(vo);
            } else {
                AppMessageRespVO parent = voMap.get(vo.getParentId());
                if (parent != null) {
                    parent.getReplies().add(vo);
                }
            }
        }
        return roots;
    }

}
