package com.sta.module.blog.controller.app.tag;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.app.tag.vo.AppTagRespVO;
import com.sta.module.blog.dal.dataobject.BlogTagDO;
import com.sta.module.blog.service.BlogTagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客标签")
@RestController
@RequestMapping("/blog/tag")
@Validated
@PermitAll
public class AppTagController {

    @Resource
    private BlogTagService tagService;

    @GetMapping("/list")
    @Operation(summary = "获得标签列表")
    public CommonResult<List<AppTagRespVO>> getTagList() {
        List<BlogTagDO> list = tagService.getTagList();
        return success(BeanUtils.toBean(list, AppTagRespVO.class));
    }

}
