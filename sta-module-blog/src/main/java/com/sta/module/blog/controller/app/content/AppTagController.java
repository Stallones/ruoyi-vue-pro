package com.sta.module.blog.controller.app.content;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.app.content.vo.AppTagRespVO;
import com.sta.module.blog.dal.dataobject.content.ArticleDO;
import com.sta.module.blog.dal.dataobject.content.ArticleTagDO;
import com.sta.module.blog.dal.mysql.content.ArticleMapper;
import com.sta.module.blog.dal.mysql.content.ArticleTagMapper;
import com.sta.module.blog.dal.dataobject.content.TagDO;
import com.sta.module.blog.service.content.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 APP - 博客标签")
@RestController
@RequestMapping("/blog/tag")
@Validated
@PermitAll
public class AppTagController {

    @Resource
    private TagService tagService;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private ArticleMapper articleMapper;

    @GetMapping("/list")
    @Operation(summary = "获得标签列表")
    public CommonResult<List<AppTagRespVO>> getTagList() {
        List<TagDO> list = tagService.getTagList();
        List<AppTagRespVO> result = BeanUtils.toBean(list, AppTagRespVO.class);
        for (AppTagRespVO vo : result) {
            List<ArticleTagDO> articleTags = articleTagMapper.selectListByTagId(vo.getId());
            if (CollUtil.isEmpty(articleTags)) {
                vo.setArticleCount(0);
                continue;
            }
            List<Long> articleIds = articleTags.stream()
                    .map(ArticleTagDO::getArticleId)
                    .collect(Collectors.toList());
            Long count = articleMapper.selectCount(
                    new LambdaQueryWrapperX<ArticleDO>()
                            .in(ArticleDO::getId, articleIds)
                            .eq(ArticleDO::getStatus, 1));
            vo.setArticleCount(count.intValue());
        }
        // return success(result);


        return success(result.stream()
            .filter(vo -> vo.getArticleCount() != null && vo.getArticleCount() > 0)
            .sorted(Comparator.comparing(AppTagRespVO::getArticleCount).reversed())
            .collect(Collectors.toList()));
    }

}
