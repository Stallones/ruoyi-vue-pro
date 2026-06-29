package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.favorite.vo.BlogFavoritePageReqVO;
import com.sta.module.blog.dal.dataobject.BlogFavoriteDO;
import com.sta.module.blog.enums.BlogTypeEnum;

import java.util.List;

/**
 * 博客收藏 Service 接口
 */
public interface BlogFavoriteService {

    PageResult<BlogFavoriteDO> getFavoritePage(BlogFavoritePageReqVO pageReqVO);

    void deleteFavorite(Long id);

    /**
     * App - 创建收藏
     */
    Long createFavorite(BlogTypeEnum type, Long typeId);

    /**
     * App - 取消收藏
     */
    void deleteFavoriteByTypeAndTypeId(BlogTypeEnum type, Long typeId);

    /**
     * App - 获取当前用户收藏列表
     */
    List<BlogFavoriteDO> getFavoriteListByCreator();

    /**
     * App - 是否已收藏
     */
    Boolean isFavorite(BlogTypeEnum type, Long typeId);

}
