package com.sta.module.blog.controller.app.favorite;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.app.favorite.vo.AppFavoriteReqVO;
import com.sta.module.blog.controller.app.favorite.vo.AppFavoriteRespVO;
import com.sta.module.blog.dal.dataobject.BlogFavoriteDO;
import com.sta.module.blog.service.BlogFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客收藏")
@RestController
@RequestMapping("/blog/favorite")
@Validated
public class AppFavoriteController {

    @Resource
    private BlogFavoriteService favoriteService;

    @PostMapping("/toggle")
    @Operation(summary = "切换收藏/取消收藏（无记录创建，有记录切换status）")
    public CommonResult<Boolean> toggleFavorite(@Valid @RequestBody AppFavoriteReqVO reqVO) {
        Long id = favoriteService.createFavorite(reqVO.getType(), reqVO.getTypeId());
        return success(id != null);
    }

    @PostMapping("/cancel")
    @Operation(summary = "取消收藏（status置0）")
    public CommonResult<Boolean> cancelFavorite(@Valid @RequestBody AppFavoriteReqVO reqVO) {
        favoriteService.deleteFavoriteByTypeAndTypeId(reqVO.getType(), reqVO.getTypeId());
        return success(true);
    }

    @GetMapping("/my-list")
    @Operation(summary = "获得我的收藏列表")
    public CommonResult<List<AppFavoriteRespVO>> getFavoriteList() {
        List<BlogFavoriteDO> list = favoriteService.getFavoriteListByCreator();
        return success(BeanUtils.toBean(list, AppFavoriteRespVO.class));
    }

    @GetMapping("/is-favorite")
    @Operation(summary = "是否已收藏")
    public CommonResult<Boolean> isFavorite(@Valid AppFavoriteReqVO reqVO) {
        return success(favoriteService.isFavorite(reqVO.getType(), reqVO.getTypeId()));
    }

}
