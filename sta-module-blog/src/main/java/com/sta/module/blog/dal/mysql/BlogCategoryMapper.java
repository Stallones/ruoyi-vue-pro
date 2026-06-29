package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.dal.dataobject.BlogCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogCategoryMapper extends BaseMapperX<BlogCategoryDO> {

    default List<BlogCategoryDO> selectListAll() {
        return selectList(new LambdaQueryWrapperX<BlogCategoryDO>()
                .orderByDesc(BlogCategoryDO::getId));
    }

}
