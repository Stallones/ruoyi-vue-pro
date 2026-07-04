package com.sta.module.blog.dal.mysql.site;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.site.vo.LinkPageReqVO;
import com.sta.module.blog.dal.dataobject.site.LinkDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LinkMapper extends BaseMapperX<LinkDO> {

    default PageResult<LinkDO> selectPage(LinkPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<LinkDO>()
                .likeIfPresent(LinkDO::getName, reqVO.getName())
                .eqIfPresent(LinkDO::getStatus, reqVO.getIsCheck())
                .orderByDesc(LinkDO::getId));
    }

    default List<LinkDO> selectListByStatus(Integer status) {
        return selectList(new LambdaQueryWrapperX<LinkDO>()
                .eq(LinkDO::getStatus, status)
                .orderByDesc(LinkDO::getId));
    }

}
