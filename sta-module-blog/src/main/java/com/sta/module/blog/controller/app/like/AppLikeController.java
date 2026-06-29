package com.sta.module.blog.controller.app.like;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.sta.module.blog.controller.app.like.vo.AppLikeReqVO;
import com.sta.module.blog.service.BlogLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客点赞")
@RestController
@RequestMapping("/blog/like")
@Validated
public class AppLikeController {

    @Resource
    private BlogLikeService likeService;

    @PostMapping("/toggle")
    @Operation(summary = "切换点赞/取消点赞（无记录创建，有记录切换status）")
    public CommonResult<Boolean> toggleLike(@Valid @RequestBody AppLikeReqVO reqVO) {
        return success(likeService.toggleLike(reqVO.getType(), reqVO.getTypeId()));
    }

    @GetMapping("/is-like")
    @Operation(summary = "是否已点赞")
    public CommonResult<Boolean> isLike(@Valid AppLikeReqVO reqVO) {
        return success(likeService.isLike(reqVO.getType(), reqVO.getTypeId()));
    }

}
