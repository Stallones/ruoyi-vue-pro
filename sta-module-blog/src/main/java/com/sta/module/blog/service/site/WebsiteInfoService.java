package com.sta.module.blog.service.site;

import com.sta.module.blog.controller.app.content.vo.AppArticleRespVO;
import com.sta.module.blog.controller.app.site.vo.AppSnapshotRespVO;
import com.sta.module.blog.controller.app.site.vo.AppWebsiteInfoRespVO;

/**
 * 博客网站信息 Service 接口
 * <p>
 * 职责：统计聚合 + 健康检查 + 快照生成
 */
public interface WebsiteInfoService {

    /**
     * 获取全站统计数据
     */
    AppWebsiteInfoRespVO getStats();

    /**
     * 健康检查（服务是否可用）
     */
    boolean healthCheck();

    /**
     * 获取全站离线快照（构建专用）
     */
    AppSnapshotRespVO getSnapshot();

    /**
     * 获取单篇文章完整内容（含 content，构建专用）
     *
     * @param id 文章ID
     * @return 文章 VO（含 content），不存在返回 null
     */
    AppArticleRespVO getArticleFull(Long id);

}
