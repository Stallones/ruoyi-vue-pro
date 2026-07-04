package com.sta.module.blog.dal.mysql.media;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.enums.TypeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ImageMapper extends BaseMapperX<ImageDO> {

    default List<ImageDO> selectList() {
        return selectList(new LambdaQueryWrapperX<ImageDO>()
                .orderByAsc(ImageDO::getSort));
    }

    default List<ImageDO> selectListByType(TypeEnum type, Long dataId) {
        return selectList(new LambdaQueryWrapperX<ImageDO>()
                .eqIfPresent(ImageDO::getType, type)
                .eqIfPresent(ImageDO::getDataId, dataId)
                .orderByAsc(ImageDO::getSort)
                .orderByDesc(ImageDO::getId));
    }

}
