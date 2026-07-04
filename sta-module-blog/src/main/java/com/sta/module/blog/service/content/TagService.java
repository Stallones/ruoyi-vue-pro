package com.sta.module.blog.service.content;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.content.vo.TagPageReqVO;
import com.sta.module.blog.controller.admin.content.vo.TagSaveReqVO;
import com.sta.module.blog.dal.dataobject.content.TagDO;

import java.util.List;

/**
 * 博客标签 Service 接口
 */
public interface TagService {

    Long createTag(TagSaveReqVO createReqVO);

    void updateTag(TagSaveReqVO updateReqVO);

    void deleteTag(Long id);

    TagDO getTag(Long id);

    PageResult<TagDO> getTagPage(TagPageReqVO pageReqVO);

    List<TagDO> getTagList();

}
