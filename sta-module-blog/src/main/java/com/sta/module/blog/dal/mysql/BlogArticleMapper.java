package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.article.vo.BlogArticlePageReqVO;
import com.sta.module.blog.controller.app.article.vo.AppArticlePageReqVO;
import com.sta.module.blog.dal.dataobject.BlogArticleDO;
import com.sta.module.blog.enums.ArchiveTypeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogArticleMapper extends BaseMapperX<BlogArticleDO> {

    default PageResult<BlogArticleDO> selectPage(BlogArticlePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogArticleDO>()
                .likeIfPresent(BlogArticleDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BlogArticleDO::getCategoryId, reqVO.getCategoryId())
                .eqIfPresent(BlogArticleDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BlogArticleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BlogArticleDO::getVisitCount)
                .orderByDesc(BlogArticleDO::getId));
    }

    default List<BlogArticleDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, status)
                .orderByDesc(BlogArticleDO::getVisitCount)
                .orderByDesc(BlogArticleDO::getId));
    }

    /**
     * App - 获取时间轴文章列表（公开文章，按创建时间排序）
     */
    default List<BlogArticleDO> selectTimelineList() {
        return selectList(new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, 1) // 仅公开文章
                .orderByDesc(BlogArticleDO::getCreateTime));
    }

    /**
     * App - 按分类/标签获取文章列表（公开文章）
     */
    default List<BlogArticleDO> selectListByArchive(String archiveType, Long archiveId) {
        LambdaQueryWrapperX<BlogArticleDO> wrapper = new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, 1);
        if (ArchiveTypeEnum.CATEGORY.getValue().equals(archiveType)) {
            wrapper.eq(BlogArticleDO::getCategoryId, archiveId);
        }
        // tag 过滤需要在 Service 层通过 article_tag 关联表处理
        return selectList(wrapper.orderByDesc(BlogArticleDO::getCreateTime));
    }

    /**
     * App - 推荐文章（高访问量公开文章，限制数量）
     */
    default List<BlogArticleDO> selectRecommendList(Integer limit) {
        return selectList(new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, 1)
                .orderByDesc(BlogArticleDO::getVisitCount)
                .last("LIMIT " + limit));
    }

    /**
     * App - 相关文章（同分类，排除自身，限制数量）
     */
    default List<BlogArticleDO> selectRelatedList(Long categoryId, Long excludeArticleId, Integer limit) {
        return selectList(new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, 1)
                .eq(BlogArticleDO::getCategoryId, categoryId)
                .ne(BlogArticleDO::getId, excludeArticleId)
                .orderByDesc(BlogArticleDO::getCreateTime)
                .last("LIMIT " + limit));
    }

    /**
     * App - 搜索标题（公开文章，用于初始化搜索）
     */
    default List<BlogArticleDO> selectSearchTitleList() {
        return selectList(new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, 1)
                .select(BlogArticleDO::getId, BlogArticleDO::getTitle));
    }

    /**
     * App - 搜索内容（公开文章，LIKE 匹配标题或内容）
     */
    default List<BlogArticleDO> selectSearchByContent(String keyword) {
        return selectList(new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, 1)
                .like(BlogArticleDO::getTitle, keyword)
                .or()
                .like(BlogArticleDO::getContent, keyword)
                .orderByDesc(BlogArticleDO::getCreateTime));
    }

    /**
     * App - 前端文章分页（仅公开文章，admin VO）
     */
    default PageResult<BlogArticleDO> selectFrontPage(BlogArticlePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, 1)
                .likeIfPresent(BlogArticleDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BlogArticleDO::getCategoryId, reqVO.getCategoryId())
                .betweenIfPresent(BlogArticleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BlogArticleDO::getCreateTime)
                .orderByDesc(BlogArticleDO::getId));
    }

    /**
     * App - 前端文章分页（仅公开文章，app VO）
     */
    default PageResult<BlogArticleDO> selectFrontPage(AppArticlePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogArticleDO>()
                .eq(BlogArticleDO::getStatus, 1)
                .eqIfPresent(BlogArticleDO::getCategoryId, reqVO.getCategoryId())
                .orderByDesc(BlogArticleDO::getCreateTime)
                .orderByDesc(BlogArticleDO::getId));
    }

}
