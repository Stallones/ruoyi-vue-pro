package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.dal.dataobject.BlogArticleTagDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogArticleTagMapper extends BaseMapperX<BlogArticleTagDO> {

    default List<BlogArticleTagDO> selectListByArticleId(Long articleId) {
        return selectList(new LambdaQueryWrapperX<BlogArticleTagDO>()
                .eq(BlogArticleTagDO::getArticleId, articleId));
    }

    default List<BlogArticleTagDO> selectListByTagId(Long tagId) {
        return selectList(new LambdaQueryWrapperX<BlogArticleTagDO>()
                .eq(BlogArticleTagDO::getTagId, tagId));
    }

    default void deleteByArticleId(Long articleId) {
        delete(new LambdaQueryWrapperX<BlogArticleTagDO>()
                .eq(BlogArticleTagDO::getArticleId, articleId));
    }

}
