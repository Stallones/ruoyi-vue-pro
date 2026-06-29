package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.tag.vo.BlogTagPageReqVO;
import com.sta.module.blog.controller.admin.tag.vo.BlogTagSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogTagDO;

import java.util.List;

/**
 * 博客标签 Service 接口
 */
public interface BlogTagService {

    Long createTag(BlogTagSaveReqVO createReqVO);

    void updateTag(BlogTagSaveReqVO updateReqVO);

    void deleteTag(Long id);

    BlogTagDO getTag(Long id);

    PageResult<BlogTagDO> getTagPage(BlogTagPageReqVO pageReqVO);

    List<BlogTagDO> getTagList();

}
