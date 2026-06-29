package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.dal.dataobject.BlogImageDO;
import com.sta.module.blog.enums.BlogTypeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogImageMapper extends BaseMapperX<BlogImageDO> {

    default List<BlogImageDO> selectListAll() {
        return selectList(new LambdaQueryWrapperX<BlogImageDO>()
                .orderByAsc(BlogImageDO::getSort));
    }

    default List<BlogImageDO> selectListByTypeAndDataId(BlogTypeEnum type, Long dataId) {
        return selectList(new LambdaQueryWrapperX<BlogImageDO>()
                .eqIfPresent(BlogImageDO::getType, type)
                .eqIfPresent(BlogImageDO::getDataId, dataId)
                .orderByAsc(BlogImageDO::getSort)
                .orderByDesc(BlogImageDO::getId));
    }

}
