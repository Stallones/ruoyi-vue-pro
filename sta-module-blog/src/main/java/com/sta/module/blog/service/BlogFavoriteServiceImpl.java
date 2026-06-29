package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.controller.admin.favorite.vo.BlogFavoritePageReqVO;
import com.sta.module.blog.dal.dataobject.BlogFavoriteDO;
import com.sta.module.blog.dal.mysql.BlogFavoriteMapper;
import com.sta.module.blog.enums.BlogTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.FAVORITE_NOT_EXISTS;

@Service
@Validated
public class BlogFavoriteServiceImpl implements BlogFavoriteService {

    @Resource
    private BlogFavoriteMapper favoriteMapper;

    @Override
    public PageResult<BlogFavoriteDO> getFavoritePage(BlogFavoritePageReqVO pageReqVO) {
        return favoriteMapper.selectPage(pageReqVO);
    }

    @Override
    public void deleteFavorite(Long id) {
        validateFavoriteExists(id);
        favoriteMapper.deleteById(id);
    }

    @Override
    public Long createFavorite(BlogTypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        BlogFavoriteDO existing = favoriteMapper.selectByTypeAndDataId(type, typeId, userId);
        if (existing != null) {
            // 已有记录：重新激活
            favoriteMapper.updateById(BlogFavoriteDO.builder().id(existing.getId()).status(1).build());
            return existing.getId();
        }
        // 无记录：创建新记录
        BlogFavoriteDO favorite = BlogFavoriteDO.builder()
                .type(type)
                .dataId(typeId)
                .userId(userId)
                .status(1)
                .build();
        favoriteMapper.insert(favorite);
        return favorite.getId();
    }

    @Override
    public void deleteFavoriteByTypeAndTypeId(BlogTypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        BlogFavoriteDO existing = favoriteMapper.selectByTypeAndDataId(type, typeId, userId);
        if (existing != null) {
            // 软取消：status 置 0
            favoriteMapper.updateById(BlogFavoriteDO.builder().id(existing.getId()).status(0).build());
        }
    }

    @Override
    public List<BlogFavoriteDO> getFavoriteListByCreator() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return favoriteMapper.selectListByUserId(userId);
    }

    @Override
    public Boolean isFavorite(BlogTypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        BlogFavoriteDO existing = favoriteMapper.selectByTypeAndDataId(type, typeId, userId);
        return existing != null && Objects.equals(existing.getStatus(), 1);
    }

    private void validateFavoriteExists(Long id) {
        if (favoriteMapper.selectById(id) == null) {
            throw exception(FAVORITE_NOT_EXISTS);
        }
    }

}
