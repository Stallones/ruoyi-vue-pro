package com.sta.module.blog.controller.app.link;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.app.link.vo.AppLinkApplyReqVO;
import com.sta.module.blog.controller.app.link.vo.AppLinkRespVO;
import com.sta.module.blog.dal.dataobject.BlogLinkDO;
import com.sta.module.blog.service.BlogLinkService;
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
    private BlogLinkService linkService;

    @GetMapping("/list")
    @PermitAll
    @Operation(summary = "获得友链列表（已审核）")
    public CommonResult<List<AppLinkRespVO>> getLinkList() {
        List<BlogLinkDO> list = linkService.getLinkList();
        return success(BeanUtils.toBean(list, AppLinkRespVO.class));
    }

    @PostMapping("/apply")
    @PermitAll
    @Operation(summary = "申请友链")
    public CommonResult<Long> applyLink(@Valid @RequestBody AppLinkApplyReqVO reqVO) {
        return success(linkService.applyLink(reqVO.getName(), reqVO.getUrl(), reqVO.getDescription(), reqVO.getBackground(), reqVO.getEmail()));
    }

}
