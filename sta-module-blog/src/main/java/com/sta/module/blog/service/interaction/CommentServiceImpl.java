package com.sta.module.blog.service.interaction;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.interaction.vo.CommentPageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.CommentDO;
import com.sta.module.blog.dal.mysql.interaction.CommentMapper;
import com.sta.module.blog.enums.TypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.COMMENT_NOT_EXISTS;

@Service
@Validated
public class CommentServiceImpl implements CommentService {

    @Resource
    private CommentMapper commentMapper;

    @Override
    public PageResult<CommentDO> getCommentPage(CommentPageReqVO pageReqVO) {
        return commentMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateCommentCheck(Long id, Integer isCheck) {
        validateCommentExists(id);
        commentMapper.updateById(CommentDO.builder().id(id).status(isCheck).build());
    }

    @Override
    public void deleteComment(Long id) {
        validateCommentExists(id);
        commentMapper.deleteById(id);
    }

    @Override
    public CommentDO getComment(Long id) {
        return commentMapper.selectById(id);
    }

    @Override
    public Long createComment(Long userId, Long articleId, Long parentId, String content, Long toUserId,
                              String ipLocation, String browser, String os) {
        TypeEnum type = (parentId == null || parentId == 0) ? TypeEnum.CMT : TypeEnum.CMT_RE;

        // 计算 rootId
        Long rootId = 0L;
        if (parentId != null && parentId > 0) {
            CommentDO parent = commentMapper.selectById(parentId);
            if (parent != null) {
                rootId = (parent.getRootId() != null && parent.getRootId() > 0)
                        ? parent.getRootId()   // 回复回复：继承父级的 rootId
                        : parent.getId();       // 回复顶级评论：rootId = 父评论 ID
            }
        }

        CommentDO comment = CommentDO.builder()
                .type(type)
                .articleId(articleId)
                .parentId(parentId != null ? parentId : 0L)
                .rootId(rootId)
                .content(content)
                .userId(userId)
                .toUserId(toUserId != null ? toUserId : 0L)
                .status(0)
                .ipLocation(ipLocation)
                .browser(browser)
                .os(os)
                .build();
        commentMapper.insert(comment);
        return comment.getId();
    }

    @Override
    public PageResult<CommentDO> getCommentPageByArticleId(Long articleId, PageParam pageParam) {
        return commentMapper.selectCommentPageByArticleId(articleId, pageParam);
    }

    @Override
    public List<CommentDO> getCommentTree(Long articleId) {
        List<CommentDO> allComments = commentMapper.selectTreeListByArticleId(articleId);
        return buildTree(allComments);
    }

    @Override
    public PageResult<CommentDO> getTopCommentPage(Long articleId, PageParam pageParam, String orderBy) {
        return commentMapper.selectTopCommentPage(articleId, pageParam, orderBy);
    }

    @Override
    public PageResult<CommentDO> getReplyPage(Long rootId, PageParam pageParam) {
        return commentMapper.selectReplyPageByRootId(rootId, pageParam);
    }

    @Override
    public Long getCommentCount(Long articleId) {
        return commentMapper.selectCountByArticleId(articleId);
    }

    /**
     * 将平铺列表组装为嵌套树（返回顶级评论，树结构由 Controller 层转换为 VO 时处理）
     */
    private List<CommentDO> buildTree(List<CommentDO> allComments) {
        if (CollUtil.isEmpty(allComments)) {
            return Collections.emptyList();
        }
        // 找出顶级评论（parentId == 0）
        return allComments.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0)
                .collect(java.util.stream.Collectors.toList());
    }

    private void validateCommentExists(Long id) {
        if (commentMapper.selectById(id) == null) {
            throw exception(COMMENT_NOT_EXISTS);
        }
    }

}
