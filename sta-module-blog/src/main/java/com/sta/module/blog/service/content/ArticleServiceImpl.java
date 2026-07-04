package com.sta.module.blog.service.content;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.controller.admin.content.vo.ArticlePageReqVO;
import com.sta.module.blog.controller.admin.content.vo.ArticleSaveReqVO;
import com.sta.module.blog.controller.app.content.vo.AppArticlePageReqVO;
import com.sta.module.blog.controller.app.content.vo.AppArticleRespVO;
import com.sta.module.blog.dal.dataobject.content.ArticleDO;
import com.sta.module.blog.dal.dataobject.content.ArticleTagDO;
import com.sta.module.blog.dal.dataobject.content.CategoryDO;
import com.sta.module.blog.dal.dataobject.interaction.FavoriteDO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.dal.dataobject.interaction.LikeDO;
import com.sta.module.blog.dal.dataobject.content.TagDO;
import com.sta.module.blog.dal.mysql.content.ArticleMapper;
import com.sta.module.blog.dal.mysql.content.ArticleTagMapper;
import com.sta.module.blog.dal.mysql.content.CategoryMapper;
import com.sta.module.blog.dal.mysql.interaction.CommentMapper;
import com.sta.module.blog.dal.mysql.interaction.FavoriteMapper;
import com.sta.module.blog.dal.mysql.media.ImageMapper;
import com.sta.module.blog.dal.mysql.interaction.LikeMapper;
import com.sta.module.blog.dal.mysql.content.TagMapper;
import com.sta.module.blog.enums.TypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.ARTICLE_NOT_EXISTS;

/**
 * 博客文章 Service 实现类
 */
@Service
@Validated
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private LikeMapper likeMapper;

    @Resource
    private FavoriteMapper favoriteMapper;

    // ========== 管理端 CRUD ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createArticle(ArticleSaveReqVO createReqVO) {
        ArticleDO article = BeanUtils.toBean(createReqVO, ArticleDO.class);
        articleMapper.insert(article);
        saveArticleTags(article.getId(), createReqVO.getTagIds());
        return article.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(ArticleSaveReqVO updateReqVO) {
        validateArticleExists(updateReqVO.getId());
        ArticleDO updateObj = BeanUtils.toBean(updateReqVO, ArticleDO.class);
        articleMapper.updateById(updateObj);
        articleTagMapper.deleteByArticleId(updateReqVO.getId());
        saveArticleTags(updateReqVO.getId(), updateReqVO.getTagIds());
    }

    @Override
    public void deleteArticle(Long id) {
        validateArticleExists(id);
        articleMapper.deleteById(id);
    }

    @Override
    public ArticleDO getArticle(Long id) {
        return articleMapper.selectById(id);
    }

    @Override
    public PageResult<ArticleDO> getArticlePage(ArticlePageReqVO pageReqVO) {
        return articleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ArticleDO> getArticleListByStatus(Integer status) {
        return articleMapper.selectListByStatus(status);
    }

    @Override
    public void updateArticleStatus(Long id, Integer status) {
        validateArticleExists(id);
        articleMapper.updateById(ArticleDO.builder().id(id).status(status).build());
    }

    private void validateArticleExists(Long id) {
        if (articleMapper.selectById(id) == null) {
            throw exception(ARTICLE_NOT_EXISTS);
        }
    }

    private void saveArticleTags(Long articleId, List<Long> tagIds) {
        if (CollUtil.isEmpty(tagIds)) {
            return;
        }
        Map<Long, String> tagNameMap = tagMapper.selectBatchIds(tagIds).stream()
                .collect(Collectors.toMap(TagDO::getId, TagDO::getTagName));
        List<ArticleTagDO> articleTags = tagIds.stream()
                .map(tagId -> ArticleTagDO.builder()
                        .articleId(articleId)
                        .tagId(tagId)
                        .tagName(tagNameMap.getOrDefault(tagId, ""))
                        .build())
                .collect(Collectors.toList());
        articleTagMapper.insertBatch(articleTags);
    }

    // ========== App 专用方法 ==========

    @Override
    public void addVisitCount(Long id) {
        ArticleDO article = articleMapper.selectById(id);
        if (article == null) {
            throw exception(ARTICLE_NOT_EXISTS);
        }
        articleMapper.updateById(ArticleDO.builder().id(id).visitCount(article.getVisitCount() + 1).build());
    }

    @Override
    public PageResult<ArticleDO> getArticlePageAdmin(ArticlePageReqVO pageReqVO) {
        return articleMapper.selectPageAdmin(pageReqVO);
    }

    @Override
    public PageResult<ArticleDO> getArticlePageApp(AppArticlePageReqVO pageReqVO) {
        return articleMapper.selectPageApp(pageReqVO);
    }

    @Override
    public List<ArticleDO> getArticleListByCreateTime() {
        return articleMapper.selectListByCreateTime();
    }

    @Override
    public PageResult<ArticleDO> getArticlePageByCreateTime(AppArticlePageReqVO pageReqVO) {
        return articleMapper.selectPageApp(pageReqVO);
    }

    @Override
    public List<ArticleDO> getArticleListByCategory(Long categoryId) {
        return articleMapper.selectList(new LambdaQueryWrapperX<ArticleDO>()
                .eq(ArticleDO::getStatus, 1)
                .eq(ArticleDO::getCategoryId, categoryId)
                .orderByDesc(ArticleDO::getCreateTime));
    }

    @Override
    public List<ArticleDO> getArticleListByTag(Long tagId) {
        List<ArticleTagDO> articleTags = articleTagMapper.selectListByTagId(tagId);
        if (CollUtil.isEmpty(articleTags)) {
            return List.of();
        }
        List<Long> articleIds = articleTags.stream().map(ArticleTagDO::getArticleId).collect(Collectors.toList());
        return articleMapper.selectBatchIds(articleIds).stream()
                .filter(a -> a.getStatus() == 1)
                .sorted((a, b) -> b.getCreateTime().compareTo(a.getCreateTime()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ArticleDO> getArticleListByVisitCount() {
        return articleMapper.selectListByVisitCount(5);
    }

    @Override
    public List<ArticleDO> getArticleListByCategoryId(Long categoryId, Long articleId) {
        return articleMapper.selectListByCategoryId(categoryId, articleId, 5);
    }

    @Override
    public List<ArticleDO> getArticleListByTitleAndContent(String keyword) {
        return articleMapper.selectListByTitleAndContent(keyword);
    }

    // ========== App VO 装配方法 ==========

    @Override
    public AppArticleRespVO convertToAppVO(ArticleDO article) {
        return convertToAppVO(article, 200, true);
    }

    /**
     * 核心装配方法：支持控制 summary 长度和是否返回完整 content
     */
    private AppArticleRespVO convertToAppVO(ArticleDO article, int summaryLength, boolean includeContent) {
        if (article == null) {
            return null;
        }
        AppArticleRespVO vo = new AppArticleRespVO();
        vo.setId(article.getId());
        vo.setCategoryId(article.getCategoryId());
        vo.setTitle(article.getTitle());
        vo.setVisitCount(article.getVisitCount());
        vo.setCreateTime(article.getCreateTime());

        // 内容处理
        if (includeContent && article.getContent() != null) {
            vo.setContent(article.getContent());
        }
        if (article.getContent() != null) {
            String plainText = StrUtil.cleanBlank(article.getContent());
            vo.setWordCount(plainText.length());
            vo.setSummary(StrUtil.sub(article.getContent(), 0, summaryLength));
        }

        // 装配关联数据
        assembleArticleExtras(vo, article);
        return vo;
    }

    /**
     * 装配文章额外信息（封面、分类、标签、点赞/收藏/评论数、用户状态）
     */
    private void assembleArticleExtras(AppArticleRespVO vo, ArticleDO article) {
        Long articleId = article.getId();

        // 封面图：从 blog_image 按 type=51(cover) + data_id=articleId 查询
        List<ImageDO> coverImages = imageMapper.selectListByType(TypeEnum.IMG_COVER, articleId);
        if (CollUtil.isNotEmpty(coverImages)) {
            vo.setCoverPath(coverImages.get(0).getPath());
        }

        // 分类名称
        if (article.getCategoryId() != null) {
            CategoryDO category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getCategoryName());
            }
        }

        // 标签
        List<ArticleTagDO> articleTags = articleTagMapper.selectListByArticleId(articleId);
        if (CollUtil.isNotEmpty(articleTags)) {
            vo.setTagIds(articleTags.stream().map(ArticleTagDO::getTagId).collect(Collectors.toList()));
            vo.setTagNames(articleTags.stream().map(ArticleTagDO::getTagName).collect(Collectors.toList()));
        } else {
            vo.setTagIds(Collections.emptyList());
            vo.setTagNames(Collections.emptyList());
        }

        // 评论数
        vo.setCommentCount(commentMapper.selectCountByArticleId(articleId));

        // 点赞数
        vo.setLikeCount(likeMapper.selectCountByType(TypeEnum.ART, articleId));

        // 收藏数
        vo.setFavoriteCount(favoriteMapper.selectCountByType(TypeEnum.ART, articleId));

        // 当前用户是否已点赞/收藏
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        if (userId != null) {
            LikeDO userLike = likeMapper.selectByType(TypeEnum.ART, articleId, userId);
            vo.setIsLiked(userLike != null && Objects.equals(userLike.getStatus(), 1));

            FavoriteDO userFavorite = favoriteMapper.selectByType(TypeEnum.ART, articleId, userId);
            vo.setIsFavorited(userFavorite != null && Objects.equals(userFavorite.getStatus(), 1));
        }
    }

    @Override
    public List<AppArticleRespVO> convertToAppList(List<ArticleDO> list) {
        return convertToAppList(list, 200);
    }

    private List<AppArticleRespVO> convertToAppList(List<ArticleDO> list, int summaryLength) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return batchAssembleAppVO(list, summaryLength);
    }

    @Override
    public PageResult<AppArticleRespVO> convertToAppPage(PageResult<ArticleDO> pageResult) {
        if (pageResult == null || CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty();
        }
        List<AppArticleRespVO> voList = batchAssembleAppVO(pageResult.getList(), 200);
        return new PageResult<>(voList, pageResult.getTotal());
    }

    /**
     * 批量装配：预加载所有关联数据以提升性能
     */
    private List<AppArticleRespVO> batchAssembleAppVO(List<ArticleDO> articles, int summaryLength) {
        List<Long> articleIds = articles.stream().map(ArticleDO::getId).collect(Collectors.toList());
        List<Long> categoryIds = articles.stream()
                .map(ArticleDO::getCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 批量加载封面图：type=51(cover), data_id in articleIds
        // 批量加载封面图：type=51(cover), data_id in articleIds
        final Map<Long, String> coverPathMap = buildCoverPathMap(articleIds);

        // 批量加载分类名
        final Map<Long, String> categoryNameMap = buildCategoryNameMap(categoryIds);

        // 批量加载标签
        final Map<Long, List<ArticleTagDO>> articleTagsMap = buildArticleTagsMap(articleIds);

        // 批量加载评论数
        final Map<Long, Long> commentCountMap = buildCommentCountMap(articleIds);

        // 批量加载点赞/收藏数
        final Map<Long, Long> likeCountMap = buildLikeCountMap(articleIds);
        final Map<Long, Long> favoriteCountMap = buildFavoriteCountMap(articleIds);

        // 当前用户点赞/收藏状态
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        final Map<Long, Boolean> userLikeMap = buildUserLikeMap(articleIds, userId);
        final Map<Long, Boolean> userFavoriteMap = buildUserFavoriteMap(articleIds, userId);

        return articles.stream().map(article -> {
            AppArticleRespVO vo = new AppArticleRespVO();
            vo.setId(article.getId());
            vo.setCategoryId(article.getCategoryId());
            vo.setTitle(article.getTitle());
            vo.setVisitCount(article.getVisitCount());
            vo.setCreateTime(article.getCreateTime());

            // content/summary/wordCount
            if (article.getContent() != null) {
                String plainText = StrUtil.cleanBlank(article.getContent());
                vo.setWordCount(plainText.length());
                vo.setSummary(StrUtil.sub(article.getContent(), 0, summaryLength));
            }

            // 封面图
            vo.setCoverPath(coverPathMap.get(article.getId()));

            // 分类名
            if (article.getCategoryId() != null) {
                vo.setCategoryName(categoryNameMap.get(article.getCategoryId()));
            }

            // 标签
            List<ArticleTagDO> tags = articleTagsMap.getOrDefault(article.getId(), Collections.emptyList());
            if (CollUtil.isNotEmpty(tags)) {
                vo.setTagIds(tags.stream().map(ArticleTagDO::getTagId).collect(Collectors.toList()));
                vo.setTagNames(tags.stream().map(ArticleTagDO::getTagName).collect(Collectors.toList()));
            } else {
                vo.setTagIds(Collections.emptyList());
                vo.setTagNames(Collections.emptyList());
            }

            // 统计
            vo.setCommentCount(commentCountMap.getOrDefault(article.getId(), 0L));
            vo.setLikeCount(likeCountMap.getOrDefault(article.getId(), 0L));
            vo.setFavoriteCount(favoriteCountMap.getOrDefault(article.getId(), 0L));

            // 用户状态
            vo.setIsLiked(userLikeMap.getOrDefault(article.getId(), false));
            vo.setIsFavorited(userFavoriteMap.getOrDefault(article.getId(), false));

            return vo;
        }).collect(Collectors.toList());
    }

    private Map<Long, String> buildCoverPathMap(List<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        List<ImageDO> allCoverImages = imageMapper.selectList(
                new LambdaQueryWrapperX<ImageDO>()
                        .eq(ImageDO::getType, TypeEnum.IMG_COVER)
                        .in(ImageDO::getDataId, articleIds)
        );
        return allCoverImages.stream()
                .collect(Collectors.toMap(ImageDO::getDataId, ImageDO::getPath, (a, b) -> a));
    }

    private Map<Long, String> buildCategoryNameMap(List<Long> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return Collections.emptyMap();
        }
        return categoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getCategoryName));
    }

    private Map<Long, List<ArticleTagDO>> buildArticleTagsMap(List<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        List<ArticleTagDO> allArticleTags = articleTagMapper.selectList(
                new LambdaQueryWrapperX<ArticleTagDO>().in(ArticleTagDO::getArticleId, articleIds)
        );
        return allArticleTags.stream()
                .collect(Collectors.groupingBy(ArticleTagDO::getArticleId));
    }

    private Map<Long, Long> buildCommentCountMap(List<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        return articleIds.stream()
                .collect(Collectors.toMap(id -> id, commentMapper::selectCountByArticleId));
    }

    private Map<Long, Long> buildLikeCountMap(List<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        return articleIds.stream()
                .collect(Collectors.toMap(id -> id, id -> likeMapper.selectCountByType(TypeEnum.ART, id)));
    }

    private Map<Long, Long> buildFavoriteCountMap(List<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        return articleIds.stream()
                .collect(Collectors.toMap(id -> id, id -> favoriteMapper.selectCountByType(TypeEnum.ART, id)));
    }

    private Map<Long, Boolean> buildUserLikeMap(List<Long> articleIds, Long userId) {
        if (userId == null || CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        Map<Long, Boolean> map = new HashMap<>();
        for (Long articleId : articleIds) {
            LikeDO userLike = likeMapper.selectByType(TypeEnum.ART, articleId, userId);
            map.put(articleId, userLike != null && Objects.equals(userLike.getStatus(), 1));
        }
        return map;
    }

    private Map<Long, Boolean> buildUserFavoriteMap(List<Long> articleIds, Long userId) {
        if (userId == null || CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        Map<Long, Boolean> map = new HashMap<>();
        for (Long articleId : articleIds) {
            FavoriteDO userFavorite = favoriteMapper.selectByType(TypeEnum.ART, articleId, userId);
            map.put(articleId, userFavorite != null && Objects.equals(userFavorite.getStatus(), 1));
        }
        return map;
    }

}
