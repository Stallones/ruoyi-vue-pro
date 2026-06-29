package com.sta.module.blog.controller.admin.link;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.link.vo.BlogLinkPageReqVO;
import com.sta.module.blog.controller.admin.link.vo.BlogLinkRespVO;
import com.sta.module.blog.controller.admin.link.vo.BlogLinkSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogLinkDO;
import com.sta.module.blog.service.BlogLinkService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客友链")
@RestController
@RequestMapping("/blog/link")
@Validated
public class BlogLinkController {

    @Resource
    private BlogLinkService linkService;

    @PostMapping("/create")
    @Operation(summary = "创建友链")
    @PreAuthorize("@ss.hasPermission('blog:link:create')")
    public CommonResult<Long> createLink(@Valid @RequestBody BlogLinkSaveReqVO createReqVO) {
        return success(linkService.createLink(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新友链")
    @PreAuthorize("@ss.hasPermission('blog:link:update')")
    public CommonResult<Boolean> updateLink(@Valid @RequestBody BlogLinkSaveReqVO updateReqVO) {
        linkService.updateLink(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除友链")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:link:delete')")
    public CommonResult<Boolean> deleteLink(@RequestParam("id") Long id) {
        linkService.deleteLink(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得友链")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('blog:link:query')")
    public CommonResult<BlogLinkRespVO> getLink(@RequestParam("id") Long id) {
        BlogLinkDO link = linkService.getLink(id);
        return success(BeanUtils.toBean(link, BlogLinkRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得友链分页")
    @PreAuthorize("@ss.hasPermission('blog:link:query')")
    public CommonResult<PageResult<BlogLinkRespVO>> getLinkPage(@Valid BlogLinkPageReqVO pageReqVO) {
        PageResult<BlogLinkDO> pageResult = linkService.getLinkPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BlogLinkRespVO.class));
    }

    @PutMapping("/update-check")
    @Operation(summary = "更新友链审核状态")
    @PreAuthorize("@ss.hasPermission('blog:link:update')")
    public CommonResult<Boolean> updateLinkCheck(@RequestParam("id") Long id,
                                                  @RequestParam("isCheck") Integer isCheck) {
        linkService.updateLinkCheck(id, isCheck);
        return success(true);
    }

}
