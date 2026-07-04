package com.sta.module.blog.service.interaction;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.interaction.vo.CommentPageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.CommentDO;

import java.util.List;

/**
 * 博客评论 Service 接口
 */
public interface CommentService {

    PageResult<CommentDO> getCommentPage(CommentPageReqVO pageReqVO);

    void updateCommentCheck(Long id, Integer isCheck);

    void deleteComment(Long id);

    CommentDO getComment(Long id);

    /**
     * App - 创建文章评论/回复
     *
     * @param userId      用户ID
     * @param articleId   文章ID
     * @param parentId    父级ID（评论为0，回复传被回复的评论/回复ID）
     * @param content     内容
     * @param toUserId    被回复用户ID（评论为0）
     * @param ipLocation  IP属地
     * @param browser     浏览器
     * @param os          操作系统
     * @return 评论ID
     */
    Long createComment(Long userId, Long articleId, Long parentId, String content, Long toUserId,
                       String ipLocation, String browser, String os);

    /**
     * App - 获取文章评论列表（分页）
     */
    PageResult<CommentDO> getCommentPageByArticleId(Long articleId, PageParam pageParam);

    /**
     * App - 获取文章评论树（嵌套结构）
     */
    List<CommentDO> getCommentTree(Long articleId);

    /**
     * App - 获取顶级评论分页（type=20）
     */
    PageResult<CommentDO> getTopCommentPage(Long articleId, PageParam pageParam, String orderBy);

    /**
     * App - 获取回复分页（type=21，按 rootId）
     */
    PageResult<CommentDO> getReplyPage(Long rootId, PageParam pageParam);

    /**
     * App - 获取文章评论总数（仅顶级评论 type=20）
     */
    Long getCommentCount(Long articleId);

}
