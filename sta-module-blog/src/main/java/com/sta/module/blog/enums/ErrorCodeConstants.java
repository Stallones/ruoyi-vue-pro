package com.sta.module.blog.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * Blog 错误码枚举类
 * <p>
 * blog 系统，使用 1-031-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 文章管理（1-031-100-000） ==========
    ErrorCode ARTICLE_NOT_EXISTS = new ErrorCode(1_031_100_000, "文章不存在");
    ErrorCode ARTICLE_TITLE_DUPLICATE = new ErrorCode(1_031_100_001, "文章标题已存在");

    // ========== 标签管理（1-031-101-000） ==========
    ErrorCode TAG_NOT_EXISTS = new ErrorCode(1_031_101_000, "标签不存在");
    ErrorCode TAG_NAME_DUPLICATE = new ErrorCode(1_031_101_001, "标签名称已存在");

    // ========== 分类管理（1-031-102-000） ==========
    ErrorCode CATEGORY_NOT_EXISTS = new ErrorCode(1_031_102_000, "分类不存在");
    ErrorCode CATEGORY_NAME_DUPLICATE = new ErrorCode(1_031_102_001, "分类名称已存在");

    // ========== 评论管理（1-031-103-000） ==========
    ErrorCode COMMENT_NOT_EXISTS = new ErrorCode(1_031_103_000, "评论不存在");

    // ========== 友链管理（1-031-104-000） ==========
    ErrorCode LINK_NOT_EXISTS = new ErrorCode(1_031_104_000, "友链不存在");
    ErrorCode LINK_NAME_DUPLICATE = new ErrorCode(1_031_104_001, "友链名称已存在");

    // ========== 留言管理（1-031-107-000） ==========
    ErrorCode MESSAGE_NOT_EXISTS = new ErrorCode(1_031_107_000, "留言不存在");

    // ========== 收藏管理（1-031-108-000） ==========
    ErrorCode FAVORITE_NOT_EXISTS = new ErrorCode(1_031_108_000, "收藏不存在");
    ErrorCode FAVORITE_ALREADY_EXISTS = new ErrorCode(1_031_108_001, "已收藏，请勿重复操作");

    // ========== 图片管理（1-031-109-000） ==========
    ErrorCode IMAGE_NOT_EXISTS = new ErrorCode(1_031_109_000, "图片不存在");

    // ========== 网站信息管理（1-031-110-000） ==========
    ErrorCode WEBSITE_INFO_NOT_EXISTS = new ErrorCode(1_031_110_000, "网站信息不存在");

    // ========== 黑名单管理（1-031-111-000） ==========
    ErrorCode BLACK_LIST_NOT_EXISTS = new ErrorCode(1_031_111_000, "黑名单记录不存在");

}
