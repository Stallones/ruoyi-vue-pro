package com.sta.module.blog.dal.mysql.content;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.content.vo.ArticlePageReqVO;
import com.sta.module.blog.controller.app.content.vo.AppArticlePageReqVO;
import com.sta.module.blog.dal.dataobject.content.ArticleDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapperX<ArticleDO> {

    default PageResult<ArticleDO> selectPage(ArticlePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ArticleDO>()
                .likeIfPresent(ArticleDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ArticleDO::getCategoryId, reqVO.getCategoryId())
                .eqIfPresent(ArticleDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ArticleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ArticleDO::getVisitCount)
                .orderByDesc(ArticleDO::getId));
    }

    default List<ArticleDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<ArticleDO>()
                .eq(ArticleDO::getStatus, status)
                .orderByDesc(ArticleDO::getVisitCount)
                .orderByDesc(ArticleDO::getId));
    }

    /**
     * App - 获取时间轴文章列表（公开文章，按创建时间排序）
     */
    default List<ArticleDO> selectListByCreateTime() {
        return selectList(new LambdaQueryWrapperX<ArticleDO>()
                .eq(ArticleDO::getStatus, 1) // 仅公开文章
                .orderByDesc(ArticleDO::getCreateTime));
    }

    /**
     * App - 推荐文章（高访问量公开文章，限制数量）
     */
    default List<ArticleDO> selectListByVisitCount(Integer limit) {
        return selectList(new LambdaQueryWrapperX<ArticleDO>()
                .eq(ArticleDO::getStatus, 1)
                .orderByDesc(ArticleDO::getVisitCount)
                .last("LIMIT " + limit));
    }

    /**
     * App - 按分类获取文章列表（同分类，可排除指定文章，限制数量）
     */
    default List<ArticleDO> selectListByCategoryId(Long categoryId, Long excludeArticleId, Integer limit) {
        return selectList(new LambdaQueryWrapperX<ArticleDO>()
                .eq(ArticleDO::getStatus, 1)
                .eq(ArticleDO::getCategoryId, categoryId)
                .ne(ArticleDO::getId, excludeArticleId)
                .orderByDesc(ArticleDO::getCreateTime)
                .last("LIMIT " + limit));
    }

    /**
     * App - 搜索内容（公开文章，LIKE 匹配标题或内容）
     */
    default List<ArticleDO> selectListByTitleAndContent(String keyword) {
        return selectList(new LambdaQueryWrapperX<ArticleDO>()
                .eq(ArticleDO::getStatus, 1)
                .like(ArticleDO::getTitle, keyword)
                .or()
                .like(ArticleDO::getContent, keyword)
                .orderByDesc(ArticleDO::getCreateTime));
    }

    /**
     * App - 前端文章分页（仅公开文章，admin VO）
     */
    default PageResult<ArticleDO> selectPageAdmin(ArticlePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ArticleDO>()
                .eq(ArticleDO::getStatus, 1)
                .likeIfPresent(ArticleDO::getTitle, reqVO.getTitle())
                .eqIfPresent(ArticleDO::getCategoryId, reqVO.getCategoryId())
                .betweenIfPresent(ArticleDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ArticleDO::getCreateTime)
                .orderByDesc(ArticleDO::getId));
    }

    /**
     * App - 前端文章分页（仅公开文章，app VO）
     */
    default PageResult<ArticleDO> selectPageApp(AppArticlePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ArticleDO>()
                .eq(ArticleDO::getStatus, 1)
                .eqIfPresent(ArticleDO::getCategoryId, reqVO.getCategoryId())
                .orderByDesc(ArticleDO::getCreateTime)
                .orderByDesc(ArticleDO::getId));
    }

}
