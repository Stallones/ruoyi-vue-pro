package com.sta.module.blog.service.interaction;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.interaction.vo.FavoritePageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.FavoriteDO;
import com.sta.module.blog.enums.TypeEnum;

import java.util.List;

/**
 * 博客收藏 Service 接口
 */
public interface FavoriteService {

    PageResult<FavoriteDO> getFavoritePage(FavoritePageReqVO pageReqVO);

    void deleteFavorite(Long id);

    /**
     * App - 创建收藏
     */
    Long createFavorite(TypeEnum type, Long typeId);

    /**
     * App - 取消收藏
     */
    void deleteFavoriteByTypeAndTypeId(TypeEnum type, Long typeId);

    /**
     * App - 获取当前用户收藏列表
     */
    List<FavoriteDO> getFavoriteListByCreator();

    /**
     * App - 是否已收藏
     */
    Boolean isFavorite(TypeEnum type, Long typeId);

}
