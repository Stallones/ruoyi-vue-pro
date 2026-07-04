package com.sta.module.blog.controller.admin.content;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.content.vo.TagPageReqVO;
import com.sta.module.blog.controller.admin.content.vo.TagRespVO;
import com.sta.module.blog.controller.admin.content.vo.TagSaveReqVO;
import com.sta.module.blog.dal.dataobject.content.TagDO;
import com.sta.module.blog.service.content.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客标签")
@RestController
@RequestMapping("/blog/tag")
@Validated
public class TagController {

    @Resource
    private TagService tagService;

    @PostMapping("/create")
    @Operation(summary = "创建标签")
    @PreAuthorize("@ss.hasPermission('blog:tag:create')")
    public CommonResult<Long> createTag(@Valid @RequestBody TagSaveReqVO createReqVO) {
        return success(tagService.createTag(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新标签")
    @PreAuthorize("@ss.hasPermission('blog:tag:update')")
    public CommonResult<Boolean> updateTag(@Valid @RequestBody TagSaveReqVO updateReqVO) {
        tagService.updateTag(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除标签")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:tag:delete')")
    public CommonResult<Boolean> deleteTag(@RequestParam("id") Long id) {
        tagService.deleteTag(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得标签")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('blog:tag:query')")
    public CommonResult<TagRespVO> getTag(@RequestParam("id") Long id) {
        TagDO tag = tagService.getTag(id);
        return success(BeanUtils.toBean(tag, TagRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得标签分页")
    @PreAuthorize("@ss.hasPermission('blog:tag:query')")
    public CommonResult<PageResult<TagRespVO>> getTagPage(@Valid TagPageReqVO pageReqVO) {
        PageResult<TagDO> pageResult = tagService.getTagPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, TagRespVO.class));
    }

}
