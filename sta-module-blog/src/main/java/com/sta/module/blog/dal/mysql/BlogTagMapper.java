package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.tag.vo.BlogTagPageReqVO;
import com.sta.module.blog.dal.dataobject.BlogTagDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogTagMapper extends BaseMapperX<BlogTagDO> {

    default PageResult<BlogTagDO> selectPage(BlogTagPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogTagDO>()
                .likeIfPresent(BlogTagDO::getTagName, reqVO.getTagName())
                .orderByDesc(BlogTagDO::getId));
    }

    default List<BlogTagDO> selectListAll() {
        return selectList(new LambdaQueryWrapperX<BlogTagDO>()
                .orderByDesc(BlogTagDO::getId));
    }

}
