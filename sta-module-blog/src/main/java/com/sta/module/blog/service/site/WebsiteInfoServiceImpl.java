package com.sta.module.blog.service.site;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.app.content.vo.AppArticleRespVO;
import com.sta.module.blog.controller.app.content.vo.AppCategoryRespVO;
import com.sta.module.blog.controller.app.content.vo.AppTagRespVO;
import com.sta.module.blog.controller.app.media.vo.AppImageRespVO;
import com.sta.module.blog.controller.app.site.vo.AppSnapshotRespVO;
import com.sta.module.blog.controller.app.site.vo.AppWebsiteInfoRespVO;
import com.sta.module.blog.controller.app.site.vo.AppLinkRespVO;
import com.sta.module.blog.dal.dataobject.content.ArticleDO;
import com.sta.module.blog.dal.dataobject.content.ArticleTagDO;
import com.sta.module.blog.dal.dataobject.content.CategoryDO;
import com.sta.module.blog.dal.dataobject.content.TagDO;
import com.sta.module.blog.dal.dataobject.interaction.CommentDO;
import com.sta.module.blog.dal.dataobject.interaction.FavoriteDO;
import com.sta.module.blog.dal.dataobject.interaction.LikeDO;
import com.sta.module.blog.dal.dataobject.interaction.MessageDO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.dal.mysql.content.ArticleMapper;
import com.sta.module.blog.dal.mysql.content.ArticleTagMapper;
import com.sta.module.blog.dal.mysql.content.CategoryMapper;
import com.sta.module.blog.dal.mysql.content.TagMapper;
import com.sta.module.blog.dal.mysql.interaction.CommentMapper;
import com.sta.module.blog.dal.mysql.interaction.FavoriteMapper;
import com.sta.module.blog.dal.mysql.interaction.LikeMapper;
import com.sta.module.blog.dal.mysql.interaction.MessageMapper;
import com.sta.module.blog.service.content.ArticleService;
import com.sta.module.blog.service.content.CategoryService;
import com.sta.module.blog.service.content.TagService;
import com.sta.module.blog.service.media.ImageService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 博客网站信息 Service 实现类
 * <p>
 * 职责：统计聚合 + 健康检查 + 快照生成（不再依赖 blog_website_info 表）
 */
@Service
public class WebsiteInfoServiceImpl implements WebsiteInfoService {

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private MessageMapper messageMapper;
    @Resource
    private LikeMapper likeMapper;
    @Resource
    private FavoriteMapper favoriteMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private ArticleService articleService;
    @Resource
    private CategoryService categoryService;
    @Resource
    private TagService tagService;
    @Resource
    private ImageService imageService;
    @Resource
    private LinkService linkService;

    @Override
    public AppWebsiteInfoRespVO getStats() {
        AppWebsiteInfoRespVO vo = new AppWebsiteInfoRespVO();
        vo.setArticleCount(articleMapper.selectCount(
                new LambdaQueryWrapperX<ArticleDO>().eq(ArticleDO::getStatus, 1)));
        vo.setCommentCount(commentMapper.selectCount(
                new LambdaQueryWrapperX<CommentDO>().eq(CommentDO::getStatus, 1)));
        vo.setMessageCount(messageMapper.selectCount(
                new LambdaQueryWrapperX<MessageDO>().eq(MessageDO::getStatus, 1)));
        vo.setLikeCount(likeMapper.selectCount(
                new LambdaQueryWrapperX<LikeDO>().eq(LikeDO::getStatus, 1)));
        vo.setFavoriteCount(favoriteMapper.selectCount(
                new LambdaQueryWrapperX<FavoriteDO>().eq(FavoriteDO::getStatus, 1)));
        vo.setCategoryCount(categoryMapper.selectCount());
        vo.setTagCount(tagMapper.selectCount());
        vo.setVisitCount(articleMapper.selectSumVisitCount());
        vo.setLastUpdateTime(articleMapper.selectMaxUpdateTime());
        return vo;
    }

    @Override
    public boolean healthCheck() {
        return true;
    }

    @Override
    public AppSnapshotRespVO getSnapshot() {
        AppSnapshotRespVO snapshot = new AppSnapshotRespVO();

        // 1. 网站统计（与 /get 接口同构）
        snapshot.setWebsiteInfo(getStats());

        // 2. 文章列表（status=1 已发布，含摘要，不含 content）
        List<ArticleDO> articles = articleService.getArticleListByStatus(1);
        snapshot.setArticles(articleService.convertToAppList(articles));

        // 3. 分类列表（含文章数统计）
        snapshot.setCategories(buildCategoryList());

        // 4. 标签列表（含文章数统计）
        snapshot.setTags(buildTagList());

        // 5. 图片列表（全量）
        List<ImageDO> images = imageService.getImageList();
        snapshot.setImages(BeanUtils.toBean(images, AppImageRespVO.class));

        // 6. 友链列表（已审核）
        snapshot.setLinks(BeanUtils.toBean(linkService.getLinkList(), AppLinkRespVO.class));

        return snapshot;
    }

    @Override
    public AppArticleRespVO getArticleFull(Long id) {
        ArticleDO article = articleService.getArticle(id);
        if (article == null) {
            return null;
        }
        return articleService.convertToAppVO(article);
    }

    // ========== 内部方法 ==========

    private List<AppCategoryRespVO> buildCategoryList() {
        List<CategoryDO> list = categoryService.getCategoryList();
        List<AppCategoryRespVO> result = BeanUtils.toBean(list, AppCategoryRespVO.class);
        for (AppCategoryRespVO vo : result) {
            Long count = articleMapper.selectCount(
                    new LambdaQueryWrapperX<ArticleDO>()
                            .eq(ArticleDO::getCategoryId, vo.getId())
                            .eq(ArticleDO::getStatus, 1));
            vo.setArticleCount(count.intValue());
        }
        return result.stream()
                .filter(vo -> vo.getArticleCount() != null && vo.getArticleCount() > 0)
                .sorted(Comparator.comparing(AppCategoryRespVO::getArticleCount).reversed())
                .collect(Collectors.toList());
    }

    private List<AppTagRespVO> buildTagList() {
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
        return result.stream()
                .filter(vo -> vo.getArticleCount() != null && vo.getArticleCount() > 0)
                .sorted(Comparator.comparing(AppTagRespVO::getArticleCount).reversed())
                .collect(Collectors.toList());
    }

}
