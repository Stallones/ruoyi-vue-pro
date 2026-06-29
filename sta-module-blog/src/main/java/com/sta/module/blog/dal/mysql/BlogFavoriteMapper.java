package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.favorite.vo.BlogFavoritePageReqVO;
import com.sta.module.blog.dal.dataobject.BlogFavoriteDO;
import com.sta.module.blog.enums.BlogTypeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogFavoriteMapper extends BaseMapperX<BlogFavoriteDO> {

    default PageResult<BlogFavoriteDO> selectPage(BlogFavoritePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogFavoriteDO>()
                .eqIfPresent(BlogFavoriteDO::getType, reqVO.getType())
                .eqIfPresent(BlogFavoriteDO::getDataId, reqVO.getDataId())
                .orderByDesc(BlogFavoriteDO::getId));
    }

    default List<BlogFavoriteDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<BlogFavoriteDO>()
                .eq(BlogFavoriteDO::getUserId, userId)
                .eq(BlogFavoriteDO::getStatus, 1)
                .orderByDesc(BlogFavoriteDO::getId));
    }

    /**
     * 按类型、数据ID和用户ID查询（含已取消的）
     */
    default BlogFavoriteDO selectByTypeAndDataId(BlogTypeEnum type, Long dataId, Long userId) {
        return selectOne(new LambdaQueryWrapperX<BlogFavoriteDO>()
                .eq(BlogFavoriteDO::getType, type)
                .eq(BlogFavoriteDO::getDataId, dataId)
                .eq(BlogFavoriteDO::getUserId, userId));
    }

    /**
     * 统计有效收藏数
     */
    default Long selectCountByTypeAndDataId(BlogTypeEnum type, Long dataId) {
        return selectCount(new LambdaQueryWrapperX<BlogFavoriteDO>()
                .eq(BlogFavoriteDO::getType, type)
                .eq(BlogFavoriteDO::getDataId, dataId)
                .eq(BlogFavoriteDO::getStatus, 1));
    }

}
