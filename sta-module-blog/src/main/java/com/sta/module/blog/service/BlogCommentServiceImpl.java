package com.sta.module.blog.service;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.comment.vo.BlogCommentPageReqVO;
import com.sta.module.blog.dal.dataobject.BlogCommentDO;
import com.sta.module.blog.dal.mysql.BlogCommentMapper;
import com.sta.module.blog.enums.BlogTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.COMMENT_NOT_EXISTS;

@Service
@Validated
public class BlogCommentServiceImpl implements BlogCommentService {

    @Resource
    private BlogCommentMapper commentMapper;

    @Override
    public PageResult<BlogCommentDO> getCommentPage(BlogCommentPageReqVO pageReqVO) {
        return commentMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateCommentCheck(Long id, Integer isCheck) {
        validateCommentExists(id);
        commentMapper.updateById(BlogCommentDO.builder().id(id).status(isCheck).build());
    }

    @Override
    public void deleteComment(Long id) {
        validateCommentExists(id);
        commentMapper.deleteById(id);
    }

    @Override
    public BlogCommentDO getComment(Long id) {
        return commentMapper.selectById(id);
    }

    @Override
    public Long createComment(Long userId, Long articleId, Long parentId, String content, Long toUserId) {
        BlogTypeEnum type = (parentId == null || parentId == 0) ? BlogTypeEnum.CMT : BlogTypeEnum.CMT_RE;
        BlogCommentDO comment = BlogCommentDO.builder()
                .type(type)
                .articleId(articleId)
                .parentId(parentId != null ? parentId : 0L)
                .content(content)
                .userId(userId)
                .toUserId(toUserId != null ? toUserId : 0L)
                .status(0)
                .build();
        commentMapper.insert(comment);
        return comment.getId();
    }

    @Override
    public PageResult<BlogCommentDO> getCommentPageByArticleId(Long articleId, cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return commentMapper.selectCommentPageByArticleId(articleId, pageParam);
    }

    @Override
    public List<BlogCommentDO> getCommentTree(Long articleId) {
        List<BlogCommentDO> allComments = commentMapper.selectTreeListByArticleId(articleId);
        return buildTree(allComments);
    }

    /**
     * 将平铺列表组装为嵌套树（返回顶级评论，树结构由 Controller 层转换为 VO 时处理）
     */
    private List<BlogCommentDO> buildTree(List<BlogCommentDO> allComments) {
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
