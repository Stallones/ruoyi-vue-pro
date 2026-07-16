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
    ErrorCode IMAGE_FILE_TYPE_INVALID = new ErrorCode(1_031_109_001, "图片类型不正确，仅支持 jpg/png");
    ErrorCode IMAGE_FILE_SIZE_EXCEEDED = new ErrorCode(1_031_109_002, "图片大小不能超过 2MB");
    ErrorCode IMAGE_TYPE_INVALID = new ErrorCode(1_031_109_003, "图片类型不正确");

    // ========== 网站信息管理（1-031-110-000） ==========
    ErrorCode WEBSITE_INFO_NOT_EXISTS = new ErrorCode(1_031_110_000, "网站信息不存在");

    // ========== 黑名单管理（1-031-111-000） ==========
    ErrorCode BLACK_LIST_NOT_EXISTS = new ErrorCode(1_031_111_000, "黑名单记录不存在");

    // ========== 认证（1-031-200-000） ==========
    ErrorCode AUTH_LOGIN_BAD_CREDENTIALS = new ErrorCode(1_031_200_000, "账号或密码不正确");
    ErrorCode AUTH_LOGIN_USER_DISABLED = new ErrorCode(1_031_200_001, "该账号已被禁用");
    ErrorCode AUTH_SOCIAL_USER_NOT_FOUND = new ErrorCode(1_031_200_002, "社交用户信息不存在");
    ErrorCode AUTH_CODE_INVALID_OR_EXPIRED = new ErrorCode(1_031_200_003, "验证码错误或已过期");

    // ========== 用户（1-031-201-000） ==========
    ErrorCode USER_NOT_EXISTS = new ErrorCode(1_031_201_000, "用户不存在");
    ErrorCode USER_EMAIL_NOT_EXISTS = new ErrorCode(1_031_201_001, "该邮箱对应的用户不存在");
    ErrorCode USER_EMAIL_USED = new ErrorCode(1_031_201_002, "该邮箱已被其他用户使用");
    ErrorCode USER_PASSWORD_ERROR = new ErrorCode(1_031_201_003, "旧密码不正确");

}
