package com.sta.module.blog.service.site;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.site.vo.LinkPageReqVO;
import com.sta.module.blog.controller.admin.site.vo.LinkSaveReqVO;
import com.sta.module.blog.dal.dataobject.site.LinkDO;

import java.util.List;

/**
 * 博客友链 Service 接口
 */
public interface LinkService {

    Long createLink(LinkSaveReqVO createReqVO);

    void updateLink(LinkSaveReqVO updateReqVO);

    void deleteLink(Long id);

    LinkDO getLink(Long id);

    PageResult<LinkDO> getLinkPage(LinkPageReqVO pageReqVO);

    void updateLinkCheck(Long id, Integer isCheck);

    /**
     * App - 获取已审核通过的友链列表
     */
    List<LinkDO> getLinkList();

    /**
     * App - 申请友链
     */
    Long applyForLink(String name, String url, String description, String background, String email);

}
