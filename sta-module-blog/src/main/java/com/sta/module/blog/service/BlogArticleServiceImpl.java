package com.sta.module.blog.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.controller.admin.article.vo.BlogArticlePageReqVO;
import com.sta.module.blog.controller.admin.article.vo.BlogArticleSaveReqVO;
import com.sta.module.blog.controller.app.article.vo.AppArticlePageReqVO;
import com.sta.module.blog.controller.app.article.vo.AppArticleRespVO;
import com.sta.module.blog.dal.dataobject.BlogArticleDO;
import com.sta.module.blog.dal.dataobject.BlogArticleTagDO;
import com.sta.module.blog.dal.dataobject.BlogCategoryDO;
import com.sta.module.blog.dal.dataobject.BlogFavoriteDO;
import com.sta.module.blog.dal.dataobject.BlogImageDO;
import com.sta.module.blog.dal.dataobject.BlogLikeDO;
import com.sta.module.blog.dal.dataobject.BlogTagDO;
import com.sta.module.blog.dal.mysql.BlogArticleMapper;
import com.sta.module.blog.dal.mysql.BlogArticleTagMapper;
import com.sta.module.blog.dal.mysql.BlogCategoryMapper;
import com.sta.module.blog.dal.mysql.BlogCommentMapper;
import com.sta.module.blog.dal.mysql.BlogFavoriteMapper;
import com.sta.module.blog.dal.mysql.BlogImageMapper;
import com.sta.module.blog.dal.mysql.BlogLikeMapper;
import com.sta.module.blog.dal.mysql.BlogTagMapper;
import com.sta.module.blog.enums.ArchiveTypeEnum;
import com.sta.module.blog.enums.BlogTypeEnum;
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
public class BlogArticleServiceImpl implements BlogArticleService {

    @Resource
    private BlogArticleMapper articleMapper;

    @Resource
    private BlogArticleTagMapper articleTagMapper;

    @Resource
    private BlogCategoryMapper categoryMapper;

    @Resource
    private BlogImageMapper imageMapper;

    @Resource
    private BlogCommentMapper commentMapper;

    @Resource
    private BlogTagMapper tagMapper;

    @Resource
    private BlogLikeMapper likeMapper;

    @Resource
    private BlogFavoriteMapper favoriteMapper;

    // ========== 管理端 CRUD ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createArticle(BlogArticleSaveReqVO createReqVO) {
        BlogArticleDO article = BeanUtils.toBean(createReqVO, BlogArticleDO.class);
        articleMapper.insert(article);
        saveArticleTags(article.getId(), createReqVO.getTagIds());
        return article.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateArticle(BlogArticleSaveReqVO updateReqVO) {
        validateArticleExists(updateReqVO.getId());
        BlogArticleDO updateObj = BeanUtils.toBean(updateReqVO, BlogArticleDO.class);
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
    public BlogArticleDO getArticle(Long id) {
        return articleMapper.selectById(id);
    }

    @Override
    public PageResult<BlogArticleDO> getArticlePage(BlogArticlePageReqVO pageReqVO) {
        return articleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BlogArticleDO> getArticleListByStatus(Integer status) {
        return articleMapper.selectListByStatus(status);
    }

    @Override
    public void updateArticleStatus(Long id, Integer status) {
        validateArticleExists(id);
        articleMapper.updateById(BlogArticleDO.builder().id(id).status(status).build());
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
                .collect(Collectors.toMap(BlogTagDO::getId, BlogTagDO::getTagName));
        List<BlogArticleTagDO> articleTags = tagIds.stream()
                .map(tagId -> BlogArticleTagDO.builder()
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
        BlogArticleDO article = articleMapper.selectById(id);
        if (article == null) {
            throw exception(ARTICLE_NOT_EXISTS);
        }
        articleMapper.updateById(BlogArticleDO.builder().id(id).visitCount(article.getVisitCount() + 1).build());
    }

    @Override
    public PageResult<BlogArticleDO> getFrontPage(BlogArticlePageReqVO pageReqVO) {
        return articleMapper.selectFrontPage(pageReqVO);
    }

    @Override
    public PageResult<BlogArticleDO> getFrontPage(AppArticlePageReqVO pageReqVO) {
        return articleMapper.selectFrontPage(pageReqVO);
    }

    @Override
    public List<BlogArticleDO> getTimelineList() {
        return articleMapper.selectTimelineList();
    }

    @Override
    public List<BlogArticleDO> getArchiveArticleList(String archiveType, Long archiveId) {
        if (ArchiveTypeEnum.TAG.getValue().equals(archiveType)) {
            List<BlogArticleTagDO> articleTags = articleTagMapper.selectListByTagId(archiveId);
            if (CollUtil.isEmpty(articleTags)) {
                return List.of();
            }
            List<Long> articleIds = articleTags.stream().map(BlogArticleTagDO::getArticleId).collect(Collectors.toList());
            return articleMapper.selectBatchIds(articleIds).stream()
                    .filter(a -> a.getStatus() == 1)
                    .collect(Collectors.toList());
        }
        return articleMapper.selectListByArchive(archiveType, archiveId);
    }

    @Override
    public List<BlogArticleDO> getRecommendArticleList() {
        return articleMapper.selectRecommendList(5);
    }

    @Override
    public List<BlogArticleDO> getRandomArticleList(Integer limit) {
        List<BlogArticleDO> allArticles = articleMapper.selectListByStatus(1);
        if (CollUtil.isEmpty(allArticles)) {
            return List.of();
        }
        int size = Math.min(limit != null ? limit : 5, allArticles.size());
        return allArticles.subList(0, size);
    }

    @Override
    public List<BlogArticleDO> getRelatedArticleList(Long categoryId, Long articleId) {
        return articleMapper.selectRelatedList(categoryId, articleId, 5);
    }

    @Override
    public List<BlogArticleDO> getSearchTitleList() {
        return articleMapper.selectSearchTitleList();
    }

    @Override
    public List<BlogArticleDO> searchArticleByContent(String keyword) {
        return articleMapper.selectSearchByContent(keyword);
    }

    // ========== App VO 装配方法 ==========

    @Override
    public AppArticleRespVO convertToAppVO(BlogArticleDO article) {
        return convertToAppVO(article, 200, true);
    }

    /**
     * 核心装配方法：支持控制 summary 长度和是否返回完整 content
     */
    private AppArticleRespVO convertToAppVO(BlogArticleDO article, int summaryLength, boolean includeContent) {
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
    private void assembleArticleExtras(AppArticleRespVO vo, BlogArticleDO article) {
        Long articleId = article.getId();

        // 封面图：从 blog_image 按 type=51(cover) + data_id=articleId 查询
        List<BlogImageDO> coverImages = imageMapper.selectListByTypeAndDataId(BlogTypeEnum.IMG_COVER, articleId);
        if (CollUtil.isNotEmpty(coverImages)) {
            vo.setCoverPath(coverImages.get(0).getPath());
        }

        // 分类名称
        if (article.getCategoryId() != null) {
            BlogCategoryDO category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getCategoryName());
            }
        }

        // 标签
        List<BlogArticleTagDO> articleTags = articleTagMapper.selectListByArticleId(articleId);
        if (CollUtil.isNotEmpty(articleTags)) {
            vo.setTagIds(articleTags.stream().map(BlogArticleTagDO::getTagId).collect(Collectors.toList()));
            vo.setTagNames(articleTags.stream().map(BlogArticleTagDO::getTagName).collect(Collectors.toList()));
        } else {
            vo.setTagIds(Collections.emptyList());
            vo.setTagNames(Collections.emptyList());
        }

        // 评论数
        vo.setCommentCount(commentMapper.selectCountByArticleId(articleId));

        // 点赞数
        vo.setLikeCount(likeMapper.selectCountByTypeAndDataId(BlogTypeEnum.ART, articleId));

        // 收藏数
        vo.setFavoriteCount(favoriteMapper.selectCountByTypeAndDataId(BlogTypeEnum.ART, articleId));

        // 当前用户是否已点赞/收藏
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        if (userId != null) {
            BlogLikeDO userLike = likeMapper.selectByTypeAndDataId(BlogTypeEnum.ART, articleId, userId);
            vo.setIsLiked(userLike != null && Objects.equals(userLike.getStatus(), 1));

            BlogFavoriteDO userFavorite = favoriteMapper.selectByTypeAndDataId(BlogTypeEnum.ART, articleId, userId);
            vo.setIsFavorited(userFavorite != null && Objects.equals(userFavorite.getStatus(), 1));
        }
    }

    @Override
    public List<AppArticleRespVO> convertToAppList(List<BlogArticleDO> list) {
        return convertToAppList(list, 200);
    }

    private List<AppArticleRespVO> convertToAppList(List<BlogArticleDO> list, int summaryLength) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return batchAssembleAppVO(list, summaryLength);
    }

    @Override
    public PageResult<AppArticleRespVO> convertToAppPage(PageResult<BlogArticleDO> pageResult) {
        if (pageResult == null || CollUtil.isEmpty(pageResult.getList())) {
            return PageResult.empty();
        }
        List<AppArticleRespVO> voList = batchAssembleAppVO(pageResult.getList(), 200);
        return new PageResult<>(voList, pageResult.getTotal());
    }

    /**
     * 批量装配：预加载所有关联数据以提升性能
     */
    private List<AppArticleRespVO> batchAssembleAppVO(List<BlogArticleDO> articles, int summaryLength) {
        List<Long> articleIds = articles.stream().map(BlogArticleDO::getId).collect(Collectors.toList());
        List<Long> categoryIds = articles.stream()
                .map(BlogArticleDO::getCategoryId)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        // 批量加载封面图：type=51(cover), data_id in articleIds
        // 批量加载封面图：type=51(cover), data_id in articleIds
        final Map<Long, String> coverPathMap = buildCoverPathMap(articleIds);

        // 批量加载分类名
        final Map<Long, String> categoryNameMap = buildCategoryNameMap(categoryIds);

        // 批量加载标签
        final Map<Long, List<BlogArticleTagDO>> articleTagsMap = buildArticleTagsMap(articleIds);

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
            List<BlogArticleTagDO> tags = articleTagsMap.getOrDefault(article.getId(), Collections.emptyList());
            if (CollUtil.isNotEmpty(tags)) {
                vo.setTagIds(tags.stream().map(BlogArticleTagDO::getTagId).collect(Collectors.toList()));
                vo.setTagNames(tags.stream().map(BlogArticleTagDO::getTagName).collect(Collectors.toList()));
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
        List<BlogImageDO> allCoverImages = imageMapper.selectList(
                new LambdaQueryWrapperX<BlogImageDO>()
                        .eq(BlogImageDO::getType, BlogTypeEnum.IMG_COVER)
                        .in(BlogImageDO::getDataId, articleIds)
        );
        return allCoverImages.stream()
                .collect(Collectors.toMap(BlogImageDO::getDataId, BlogImageDO::getPath, (a, b) -> a));
    }

    private Map<Long, String> buildCategoryNameMap(List<Long> categoryIds) {
        if (CollUtil.isEmpty(categoryIds)) {
            return Collections.emptyMap();
        }
        return categoryMapper.selectBatchIds(categoryIds).stream()
                .collect(Collectors.toMap(BlogCategoryDO::getId, BlogCategoryDO::getCategoryName));
    }

    private Map<Long, List<BlogArticleTagDO>> buildArticleTagsMap(List<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        List<BlogArticleTagDO> allArticleTags = articleTagMapper.selectList(
                new LambdaQueryWrapperX<BlogArticleTagDO>().in(BlogArticleTagDO::getArticleId, articleIds)
        );
        return allArticleTags.stream()
                .collect(Collectors.groupingBy(BlogArticleTagDO::getArticleId));
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
                .collect(Collectors.toMap(id -> id, id -> likeMapper.selectCountByTypeAndDataId(BlogTypeEnum.ART, id)));
    }

    private Map<Long, Long> buildFavoriteCountMap(List<Long> articleIds) {
        if (CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        return articleIds.stream()
                .collect(Collectors.toMap(id -> id, id -> favoriteMapper.selectCountByTypeAndDataId(BlogTypeEnum.ART, id)));
    }

    private Map<Long, Boolean> buildUserLikeMap(List<Long> articleIds, Long userId) {
        if (userId == null || CollUtil.isEmpty(articleIds)) {
            return Collections.emptyMap();
        }
        Map<Long, Boolean> map = new HashMap<>();
        for (Long articleId : articleIds) {
            BlogLikeDO userLike = likeMapper.selectByTypeAndDataId(BlogTypeEnum.ART, articleId, userId);
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
            BlogFavoriteDO userFavorite = favoriteMapper.selectByTypeAndDataId(BlogTypeEnum.ART, articleId, userId);
            map.put(articleId, userFavorite != null && Objects.equals(userFavorite.getStatus(), 1));
        }
        return map;
    }

}
