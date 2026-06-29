package com.sta.module.blog.dal.mysql;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.message.vo.BlogMessagePageReqVO;
import com.sta.module.blog.dal.dataobject.BlogMessageDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BlogMessageMapper extends BaseMapperX<BlogMessageDO> {

    default PageResult<BlogMessageDO> selectPage(BlogMessagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogMessageDO>()
                .eqIfPresent(BlogMessageDO::getType, reqVO.getType())
                .eqIfPresent(BlogMessageDO::getStatus, reqVO.getIsCheck())
                .betweenIfPresent(BlogMessageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BlogMessageDO::getId));
    }

    /**
     * App - 查询已审核留言列表（分页）
     */
    default PageResult<BlogMessageDO> selectMessagePageByStatus(cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<BlogMessageDO>()
                .eq(BlogMessageDO::getStatus, 1)
                .orderByDesc(BlogMessageDO::getCreateTime));
    }

    /**
     * App - 查询所有已审核留言（树结构用，不分页）
     */
    default List<BlogMessageDO> selectTreeList() {
        return selectList(new LambdaQueryWrapperX<BlogMessageDO>()
                .eq(BlogMessageDO::getStatus, 1)
                .orderByAsc(BlogMessageDO::getCreateTime));
    }

}
