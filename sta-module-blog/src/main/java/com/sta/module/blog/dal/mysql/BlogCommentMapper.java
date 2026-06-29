package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.comment.vo.BlogCommentPageReqVO;
import com.sta.module.blog.dal.dataobject.BlogCommentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogCommentMapper extends BaseMapperX<BlogCommentDO> {

    default PageResult<BlogCommentDO> selectPage(BlogCommentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogCommentDO>()
                .eqIfPresent(BlogCommentDO::getType, reqVO.getType())
                .eqIfPresent(BlogCommentDO::getArticleId, reqVO.getArticleId())
                .eqIfPresent(BlogCommentDO::getStatus, reqVO.getIsCheck())
                .betweenIfPresent(BlogCommentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BlogCommentDO::getId));
    }

    /**
     * App - 根据文章ID查询已审核评论列表（分页）
     */
    default PageResult<BlogCommentDO> selectCommentPageByArticleId(Long articleId, cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<BlogCommentDO>()
                .eq(BlogCommentDO::getArticleId, articleId)
                .eq(BlogCommentDO::getStatus, 1)
                .orderByAsc(BlogCommentDO::getCreateTime));
    }

    /**
     * App - 根据文章ID查询所有已审核评论（树结构用，不分页）
     */
    default List<BlogCommentDO> selectTreeListByArticleId(Long articleId) {
        return selectList(new LambdaQueryWrapperX<BlogCommentDO>()
                .eq(BlogCommentDO::getArticleId, articleId)
                .eq(BlogCommentDO::getStatus, 1)
                .orderByAsc(BlogCommentDO::getCreateTime));
    }

    /**
     * 统计文章评论数（已审核）
     */
    default Long selectCountByArticleId(Long articleId) {
        return selectCount(new LambdaQueryWrapperX<BlogCommentDO>()
                .eq(BlogCommentDO::getArticleId, articleId)
                .eq(BlogCommentDO::getStatus, 1));
    }

}

