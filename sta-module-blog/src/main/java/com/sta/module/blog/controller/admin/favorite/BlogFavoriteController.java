package com.sta.module.blog.controller.admin.favorite;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.favorite.vo.BlogFavoritePageReqVO;
import com.sta.module.blog.controller.admin.favorite.vo.BlogFavoriteRespVO;
import com.sta.module.blog.dal.dataobject.BlogFavoriteDO;
import com.sta.module.blog.service.BlogFavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客收藏")
@RestController
@RequestMapping("/blog/favorite")
@Validated
public class BlogFavoriteController {

    @Resource
    private BlogFavoriteService favoriteService;

    @GetMapping("/page")
    @Operation(summary = "获得收藏分页")
    @PreAuthorize("@ss.hasPermission('blog:favorite:query')")
    public CommonResult<PageResult<BlogFavoriteRespVO>> getFavoritePage(@Valid BlogFavoritePageReqVO pageReqVO) {
        PageResult<BlogFavoriteDO> pageResult = favoriteService.getFavoritePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BlogFavoriteRespVO.class));
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收藏")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:favorite:delete')")
    public CommonResult<Boolean> deleteFavorite(@RequestParam("id") Long id) {
        favoriteService.deleteFavorite(id);
        return success(true);
    }

}
