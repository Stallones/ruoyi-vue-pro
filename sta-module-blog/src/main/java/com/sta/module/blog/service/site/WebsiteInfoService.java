package com.sta.module.blog.service.site;

import com.sta.module.blog.controller.admin.site.vo.WebsiteInfoSaveReqVO;
import com.sta.module.blog.dal.dataobject.site.WebsiteInfoDO;

/**
 * 博客网站信息 Service 接口
 * <p>
 * 网站信息为单条记录，只支持查询和修改，不支持新增和删除
 */
public interface WebsiteInfoService {

    /**
     * 更新网站信息
     */
    void updateWebsiteInfo(WebsiteInfoSaveReqVO updateReqVO);

    /**
     * 获得网站信息
     */
    WebsiteInfoDO getWebsiteInfo();

    /**
     * App - 健康检查（服务是否可用）
     */
    Boolean healthCheck();

}
