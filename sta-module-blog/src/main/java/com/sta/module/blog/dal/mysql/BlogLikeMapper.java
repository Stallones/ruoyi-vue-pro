package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.dal.dataobject.BlogLikeDO;
import com.sta.module.blog.enums.BlogTypeEnum;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BlogLikeMapper extends BaseMapperX<BlogLikeDO> {

    /**
     * 按类型、数据ID和用户ID查询（含已取消的）
     */
    default BlogLikeDO selectByTypeAndDataId(BlogTypeEnum type, Long dataId, Long userId) {
        return selectOne(new LambdaQueryWrapperX<BlogLikeDO>()
                .eq(BlogLikeDO::getType, type)
                .eq(BlogLikeDO::getDataId, dataId)
                .eq(BlogLikeDO::getUserId, userId));
    }

    /**
     * 统计有效点赞数
     */
    default Long selectCountByTypeAndDataId(BlogTypeEnum type, Long dataId) {
        return selectCount(new LambdaQueryWrapperX<BlogLikeDO>()
                .eq(BlogLikeDO::getType, type)
                .eq(BlogLikeDO::getDataId, dataId)
                .eq(BlogLikeDO::getStatus, 1));
    }

}
