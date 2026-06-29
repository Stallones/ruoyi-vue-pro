package com.sta.module.blog.controller.admin.comment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.comment.vo.BlogCommentPageReqVO;
import com.sta.module.blog.controller.admin.comment.vo.BlogCommentRespVO;
import com.sta.module.blog.dal.dataobject.BlogCommentDO;
import com.sta.module.blog.service.BlogCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "博客评论")
@RestController
@RequestMapping("/blog/comment")
@Validated
public class BlogCommentController {

    @Resource
    private BlogCommentService commentService;

    @GetMapping("/page")
    @Operation(summary = "获得评论分页")
    @PreAuthorize("@ss.hasPermission('blog:comment:query')")
    public CommonResult<PageResult<BlogCommentRespVO>> getCommentPage(@Valid BlogCommentPageReqVO pageReqVO) {
        PageResult<BlogCommentDO> pageResult = commentService.getCommentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BlogCommentRespVO.class));
    }

    @GetMapping("/get")
    @Operation(summary = "获得评论")
    @Parameter(name = "id", description = "编号", required = true, example = "1")
    @PreAuthorize("@ss.hasPermission('blog:comment:query')")
    public CommonResult<BlogCommentRespVO> getComment(@RequestParam("id") Long id) {
        BlogCommentDO comment = commentService.getComment(id);
        return success(BeanUtils.toBean(comment, BlogCommentRespVO.class));
    }

    @PutMapping("/update-check")
    @Operation(summary = "更新评论审核状态")
    @PreAuthorize("@ss.hasPermission('blog:comment:update')")
    public CommonResult<Boolean> updateCommentCheck(@RequestParam("id") Long id,
                                                     @RequestParam("isCheck") Integer isCheck) {
        commentService.updateCommentCheck(id, isCheck);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除评论")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('blog:comment:delete')")
    public CommonResult<Boolean> deleteComment(@RequestParam("id") Long id) {
        commentService.deleteComment(id);
        return success(true);
    }

}
