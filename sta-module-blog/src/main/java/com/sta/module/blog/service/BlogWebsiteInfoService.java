package com.sta.module.blog.service;

import com.sta.module.blog.controller.admin.websiteinfo.vo.BlogWebsiteInfoSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogWebsiteInfoDO;

/**
 * 博客网站信息 Service 接口
 * <p>
 * 网站信息为单条记录，只支持查询和修改，不支持新增和删除
 */
public interface BlogWebsiteInfoService {

    /**
     * 更新网站信息
     */
    void updateWebsiteInfo(BlogWebsiteInfoSaveReqVO updateReqVO);

    /**
     * 获得网站信息
     */
    BlogWebsiteInfoDO getWebsiteInfo();

    /**
     * App - 健康检查（服务是否可用）
     */
    Boolean healthCheck();

}
