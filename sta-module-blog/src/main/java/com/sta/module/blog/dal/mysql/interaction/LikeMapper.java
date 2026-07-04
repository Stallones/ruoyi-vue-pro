package com.sta.module.blog.dal.mysql.interaction;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.dal.dataobject.interaction.LikeDO;
import com.sta.module.blog.enums.TypeEnum;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper extends BaseMapperX<LikeDO> {

    /**
     * 按类型、数据ID和用户ID查询（含已取消的）
     */
    default LikeDO selectByType(TypeEnum type, Long dataId, Long userId) {
        return selectOne(new LambdaQueryWrapperX<LikeDO>()
                .eq(LikeDO::getType, type)
                .eq(LikeDO::getDataId, dataId)
                .eq(LikeDO::getUserId, userId));
    }

    /**
     * 统计有效点赞数
     */
    default Long selectCountByType(TypeEnum type, Long dataId) {
        return selectCount(new LambdaQueryWrapperX<LikeDO>()
                .eq(LikeDO::getType, type)
                .eq(LikeDO::getDataId, dataId)
                .eq(LikeDO::getStatus, 1));
    }

    /**
     * 批量查询某类型下多个 dataId 的有效点赞记录（用于计算 likeCount）
     */
    default java.util.List<LikeDO> selectListByTypeAndDataIds(TypeEnum type, java.util.List<Long> dataIds) {
        return selectList(new LambdaQueryWrapperX<LikeDO>()
                .eq(LikeDO::getType, type)
                .in(LikeDO::getDataId, dataIds)
                .eq(LikeDO::getStatus, 1)
                .select(LikeDO::getDataId));
    }

    /**
     * 批量查询当前用户对多个 dataId 的点赞状态
     */
    default java.util.List<LikeDO> selectUserLikesByTypeAndDataIds(TypeEnum type, java.util.List<Long> dataIds, Long userId) {
        return selectList(new LambdaQueryWrapperX<LikeDO>()
                .eq(LikeDO::getType, type)
                .in(LikeDO::getDataId, dataIds)
                .eq(LikeDO::getUserId, userId)
                .eq(LikeDO::getStatus, 1)
                .select(LikeDO::getDataId));
    }

}
