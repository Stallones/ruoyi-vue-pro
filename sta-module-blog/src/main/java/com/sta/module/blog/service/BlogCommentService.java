package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.comment.vo.BlogCommentPageReqVO;
import com.sta.module.blog.dal.dataobject.BlogCommentDO;

import java.util.List;

/**
 * 博客评论 Service 接口
 */
public interface BlogCommentService {

    PageResult<BlogCommentDO> getCommentPage(BlogCommentPageReqVO pageReqVO);

    void updateCommentCheck(Long id, Integer isCheck);

    void deleteComment(Long id);

    BlogCommentDO getComment(Long id);

    /**
     * App - 创建文章评论/回复
     *
     * @param userId      用户ID
     * @param articleId   文章ID
     * @param parentId    父级ID（评论为0，回复指向被回复的评论/回复ID）
     * @param content     内容
     * @param replyUserId 被回复用户ID（评论为0）
     * @return 评论ID
     */
    Long createComment(Long userId, Long articleId, Long parentId, String content, Long toUserId);

    /**
     * App - 获取文章评论列表（分页）
     */
    PageResult<BlogCommentDO> getCommentPageByArticleId(Long articleId, cn.iocoder.yudao.framework.common.pojo.PageParam pageParam);

    /**
     * App - 获取文章评论树（嵌套结构）
     */
    List<BlogCommentDO> getCommentTree(Long articleId);

}
