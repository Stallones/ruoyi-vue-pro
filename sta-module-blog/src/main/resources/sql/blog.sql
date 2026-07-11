-- =============================================
-- 博客模块数据库建表 SQL
-- 模块：sta-module-blog
-- 说明：包含博客业务所有表的新建（每次执行先删后建）
-- =============================================

-- ========== 1. 文章表 ==========
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `category_id` bigint NOT NULL COMMENT '分类一对一ID',
    `title` varchar(256) NOT NULL COMMENT '文章标题',
    `content` longtext COMMENT '文章内容',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '文章状态（1通过 0不通过）',
    `visit_count` bigint NOT NULL DEFAULT 0 COMMENT '访问量',

    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客文章表';

-- ========== 2. 标签表 ==========
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '标签ID',
    `tag_name` varchar(128) NOT NULL COMMENT '标签名称',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tag_name` (`tag_name`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客标签表';

-- ========== 3. 文章-标签关联表 ==========
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '文章标签一对多关联ID',
    `article_id` bigint NOT NULL COMMENT '文章ID',
    `tag_id` bigint NOT NULL COMMENT '标签ID',
    `tag_name` varchar(128) NOT NULL COMMENT '标签名称',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_tag_id` (`tag_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客文章-标签关联表';

-- ========== 4. 分类表 ==========
DROP TABLE IF EXISTS `blog_category`;
CREATE TABLE `blog_category` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `category_name` varchar(128) NOT NULL COMMENT '分类名称',
    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_category_name` (`category_name`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客分类表';

-- ========== 5. 评论表（仅文章评论） ==========
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `type` tinyint NOT NULL DEFAULT 20 COMMENT '类型（20评论 21回复）',
    `article_id` bigint NOT NULL COMMENT '文章ID',
    `parent_id` bigint DEFAULT 0 COMMENT '父级ID（保留层级关系，当前业务不查）',
    `root_id` bigint DEFAULT 0 COMMENT '根节点ID（顶级评论为0，回复指向根评论）',
    `content` text COMMENT '内容',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `to_user_id` bigint DEFAULT 0 COMMENT '被回复用户ID',
    `status` tinyint NOT NULL DEFAULT 0 COMMENT '是否过审（0否 1是）',
    `ip_location` varchar(64) DEFAULT '' COMMENT 'IP属地',
    `browser` varchar(64) DEFAULT '' COMMENT '浏览器',
    `os` varchar(64) DEFAULT '' COMMENT '操作系统',

    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_article_type` (`article_id`, `type`),
    KEY `idx_root_id` (`root_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客文章评论表';

-- ========== 6. 留言表 ==========
DROP TABLE IF EXISTS `blog_message`;
CREATE TABLE `blog_message` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '留言ID',
    `type` tinyint NOT NULL DEFAULT 30 COMMENT '类型（30留言 31回复）',
    `parent_id` bigint DEFAULT 0 COMMENT '父级ID（保留层级关系，当前业务不查）',
    `root_id` bigint DEFAULT 0 COMMENT '根节点ID（顶级留言为0，回复指向根留言）',
    `content` text COMMENT '内容',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `to_user_id` bigint DEFAULT 0 COMMENT '被回复用户ID',
    `status` tinyint NOT NULL DEFAULT 0 COMMENT '是否通过（0否 1是）',
    `ip_location` varchar(64) DEFAULT '' COMMENT 'IP属地',
    `browser` varchar(64) DEFAULT '' COMMENT '浏览器',
    `os` varchar(64) DEFAULT '' COMMENT '操作系统',

    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_type` (`type`),
    KEY `idx_root_id` (`root_id`),
    KEY `idx_status` (`status`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客留言表';

-- ========== 7. 友链表 ==========
DROP TABLE IF EXISTS `blog_link`;
CREATE TABLE `blog_link` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '友链ID',
    `name` varchar(128) NOT NULL COMMENT '网站名称',
    `url` varchar(512) NOT NULL COMMENT '网站地址',
    `description` varchar(512) DEFAULT '' COMMENT '网站描述',
    `background` varchar(512) DEFAULT '' COMMENT '网站背景图',
    `email` varchar(128) DEFAULT '' COMMENT '邮箱地址',
    `status` tinyint NOT NULL DEFAULT 0 COMMENT '审核状态（0未通过 1已通过）',

    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_is_check` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客友链表';

-- ========== 8. 收藏表 ==========
DROP TABLE IF EXISTS `blog_favorite`;
CREATE TABLE `blog_favorite` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
    `type` tinyint NOT NULL COMMENT '收藏类型（10文章）',
    `data_id` bigint NOT NULL COMMENT '数据ID（按 type 关联：ART→文章ID）',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `to_user_id` bigint DEFAULT 0 COMMENT '目标用户ID',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0取消 1有效）',

    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_type_data` (`type`, `data_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客收藏表';

-- ========== 9. 点赞表 ==========
DROP TABLE IF EXISTS `blog_like`;
CREATE TABLE `blog_like` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '点赞ID',
    `type` tinyint NOT NULL COMMENT '点赞类型（10文章 20评论 30留言）',
    `data_id` bigint NOT NULL COMMENT '数据ID（按 type 关联：ART→文章ID, CMT→评论ID, MSG→留言ID）',
    `user_id` bigint NOT NULL COMMENT '用户ID',
    `to_user_id` bigint DEFAULT 0 COMMENT '目标用户ID',
    `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态（0取消 1有效）',

    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`),
    KEY `idx_type_data` (`type`, `data_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客点赞表';

-- ========== 10. 图片表 ==========
DROP TABLE IF EXISTS `blog_image`;
CREATE TABLE `blog_image` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '图ID',
    `type` tinyint NOT NULL COMMENT '图类型(51封面 52首页轮播图 53路由页头)',
    `data_id` bigint NOT NULL COMMENT '目标ID（按 type 关联：cover→文章ID, home→首页轮播图ID, banner→路由页头ID）',
    `path` varchar(512) NOT NULL COMMENT '图片路径',
    `size` bigint DEFAULT 0 COMMENT '图片大小（字节）',
    `extension` varchar(64) DEFAULT '' COMMENT '图片扩展名',
    `sort` int NOT NULL DEFAULT 0 COMMENT '排序',

    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客图片表';

-- ========== 11. 网站信息表 ==========
DROP TABLE IF EXISTS `blog_website_info`;
CREATE TABLE `blog_website_info` (
    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '网站信息ID',
    `webmaster_avatar` varchar(512) DEFAULT '' COMMENT '站长头像',
    `webmaster_name` varchar(128) DEFAULT '' COMMENT '站长名称',
    `webmaster_copy` varchar(512) DEFAULT '' COMMENT '站长文案',
    `webmaster_profile_background` varchar(512) DEFAULT '' COMMENT '站长资料卡背景图',
    `gitee_link` varchar(256) DEFAULT '' COMMENT 'Gitee链接',
    `github_link` varchar(256) DEFAULT '' COMMENT 'GitHub链接',
    `website_name` varchar(128) DEFAULT '' COMMENT '网站名称',
    `header_notification` varchar(1024) DEFAULT '' COMMENT '头部通知',
    `sidebar_announcement` varchar(1024) DEFAULT '' COMMENT '侧面公告',
    `record_info` varchar(256) DEFAULT '' COMMENT '备案信息',
    `start_time` datetime DEFAULT NULL COMMENT '开始运行时间',

    `creator` varchar(64) DEFAULT '' COMMENT '创建者',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updater` varchar(64) DEFAULT '' COMMENT '更新者',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='博客网站信息表';


DROP TABLE IF EXISTS `blog_user`;
CREATE TABLE `blog_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `status` tinyint NOT NULL COMMENT '状态',
  `register_ip` varchar(32) NOT NULL COMMENT '注册 IP',
  `login_ip` varchar(50) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `nickname` varchar(30) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `avatar` varchar(512) NOT NULL DEFAULT '' COMMENT '头像',
  `sex` tinyint DEFAULT '0' COMMENT '用户性别',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_email` (`email`, `deleted`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='博客用户';

-- =============================================
-- 数据初始化 SQL：字典 + 菜单（blog_dict.sql + blog_menu.sql）
-- 注意：博客审核类状态（1=通过/0=不通过）与框架 common_status（0=开启/1=关闭）方向相反
-- ID 段：dict_type 2100-2104, dict_data 4200-4209, menu 3000-3134
-- =============================================

-- ========== 字典类型 ==========
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2100, '博客文章状态', 'blog_article_status', 0, '文章审核状态', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2101, '博客评论审核状态', 'blog_comment_check_status', 0, '评论是否过审', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2102, '博客留言审核状态', 'blog_message_check_status', 0, '留言是否通过', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2103, '博客友链审核状态', 'blog_link_check_status', 0, '友链审核状态', 'admin', NOW(), 'admin', NOW(), b'0', NULL);
INSERT INTO `system_dict_type` (`id`, `name`, `type`, `status`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`, `deleted_time`)
VALUES (2104, '博客收藏状态', 'blog_favorite_status', 0, '收藏状态', 'admin', NOW(), 'admin', NOW(), b'0', NULL);

-- ========== 字典数据 ==========
-- 文章状态（1=通过 0=不通过）
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4200, 1, '不通过', '0', 'blog_article_status', 0, 'danger', '', '文章审核未通过', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4201, 2, '通过', '1', 'blog_article_status', 0, 'success', '', '文章审核通过', 'admin', NOW(), 'admin', NOW(), b'0');
-- 评论审核状态（0=未过审 1=已过审）
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4202, 1, '未过审', '0', 'blog_comment_check_status', 0, 'warning', '', '评论未过审', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4203, 2, '已过审', '1', 'blog_comment_check_status', 0, 'success', '', '评论已过审', 'admin', NOW(), 'admin', NOW(), b'0');
-- 留言审核状态（0=未通过 1=已通过）
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4204, 1, '未通过', '0', 'blog_message_check_status', 0, 'warning', '', '留言未通过', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4205, 2, '已通过', '1', 'blog_message_check_status', 0, 'success', '', '留言已通过', 'admin', NOW(), 'admin', NOW(), b'0');
-- 友链审核状态（0=未通过 1=已通过）
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4206, 1, '未通过', '0', 'blog_link_check_status', 0, 'warning', '', '友链未通过', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4207, 2, '已通过', '1', 'blog_link_check_status', 0, 'success', '', '友链已通过', 'admin', NOW(), 'admin', NOW(), b'0');
-- 收藏状态（0=取消 1=有效）
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4208, 1, '取消', '0', 'blog_favorite_status', 0, 'info', '', '收藏已取消', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_dict_data` (`id`, `sort`, `label`, `value`, `dict_type`, `status`, `color_type`, `css_class`, `remark`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (4209, 2, '有效', '1', 'blog_favorite_status', 0, 'success', '', '收藏有效', 'admin', NOW(), 'admin', NOW(), b'0');

-- ========== 菜单初始化（ID 3000-3135） ==========
-- 结构：博客管理 → [内容|互动|媒体|站点|用户] → 菜单 → 按钮

-- 一级目录：博客管理
INSERT INTO `system_menu` (`id`, `name`, `permission`, `type`, `sort`, `parent_id`, `path`, `icon`, `component`, `component_name`, `status`, `visible`, `keep_alive`, `always_show`, `creator`, `create_time`, `updater`, `update_time`, `deleted`)
VALUES (3000, '博客管理', '', 1, 50, 0, '/blog', 'ep:notebook-2', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- ========== 二级目录：内容 ==========
INSERT INTO `system_menu` VALUES (3015, '内容', '', 1, 1, 3000, 'content', 'ep:document', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 文章管理
INSERT INTO `system_menu` VALUES (3001, '文章管理', '', 2, 1, 3015, 'article', 'ep:document', 'blog/content/article/index', 'BlogArticle', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3002, '文章查询', 'blog:article:query', 3, 1, 3001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3003, '文章新增', 'blog:article:create', 3, 2, 3001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3004, '文章修改', 'blog:article:update', 3, 3, 3001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3005, '文章删除', 'blog:article:delete', 3, 4, 3001, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 分类管理
INSERT INTO `system_menu` VALUES (3010, '分类管理', '', 2, 2, 3015, 'category', 'ep:folder', 'blog/content/category/index', 'BlogCategory', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3011, '分类查询', 'blog:category:query', 3, 1, 3010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3012, '分类新增', 'blog:category:create', 3, 2, 3010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3013, '分类修改', 'blog:category:update', 3, 3, 3010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3014, '分类删除', 'blog:category:delete', 3, 4, 3010, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 标签管理
INSERT INTO `system_menu` VALUES (3020, '标签管理', '', 2, 3, 3015, 'tag', 'ep:price-tag', 'blog/content/tag/index', 'BlogTag', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3021, '标签查询', 'blog:tag:query', 3, 1, 3020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3022, '标签新增', 'blog:tag:create', 3, 2, 3020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3023, '标签修改', 'blog:tag:update', 3, 3, 3020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3024, '标签删除', 'blog:tag:delete', 3, 4, 3020, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- ========== 二级目录：互动 ==========
INSERT INTO `system_menu` VALUES (3035, '互动', '', 1, 2, 3000, 'interaction', 'ep:chat-dot-round', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 评论管理
INSERT INTO `system_menu` VALUES (3030, '评论管理', '', 2, 1, 3035, 'comment', 'ep:chat-dot-round', 'blog/interaction/comment/index', 'BlogComment', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3031, '评论查询', 'blog:comment:query', 3, 1, 3030, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3032, '评论审核', 'blog:comment:update', 3, 2, 3030, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3033, '评论删除', 'blog:comment:delete', 3, 3, 3030, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 留言管理
INSERT INTO `system_menu` VALUES (3040, '留言管理', '', 2, 2, 3035, 'message', 'ep:message', 'blog/interaction/message/index', 'BlogMessage', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3041, '留言查询', 'blog:message:query', 3, 1, 3040, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3042, '留言审核', 'blog:message:update', 3, 2, 3040, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3043, '留言删除', 'blog:message:delete', 3, 3, 3040, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 友链管理
INSERT INTO `system_menu` VALUES (3050, '友链管理', '', 2, 3, 3035, 'link', 'ep:link', 'blog/site/link/index', 'BlogLink', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3051, '友链查询', 'blog:link:query', 3, 1, 3050, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3052, '友链新增', 'blog:link:create', 3, 2, 3050, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3053, '友链修改', 'blog:link:update', 3, 3, 3050, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3054, '友链删除', 'blog:link:delete', 3, 4, 3050, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 收藏管理
INSERT INTO `system_menu` VALUES (3090, '收藏管理', '', 2, 4, 3035, 'favorite', 'ep:star', 'blog/interaction/favorite/index', 'BlogFavorite', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3091, '收藏查询', 'blog:favorite:query', 3, 1, 3090, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3092, '收藏删除', 'blog:favorite:delete', 3, 2, 3090, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- ========== 二级目录：媒体 ==========
INSERT INTO `system_menu` VALUES (3055, '媒体', '', 1, 3, 3000, 'media', 'ep:picture', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 图片管理（type 区分封面/轮播/banner）
INSERT INTO `system_menu` VALUES (3095, '图片管理', '', 2, 1, 3055, 'image', 'ep:picture', 'blog/media/image/index', 'BlogImage', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3096, '图片查询', 'blog:image:query', 3, 1, 3095, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3097, '图片新增', 'blog:image:create', 3, 2, 3095, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3098, '图片修改', 'blog:image:update', 3, 3, 3095, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3099, '图片删除', 'blog:image:delete', 3, 4, 3095, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- ========== 二级目录：站点 ==========
INSERT INTO `system_menu` VALUES (3105, '站点', '', 1, 4, 3000, 'site', 'ep:monitor', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 网站信息
INSERT INTO `system_menu` VALUES (3110, '网站信息', '', 2, 1, 3105, 'web-info', 'ep:monitor', 'blog/site/website-info/index', 'BlogWebsiteInfo', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3111, '网站信息查询', 'blog:website-info:query', 3, 1, 3110, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3112, '网站信息修改', 'blog:website-info:update', 3, 2, 3110, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');

-- ========== 二级目录：用户 ==========
INSERT INTO `system_menu` VALUES (3135, '用户', '', 1, 5, 3000, 'user', 'ep:user-filled', NULL, NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
-- 博客用户（博客前台注册用户，区别于 system_user）
INSERT INTO `system_menu` VALUES (3130, '博客用户', '', 2, 1, 3135, 'blog-user', 'ep:user-filled', 'blog/user/index', 'BlogUser', 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3131, '博客用户查询', 'blog:user:query', 3, 1, 3130, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3132, '博客用户新增', 'blog:user:create', 3, 2, 3130, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3133, '博客用户修改', 'blog:user:update', 3, 3, 3130, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
INSERT INTO `system_menu` VALUES (3134, '博客用户删除', 'blog:user:delete', 3, 4, 3130, '', '', '', NULL, 0, b'1', b'1', b'1', 'admin', NOW(), 'admin', NOW(), b'0');
