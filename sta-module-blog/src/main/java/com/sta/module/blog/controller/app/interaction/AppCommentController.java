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
import com.sta.module.blog.controller.app.interaction.vo.AppCommentCreateReqVO;
import com.sta.module.blog.controller.app.interaction.vo.AppCommentRespVO;
import com.sta.module.blog.dal.dataobject.interaction.CommentDO;
import com.sta.module.blog.dal.dataobject.interaction.LikeDO;
import com.sta.module.blog.dal.mysql.interaction.CommentMapper;
import com.sta.module.blog.dal.mysql.interaction.LikeMapper;
import com.sta.module.blog.enums.TypeEnum;
import com.sta.module.blog.service.interaction.CommentService;
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

@Tag(name = "用户 APP - 博客评论")
@RestController
@RequestMapping("/blog/comment")
@Validated
public class AppCommentController {

    @Resource
    private CommentService commentService;

    @Resource
    private BlogUserService blogUserService;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private LikeMapper likeMapper;

    @PostMapping("/create")
    @Operation(summary = "创建评论/回复")
    public CommonResult<Long> createComment(@Valid @RequestBody AppCommentCreateReqVO reqVO) {
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

        return success(commentService.createComment(userId, reqVO.getArticleId(), reqVO.getParentId(),
                reqVO.getContent(), reqVO.getToUserId(), ipLocation, browser, os));
    }

    @GetMapping("/page")
    @PermitAll
    @Operation(summary = "获得文章评论分页（顶级评论，type=20）")
    @Parameter(name = "articleId", description = "文章ID", required = true)
    public CommonResult<PageResult<AppCommentRespVO>> getCommentPage(
            @RequestParam("articleId") Long articleId,
            @RequestParam(value = "orderBy", defaultValue = "newest") String orderBy,
            @Valid PageParam pageParam) {
        // 1. 查询顶级评论分页
        PageResult<CommentDO> pageResult = commentService.getTopCommentPage(articleId, pageParam, orderBy);
        List<CommentDO> comments = pageResult.getList();
        if (CollUtil.isEmpty(comments)) {
            return success(new PageResult<>(Collections.emptyList(), pageResult.getTotal()));
        }

        // 2. 转为 VO
        List<AppCommentRespVO> voList = BeanUtils.toBean(comments, AppCommentRespVO.class);
        List<Long> commentIds = comments.stream().map(CommentDO::getId).collect(Collectors.toList());

        // 3. 批量填充 replyCount
        fillReplyCount(commentIds, voList);

        // 4. 批量填充 likeCount + isLiked
        fillLikeInfo(commentIds, voList);

        // 5. 批量填充用户元数据
        fillUserMetadata(voList);

        return success(new PageResult<>(voList, pageResult.getTotal()));
    }

    @GetMapping("/reply-page")
    @PermitAll
    @Operation(summary = "获得评论回复分页（type=21，按 rootId）")
    @Parameter(name = "rootId", description = "根评论ID", required = true)
    public CommonResult<PageResult<AppCommentRespVO>> getReplyPage(
            @RequestParam("rootId") Long rootId,
            @Valid PageParam pageParam) {
        // 1. 查询回复分页
        PageResult<CommentDO> pageResult = commentService.getReplyPage(rootId, pageParam);
        List<CommentDO> replies = pageResult.getList();
        if (CollUtil.isEmpty(replies)) {
            return success(new PageResult<>(Collections.emptyList(), pageResult.getTotal()));
        }

        // 2. 转为 VO
        List<AppCommentRespVO> voList = BeanUtils.toBean(replies, AppCommentRespVO.class);
        List<Long> replyIds = replies.stream().map(CommentDO::getId).collect(Collectors.toList());

        // 3. 批量填充 likeCount + isLiked
        fillLikeInfo(replyIds, voList);

        // 4. 批量填充用户元数据
        fillUserMetadata(voList);

        return success(new PageResult<>(voList, pageResult.getTotal()));
    }

    @GetMapping("/count")
    @PermitAll
    @Operation(summary = "获得文章评论总数（仅顶级评论）")
    @Parameter(name = "articleId", description = "文章ID", required = true)
    public CommonResult<Long> getCommentCount(@RequestParam("articleId") Long articleId) {
        return success(commentService.getCommentCount(articleId));
    }

    @GetMapping("/tree")
    @PermitAll
    @Operation(summary = "获得文章评论树（嵌套结构）")
    @Parameter(name = "articleId", description = "文章ID", required = true)
    public CommonResult<List<AppCommentRespVO>> getCommentTree(
            @RequestParam("articleId") Long articleId) {
        List<CommentDO> allComments = commentService.getCommentTree(articleId);
        List<AppCommentRespVO> voTree = buildCommentTreeVO(allComments);
        return success(voTree);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除自己的评论")
    @Parameter(name = "id", description = "评论编号", required = true)
    public CommonResult<Boolean> deleteComment(@RequestParam("id") Long id) {
        commentService.deleteComment(id);
        return success(true);
    }

    // ======================== 私有方法 ========================

    /**
     * 批量填充 replyCount（通过批量查询回复，按 rootId 分组计数）
     */
    private void fillReplyCount(List<Long> rootIds, List<AppCommentRespVO> voList) {
        List<CommentDO> allReplies = commentMapper.selectRepliesByRootIds(rootIds);
        Map<Long, Long> replyCountMap = allReplies.stream()
                .collect(Collectors.groupingBy(CommentDO::getRootId, Collectors.counting()));
        for (AppCommentRespVO vo : voList) {
            vo.setReplyCount(replyCountMap.getOrDefault(vo.getId(), 0L));
        }
    }

    /**
     * 批量填充 likeCount + isLiked
     */
    private void fillLikeInfo(List<Long> dataIds, List<AppCommentRespVO> voList) {
        // likeCount: 批量查询所有有效点赞，按 dataId 分组计数
        List<LikeDO> allLikes = likeMapper.selectListByTypeAndDataIds(TypeEnum.CMT, dataIds);
        Map<Long, Long> likeCountMap = allLikes.stream()
                .collect(Collectors.groupingBy(LikeDO::getDataId, Collectors.counting()));

        // isLiked: 当前登录用户的点赞状态
        Long currentUserId = SecurityFrameworkUtils.getLoginUserId();
        Set<Long> likedIds = new HashSet<>();
        if (currentUserId != null) {
            List<LikeDO> userLikes = likeMapper.selectUserLikesByTypeAndDataIds(TypeEnum.CMT, dataIds, currentUserId);
            likedIds = userLikes.stream().map(LikeDO::getDataId).collect(Collectors.toSet());
        }

        for (AppCommentRespVO vo : voList) {
            vo.setLikeCount(likeCountMap.getOrDefault(vo.getId(), 0L));
            vo.setIsLiked(likedIds.contains(vo.getId()));
        }
    }

    /**
     * 批量补全评论中的用户昵称、头像、被回复用户昵称
     */
    private void fillUserMetadata(List<AppCommentRespVO> allVos) {
        Set<Long> userIds = new HashSet<>();
        for (AppCommentRespVO vo : allVos) {
            if (vo.getUserId() != null) userIds.add(vo.getUserId());
            if (vo.getToUserId() != null && vo.getToUserId() > 0) userIds.add(vo.getToUserId());
        }
        if (userIds.isEmpty()) return;

        Map<Long, BlogUserDO> userMap = blogUserService.getUserList(userIds).stream()
                .collect(Collectors.toMap(BlogUserDO::getId, u -> u, (a, b) -> a));

        for (AppCommentRespVO vo : allVos) {
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
     * 将平铺的 CommentDO 列表构建为嵌套树 VO（兼容旧 tree 接口）
     */
    private List<AppCommentRespVO> buildCommentTreeVO(List<CommentDO> allComments) {
        if (CollUtil.isEmpty(allComments)) {
            return Collections.emptyList();
        }
        List<AppCommentRespVO> allVos = allComments.stream()
                .map(comment -> {
                    AppCommentRespVO vo = BeanUtils.toBean(comment, AppCommentRespVO.class);
                    vo.setReplies(new ArrayList<>());
                    return vo;
                })
                .collect(Collectors.toList());

        fillUserMetadata(allVos);

        Map<Long, AppCommentRespVO> voMap = allVos.stream()
                .collect(Collectors.toMap(AppCommentRespVO::getId, v -> v));

        List<AppCommentRespVO> roots = new ArrayList<>();
        for (AppCommentRespVO vo : allVos) {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                roots.add(vo);
            } else {
                AppCommentRespVO parent = voMap.get(vo.getParentId());
                if (parent != null) {
                    parent.getReplies().add(vo);
                }
            }
        }
        return roots;
    }

}
