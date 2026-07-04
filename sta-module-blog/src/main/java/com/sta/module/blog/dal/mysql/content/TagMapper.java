package com.sta.module.blog.dal.mysql.content;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.content.vo.TagPageReqVO;
import com.sta.module.blog.dal.dataobject.content.TagDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TagMapper extends BaseMapperX<TagDO> {

    default PageResult<TagDO> selectPage(TagPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<TagDO>()
                .likeIfPresent(TagDO::getTagName, reqVO.getTagName())
                .orderByDesc(TagDO::getId));
    }

    default List<TagDO> selectList() {
        return selectList(new LambdaQueryWrapperX<TagDO>()
                .orderByDesc(TagDO::getId));
    }

}
