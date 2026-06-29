package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.link.vo.BlogLinkPageReqVO;
import com.sta.module.blog.controller.admin.link.vo.BlogLinkSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogLinkDO;

import java.util.List;

/**
 * 博客友链 Service 接口
 */
public interface BlogLinkService {

    Long createLink(BlogLinkSaveReqVO createReqVO);

    void updateLink(BlogLinkSaveReqVO updateReqVO);

    void deleteLink(Long id);

    BlogLinkDO getLink(Long id);

    PageResult<BlogLinkDO> getLinkPage(BlogLinkPageReqVO pageReqVO);

    void updateLinkCheck(Long id, Integer isCheck);

    /**
     * App - 获取已审核通过的友链列表
     */
    List<BlogLinkDO> getLinkList();

    /**
     * App - 申请友链
     */
    Long applyLink(String name, String url, String description, String background, String email);

}
