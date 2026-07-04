package com.sta.module.blog.dal.mysql.interaction;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.interaction.vo.FavoritePageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.FavoriteDO;
import com.sta.module.blog.enums.TypeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FavoriteMapper extends BaseMapperX<FavoriteDO> {

    default PageResult<FavoriteDO> selectPage(FavoritePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FavoriteDO>()
                .eqIfPresent(FavoriteDO::getType, reqVO.getType())
                .eqIfPresent(FavoriteDO::getDataId, reqVO.getDataId())
                .orderByDesc(FavoriteDO::getId));
    }

    default List<FavoriteDO> selectListByUserId(Long userId) {
        return selectList(new LambdaQueryWrapperX<FavoriteDO>()
                .eq(FavoriteDO::getUserId, userId)
                .eq(FavoriteDO::getStatus, 1)
                .orderByDesc(FavoriteDO::getId));
    }

    /**
     * 按类型、数据ID和用户ID查询（含已取消的）
     */
    default FavoriteDO selectByType(TypeEnum type, Long dataId, Long userId) {
        return selectOne(new LambdaQueryWrapperX<FavoriteDO>()
                .eq(FavoriteDO::getType, type)
                .eq(FavoriteDO::getDataId, dataId)
                .eq(FavoriteDO::getUserId, userId));
    }

    /**
     * 统计有效收藏数
     */
    default Long selectCountByType(TypeEnum type, Long dataId) {
        return selectCount(new LambdaQueryWrapperX<FavoriteDO>()
                .eq(FavoriteDO::getType, type)
                .eq(FavoriteDO::getDataId, dataId)
                .eq(FavoriteDO::getStatus, 1));
    }

}
