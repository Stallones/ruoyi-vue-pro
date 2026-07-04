package com.sta.module.blog.controller.app.site;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.app.site.vo.AppLinkApplyReqVO;
import com.sta.module.blog.controller.app.site.vo.AppLinkRespVO;
import com.sta.module.blog.dal.dataobject.site.LinkDO;
import com.sta.module.blog.service.site.LinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客友链")
@RestController
@RequestMapping("/blog/link")
@Validated
public class AppLinkController {

    @Resource
    private LinkService linkService;

    @GetMapping("/list")
    @PermitAll
    @Operation(summary = "获得友链列表（已审核）")
    public CommonResult<List<AppLinkRespVO>> getLinkList() {
        List<LinkDO> list = linkService.getLinkList();
        return success(BeanUtils.toBean(list, AppLinkRespVO.class));
    }

    @PostMapping("/apply")
    @PermitAll
    @Operation(summary = "申请友链")
    public CommonResult<Long> applyForLink(@Valid @RequestBody AppLinkApplyReqVO reqVO) {
        return success(linkService.applyForLink(reqVO.getName(), reqVO.getUrl(), reqVO.getDescription(), reqVO.getBackground(), reqVO.getEmail()));
    }

}
