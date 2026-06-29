package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.link.vo.BlogLinkPageReqVO;
import com.sta.module.blog.dal.dataobject.BlogLinkDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogLinkMapper extends BaseMapperX<BlogLinkDO> {

    default PageResult<BlogLinkDO> selectPage(BlogLinkPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogLinkDO>()
                .likeIfPresent(BlogLinkDO::getName, reqVO.getName())
                .eqIfPresent(BlogLinkDO::getStatus, reqVO.getIsCheck())
                .orderByDesc(BlogLinkDO::getId));
    }

    default List<BlogLinkDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<BlogLinkDO>()
                .eq(BlogLinkDO::getStatus, status)
                .orderByDesc(BlogLinkDO::getId));
    }

}
