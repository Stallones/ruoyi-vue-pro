package com.sta.module.blog.dal.mysql.content;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.dal.dataobject.content.CategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapperX<CategoryDO> {

    default List<CategoryDO> selectList() {
        return selectList(new LambdaQueryWrapperX<CategoryDO>()
                .orderByDesc(CategoryDO::getId));
    }

}
