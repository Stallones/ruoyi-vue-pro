package com.sta.module.blog.dal.mysql.interaction;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.interaction.vo.CommentPageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.CommentDO;
import com.sta.module.blog.enums.TypeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper extends BaseMapperX<CommentDO> {

    default PageResult<CommentDO> selectPage(CommentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CommentDO>()
                .eqIfPresent(CommentDO::getType, reqVO.getType())
                .eqIfPresent(CommentDO::getArticleId, reqVO.getArticleId())
                .eqIfPresent(CommentDO::getStatus, reqVO.getIsCheck())
                .betweenIfPresent(CommentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CommentDO::getId));
    }

    /**
     * App - 根据文章ID查询已审核评论列表（分页）
     */
    default PageResult<CommentDO> selectCommentPageByArticleId(Long articleId, cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<CommentDO>()
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getStatus, 1)
                .orderByAsc(CommentDO::getCreateTime));
    }

    /**
     * App - 顶级评论分页（type=20 + articleId + status=1 + 排序）
     *
     * @param articleId 文章ID
     * @param pageParam 分页参数
     * @param orderBy   排序方式：newest=最新(时间倒序), hottest=最热(时间倒序，后续由 Controller 按点赞数二次排序)
     */
    default PageResult<CommentDO> selectTopCommentPage(Long articleId,
                                                       cn.iocoder.yudao.framework.common.pojo.PageParam pageParam,
                                                       String orderBy) {
        LambdaQueryWrapperX<CommentDO> wrapper = new LambdaQueryWrapperX<CommentDO>()
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getType, TypeEnum.CMT)
                .eq(CommentDO::getStatus, 1);
        // 默认按时间倒序（最新优先）
        wrapper.orderByDesc(CommentDO::getCreateTime);
        return selectPage(pageParam, wrapper);
    }

    /**
     * App - 回复分页（root_id=X + type=21 + status=1 + 按时间升序）
     */
    default PageResult<CommentDO> selectReplyPageByRootId(Long rootId,
                                                          cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<CommentDO>()
                .eq(CommentDO::getRootId, rootId)
                .eq(CommentDO::getType, TypeEnum.CMT_RE)
                .eq(CommentDO::getStatus, 1)
                .orderByAsc(CommentDO::getCreateTime));
    }

    /**
     * App - 统计某根评论下的回复数（已审核）
     */
    default Long selectReplyCountByRootId(Long rootId) {
        return selectCount(new LambdaQueryWrapperX<CommentDO>()
                .eq(CommentDO::getRootId, rootId)
                .eq(CommentDO::getType, TypeEnum.CMT_RE)
                .eq(CommentDO::getStatus, 1));
    }

    /**
     * App - 批量查询多个根评论下的所有回复（用于批量计算 replyCount）
     */
    default List<CommentDO> selectRepliesByRootIds(List<Long> rootIds) {
        return selectList(new LambdaQueryWrapperX<CommentDO>()
                .in(CommentDO::getRootId, rootIds)
                .eq(CommentDO::getType, TypeEnum.CMT_RE)
                .eq(CommentDO::getStatus, 1)
                .select(CommentDO::getRootId));
    }

    /**
     * App - 根据文章ID查询所有已审核评论（树结构用，不分页）
     */
    default List<CommentDO> selectTreeListByArticleId(Long articleId) {
        return selectList(new LambdaQueryWrapperX<CommentDO>()
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getStatus, 1)
                .orderByAsc(CommentDO::getCreateTime));
    }

    /**
     * 统计文章评论数（已审核，仅顶级评论 type=20）
     */
    default Long selectCountByArticleId(Long articleId) {
        return selectCount(new LambdaQueryWrapperX<CommentDO>()
                .eq(CommentDO::getArticleId, articleId)
                .eq(CommentDO::getType, TypeEnum.CMT)
                .eq(CommentDO::getStatus, 1));
    }

    /**
     * 统计全站已审核评论总数
     */
    default Long selectTotalApprovedCount() {
        return selectCount(new LambdaQueryWrapperX<CommentDO>()
                .eq(CommentDO::getStatus, 1));
    }

}

