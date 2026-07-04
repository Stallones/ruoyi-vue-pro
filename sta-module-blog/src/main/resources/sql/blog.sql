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
-- 数据迁移 SQL（从旧表迁移到新表）
-- 执行前请确认旧表存在且有数据
-- =============================================
