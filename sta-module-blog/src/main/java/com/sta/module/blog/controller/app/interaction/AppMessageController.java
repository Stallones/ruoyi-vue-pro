package com.sta.module.blog.controller.app.interaction;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.ip.core.Area;
import cn.iocoder.yudao.framework.ip.core.utils.IPUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.dal.dataobject.user.BlogUserDO;
import com.sta.module.blog.service.user.BlogUserService;
import com.sta.module.blog.controller.app.interaction.vo.AppMessageCreateReqVO;
import com.sta.module.blog.controller.app.interaction.vo.AppMessageRespVO;
import com.sta.module.blog.dal.dataobject.interaction.LikeDO;
import com.sta.module.blog.dal.dataobject.interaction.MessageDO;
import com.sta.module.blog.dal.mysql.interaction.LikeMapper;
import com.sta.module.blog.dal.mysql.interaction.MessageMapper;
import com.sta.module.blog.enums.TypeEnum;
import com.sta.module.blog.service.interaction.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getUserAgent;

@Tag(name = "用户 APP - 博客留言")
@RestController
@RequestMapping("/blog/message")
@Validated
public class AppMessageController {

    @Resource
    private MessageService messageService;

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private MessageMapper messageMapper;

    @Resource
    private LikeMapper likeMapper;

    @PostMapping("/create")
    @Operation(summary = "创建留言/回复")
    public CommonResult<Long> createMessage(@Valid @RequestBody AppMessageCreateReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();

        // 解析 IP 属地
        String ip = getClientIP();
        String ipLocation = "";
        try {
            Area area = IPUtils.getArea(ip);
            if (area != null) {
                Area province = area.getParent();
                ipLocation = (province != null) ? province.getName() : area.getName();
            }
        } catch (Exception ignored) {}

        // 解析 UA
        String browser = "";
        String os = "";
        try {
            String uaStr = getUserAgent();
            UserAgent ua = UserAgentUtil.parse(uaStr);
            if (ua != null) {
                browser = ua.getBrowser() != null
                        ? ua.getBrowser().getName() + " " + ua.getVersion() : "";
                os = ua.getPlatform() != null
                        ? ua.getPlatform().getName() : "";
            }
        } catch (Exception ignored) {}

        return success(messageService.createMessage(userId, reqVO.getContent(), reqVO.getParentId(),
                reqVO.getToUserId(), ipLocation, browser, os));
    }

    @GetMapping("/page")
    @PermitAll
    @Operation(summary = "获得留言分页（顶级留言，type=30）")
    public CommonResult<PageResult<AppMessageRespVO>> getMessagePage(
            @RequestParam(value = "orderBy", defaultValue = "newest") String orderBy,
            @Valid PageParam pageParam) {
        // 1. 查询顶级留言分页
        PageResult<MessageDO> pageResult = messageService.getTopMessagePage(pageParam, orderBy);
        List<MessageDO> messages = pageResult.getList();
        if (CollUtil.isEmpty(messages)) {
            return success(new PageResult<>(Collections.emptyList(), pageResult.getTotal()));
        }

        // 2. 转为 VO
        List<AppMessageRespVO> voList = BeanUtils.toBean(messages, AppMessageRespVO.class);
        List<Long> messageIds = messages.stream().map(MessageDO::getId).collect(Collectors.toList());

        // 3. 批量填充 replyCount
        fillReplyCount(messageIds, voList);

        // 4. 批量填充 likeCount + isLiked
        fillLikeInfo(messageIds, voList);

        // 5. 批量填充用户元数据
        fillUserMetadata(voList);

        return success(new PageResult<>(voList, pageResult.getTotal()));
    }

    @GetMapping("/reply-page")
    @PermitAll
    @Operation(summary = "获得留言回复分页（type=31，按 rootId）")
    @Parameter(name = "rootId", description = "根留言ID", required = true)
    public CommonResult<PageResult<AppMessageRespVO>> getReplyPage(
            @RequestParam("rootId") Long rootId,
            @Valid PageParam pageParam) {
        // 1. 查询回复分页
        PageResult<MessageDO> pageResult = messageService.getReplyPage(rootId, pageParam);
        List<MessageDO> replies = pageResult.getList();
        if (CollUtil.isEmpty(replies)) {
            return success(new PageResult<>(Collections.emptyList(), pageResult.getTotal()));
        }

        // 2. 转为 VO
        List<AppMessageRespVO> voList = BeanUtils.toBean(replies, AppMessageRespVO.class);
        List<Long> replyIds = replies.stream().map(MessageDO::getId).collect(Collectors.toList());

        // 3. 批量填充 likeCount + isLiked
        fillLikeInfo(replyIds, voList);

        // 4. 批量填充用户元数据
        fillUserMetadata(voList);

        return success(new PageResult<>(voList, pageResult.getTotal()));
    }

    @GetMapping("/tree")
    @PermitAll
    @Operation(summary = "获得留言树（嵌套结构）")
    public CommonResult<List<AppMessageRespVO>> getMessageTree() {
        List<MessageDO> allMessages = messageService.getMessageTree();
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

    // ======================== 私有方法 ========================

    /**
     * 批量填充 replyCount
     */
    private void fillReplyCount(List<Long> rootIds, List<AppMessageRespVO> voList) {
        List<MessageDO> allReplies = messageMapper.selectRepliesByRootIds(rootIds);
        Map<Long, Long> replyCountMap = allReplies.stream()
                .collect(Collectors.groupingBy(MessageDO::getRootId, Collectors.counting()));
        for (AppMessageRespVO vo : voList) {
            vo.setReplyCount(replyCountMap.getOrDefault(vo.getId(), 0L));
        }
    }

    /**
     * 批量填充 likeCount + isLiked
     */
    private void fillLikeInfo(List<Long> dataIds, List<AppMessageRespVO> voList) {
        List<LikeDO> allLikes = likeMapper.selectListByTypeAndDataIds(TypeEnum.MSG, dataIds);
        Map<Long, Long> likeCountMap = allLikes.stream()
                .collect(Collectors.groupingBy(LikeDO::getDataId, Collectors.counting()));

        Long currentUserId = SecurityFrameworkUtils.getLoginUserId();
        Set<Long> likedIds = new HashSet<>();
        if (currentUserId != null) {
            List<LikeDO> userLikes = likeMapper.selectUserLikesByTypeAndDataIds(TypeEnum.MSG, dataIds, currentUserId);
            likedIds = userLikes.stream().map(LikeDO::getDataId).collect(Collectors.toSet());
        }

        for (AppMessageRespVO vo : voList) {
            vo.setLikeCount(likeCountMap.getOrDefault(vo.getId(), 0L));
            vo.setIsLiked(likedIds.contains(vo.getId()));
        }
    }

    /**
     * 批量补全留言中的用户昵称、头像、被回复用户昵称
     */
    private void fillUserMetadata(List<AppMessageRespVO> allVos) {
        Set<Long> userIds = new HashSet<>();
        for (AppMessageRespVO vo : allVos) {
            if (vo.getUserId() != null) userIds.add(vo.getUserId());
            if (vo.getToUserId() != null && vo.getToUserId() > 0) userIds.add(vo.getToUserId());
        }
        if (userIds.isEmpty()) return;

        Map<Long, BlogUserDO> userMap = blogUserService.getUserList(userIds).stream()
                .collect(Collectors.toMap(BlogUserDO::getId, u -> u, (a, b) -> a));

        for (AppMessageRespVO vo : allVos) {
            BlogUserDO user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            } else {
                vo.setUserNickname("已注销用户");
                vo.setUserAvatar("");
            }
            if (vo.getToUserId() != null && vo.getToUserId() > 0) {
                BlogUserDO toUser = userMap.get(vo.getToUserId());
                if (toUser != null) {
                    vo.setToUserNickname(toUser.getNickname());
                } else {
                    vo.setToUserNickname("已注销用户");
                }
            }
        }
    }

    /**
     * 将平铺的 MessageDO 列表构建为嵌套树 VO（兼容旧 tree 接口）
     */
    private List<AppMessageRespVO> buildMessageTreeVO(List<MessageDO> allMessages) {
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

        fillUserMetadata(allVos);

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
