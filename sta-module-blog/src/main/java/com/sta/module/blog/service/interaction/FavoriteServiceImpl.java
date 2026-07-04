package com.sta.module.blog.service.interaction;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.controller.admin.interaction.vo.FavoritePageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.FavoriteDO;
import com.sta.module.blog.dal.mysql.interaction.FavoriteMapper;
import com.sta.module.blog.enums.TypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.FAVORITE_NOT_EXISTS;

@Service
@Validated
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    private FavoriteMapper favoriteMapper;

    @Override
    public PageResult<FavoriteDO> getFavoritePage(FavoritePageReqVO pageReqVO) {
        return favoriteMapper.selectPage(pageReqVO);
    }

    @Override
    public void deleteFavorite(Long id) {
        validateFavoriteExists(id);
        favoriteMapper.deleteById(id);
    }

    @Override
    public Long createFavorite(TypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        FavoriteDO existing = favoriteMapper.selectByType(type, typeId, userId);
        if (existing != null) {
            // 已有记录：重新激活
            favoriteMapper.updateById(FavoriteDO.builder().id(existing.getId()).status(1).build());
            return existing.getId();
        }
        // 无记录：创建新记录
        FavoriteDO favorite = FavoriteDO.builder()
                .type(type)
                .dataId(typeId)
                .userId(userId)
                .status(1)
                .build();
        favoriteMapper.insert(favorite);
        return favorite.getId();
    }

    @Override
    public void deleteFavoriteByTypeAndTypeId(TypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        FavoriteDO existing = favoriteMapper.selectByType(type, typeId, userId);
        if (existing != null) {
            // 软取消：status 置 0
            favoriteMapper.updateById(FavoriteDO.builder().id(existing.getId()).status(0).build());
        }
    }

    @Override
    public List<FavoriteDO> getFavoriteListByCreator() {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        return favoriteMapper.selectListByUserId(userId);
    }

    @Override
    public Boolean isFavorite(TypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        FavoriteDO existing = favoriteMapper.selectByType(type, typeId, userId);
        return existing != null && Objects.equals(existing.getStatus(), 1);
    }

    private void validateFavoriteExists(Long id) {
        if (favoriteMapper.selectById(id) == null) {
            throw exception(FAVORITE_NOT_EXISTS);
        }
    }

}
