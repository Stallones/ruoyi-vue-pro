package com.sta.module.blog.dal.mysql.content;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.dal.dataobject.content.ArticleTagDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ArticleTagMapper extends BaseMapperX<ArticleTagDO> {

    default List<ArticleTagDO> selectListByArticleId(Long articleId) {
        return selectList(new LambdaQueryWrapperX<ArticleTagDO>()
                .eq(ArticleTagDO::getArticleId, articleId));
    }

    default List<ArticleTagDO> selectListByTagId(Long tagId) {
        return selectList(new LambdaQueryWrapperX<ArticleTagDO>()
                .eq(ArticleTagDO::getTagId, tagId));
    }

    default void deleteByArticleId(Long articleId) {
        delete(new LambdaQueryWrapperX<ArticleTagDO>()
                .eq(ArticleTagDO::getArticleId, articleId));
    }

}
