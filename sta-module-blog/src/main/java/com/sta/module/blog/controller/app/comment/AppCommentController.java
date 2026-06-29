package com.sta.module.blog.controller.app.comment;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.module.member.dal.dataobject.user.MemberUserDO;
import cn.iocoder.yudao.module.member.service.user.MemberUserService;
import com.sta.module.blog.controller.app.comment.vo.AppCommentCreateReqVO;
import com.sta.module.blog.controller.app.comment.vo.AppCommentRespVO;
import com.sta.module.blog.dal.dataobject.BlogCommentDO;
import com.sta.module.blog.service.BlogCommentService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客评论")
@RestController
@RequestMapping("/blog/comment")
@Validated
public class AppCommentController {

    @Resource
    private BlogCommentService commentService;

    @Resource
    private MemberUserService memberUserService;

    @PostMapping("/create")
    @Operation(summary = "创建评论/回复")
    public CommonResult<Long> createComment(@Valid @RequestBody AppCommentCreateReqVO reqVO) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return success(commentService.createComment(userId, reqVO.getArticleId(), reqVO.getParentId(), reqVO.getContent(), reqVO.getToUserId()));
    }

    @GetMapping("/page")
    @PermitAll
    @Operation(summary = "获得文章评论分页")
    @Parameter(name = "articleId", description = "文章ID", required = true)
    public CommonResult<PageResult<AppCommentRespVO>> getCommentPage(
            @RequestParam("articleId") Long articleId,
            @Valid PageParam pageParam) {
        PageResult<BlogCommentDO> pageResult = commentService.getCommentPageByArticleId(articleId, pageParam);
        return success(BeanUtils.toBean(pageResult, AppCommentRespVO.class));
    }

    @GetMapping("/tree")
    @PermitAll
    @Operation(summary = "获得文章评论树（嵌套结构）")
    @Parameter(name = "articleId", description = "文章ID", required = true)
    public CommonResult<List<AppCommentRespVO>> getCommentTree(
            @RequestParam("articleId") Long articleId) {
        List<BlogCommentDO> allComments = commentService.getCommentTree(articleId);
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

    /**
     * 将平铺的 BlogCommentDO 列表构建为嵌套树 VO，并批量补全用户元数据
     */
    private List<AppCommentRespVO> buildCommentTreeVO(List<BlogCommentDO> allComments) {
        if (CollUtil.isEmpty(allComments)) {
            return Collections.emptyList();
        }
        // 先全部转为 VO
        List<AppCommentRespVO> allVos = allComments.stream()
                .map(comment -> {
                    AppCommentRespVO vo = BeanUtils.toBean(comment, AppCommentRespVO.class);
                    vo.setReplies(new ArrayList<>());
                    return vo;
                })
                .collect(Collectors.toList());

        // 批量查询用户元数据
        fillUserMetadata(allVos);

        // 按 id 索引
        Map<Long, AppCommentRespVO> voMap = allVos.stream()
                .collect(Collectors.toMap(AppCommentRespVO::getId, v -> v));

        // 组装树
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

    /**
     * 批量补全评论中的用户昵称、头像、被回复用户昵称
     */
    private void fillUserMetadata(List<AppCommentRespVO> allVos) {
        // 收集所有 userId 和 toUserId
        Set<Long> userIds = new HashSet<>();
        for (AppCommentRespVO vo : allVos) {
            if (vo.getUserId() != null) userIds.add(vo.getUserId());
            if (vo.getToUserId() != null && vo.getToUserId() > 0) userIds.add(vo.getToUserId());
        }
        if (userIds.isEmpty()) return;

        // 批量查询并构建 Map
        Map<Long, MemberUserDO> userMap = memberUserService.getUserList(userIds).stream()
                .collect(Collectors.toMap(MemberUserDO::getId, u -> u, (a, b) -> a));

        // 回填用户元数据
        for (AppCommentRespVO vo : allVos) {
            MemberUserDO user = userMap.get(vo.getUserId());
            if (user != null) {
                vo.setUserNickname(user.getNickname());
                vo.setUserAvatar(user.getAvatar());
            }
            if (vo.getToUserId() != null && vo.getToUserId() > 0) {
                MemberUserDO toUser = userMap.get(vo.getToUserId());
                if (toUser != null) {
                    vo.setToUserNickname(toUser.getNickname());
                }
            }
        }
    }

}
