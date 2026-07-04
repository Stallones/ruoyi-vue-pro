DROP TABLE IF EXISTS `member_user`;
CREATE TABLE `member_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `status` tinyint NOT NULL COMMENT '状态',
  `register_ip` varchar(32) NOT NULL COMMENT '注册 IP',
  `register_terminal` tinyint DEFAULT NULL COMMENT '注册终端',
  `login_ip` varchar(50) DEFAULT '' COMMENT '最后登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  `nickname` varchar(30) NOT NULL DEFAULT '' COMMENT '用户昵称',
  `avatar` varchar(512) NOT NULL DEFAULT '' COMMENT '头像',
  `name` varchar(30) DEFAULT '' COMMENT '真实名字',
  `sex` tinyint DEFAULT '0' COMMENT '用户性别',
  `area_id` bigint DEFAULT NULL COMMENT '所在地',
  `birthday` datetime DEFAULT NULL COMMENT '出生日期',
  `mark` varchar(255) DEFAULT NULL COMMENT '会员备注',

  `tag_ids` varchar(255) DEFAULT NULL COMMENT '用户标签编号列表，以逗号分隔',
  `group_id` bigint DEFAULT NULL COMMENT '用户分组编号',

  `level_id` bigint DEFAULT NULL COMMENT '等级编号',
  `experience` int NOT NULL DEFAULT '0' COMMENT '经验',

  `point` int NOT NULL DEFAULT '0' COMMENT '积分',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_mobile` (`mobile`, `deleted`),
  UNIQUE KEY `uk_email` (`email`, `deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员用户';

-- ==================== 会员标签 ====================
DROP TABLE IF EXISTS `member_tag`;
CREATE TABLE `member_tag` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '标签名称',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员标签';

-- ==================== 会员分组 ====================
DROP TABLE IF EXISTS `member_group`;
CREATE TABLE `member_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '名称',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户分组';

-- ==================== 会员等级 ====================
DROP TABLE IF EXISTS `member_level`;
CREATE TABLE `member_level` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(30) NOT NULL DEFAULT '' COMMENT '等级名称',
  `level` int NOT NULL DEFAULT '0' COMMENT '等级',
  `experience` int NOT NULL DEFAULT '0' COMMENT '升级经验',
  `discount_percent` tinyint NOT NULL DEFAULT '100' COMMENT '享受折扣',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '等级图标',
  `background_url` varchar(255) NOT NULL DEFAULT '' COMMENT '等级背景图',
  `status` tinyint NOT NULL DEFAULT '0' COMMENT '状态',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员等级';

-- ==================== 会员等级记录 ====================
DROP TABLE IF EXISTS `member_level_record`;
CREATE TABLE `member_level_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户编号',
  `level_id` bigint NOT NULL DEFAULT '0' COMMENT '等级编号',
  `level` int NOT NULL DEFAULT '0' COMMENT '会员等级',
  `discount_percent` tinyint NOT NULL DEFAULT '100' COMMENT '享受折扣',
  `experience` int NOT NULL DEFAULT '0' COMMENT '升级经验',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '描述',
  `user_experience` int NOT NULL DEFAULT '0' COMMENT '会员此时的经验',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '会员等级记录-用户编号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员等级记录';

-- ==================== 会员经验记录 ====================
DROP TABLE IF EXISTS `member_experience_record`;
CREATE TABLE `member_experience_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` bigint NOT NULL DEFAULT '0' COMMENT '用户编号',
  `biz_id` varchar(64) NOT NULL DEFAULT '' COMMENT '业务编号',
  `biz_type` tinyint NOT NULL DEFAULT '0' COMMENT '业务类型',
  `title` varchar(30) NOT NULL DEFAULT '' COMMENT '标题',
  `description` varchar(512) NOT NULL DEFAULT '' COMMENT '描述',
  `experience` int NOT NULL DEFAULT '0' COMMENT '经验',
  `total_experience` int NOT NULL DEFAULT '0' COMMENT '变更后的经验',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '会员经验记录-用户编号',
  KEY `idx_user_biz_type` (`user_id`, `biz_type`) USING BTREE COMMENT '会员经验记录-用户业务类型'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='会员经验记录';

-- ==================== 会员积分记录 ====================
DROP TABLE IF EXISTS `member_point_record`;
CREATE TABLE `member_point_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `user_id` bigint NOT NULL COMMENT '用户编号',
  `biz_id` varchar(255) NOT NULL COMMENT '业务编码',
  `biz_type` tinyint NOT NULL COMMENT '业务类型',
  `title` varchar(255) NOT NULL COMMENT '积分标题',
  `description` varchar(5000) DEFAULT NULL COMMENT '积分描述',
  `point` int NOT NULL COMMENT '积分',
  `total_point` int NOT NULL COMMENT '变动后的积分',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_userId` (`user_id`) USING BTREE,
  KEY `index_title` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户积分记录';

-- ==================== 签到规则 ====================
DROP TABLE IF EXISTS `member_sign_in_config`;
CREATE TABLE `member_sign_in_config` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `day` int NOT NULL COMMENT '第几天',
  `point` int NOT NULL COMMENT '奖励积分',
  `experience` int NOT NULL DEFAULT '0' COMMENT '奖励经验',
  `status` tinyint NOT NULL COMMENT '状态',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='签到规则';

-- ==================== 签到记录 ====================
DROP TABLE IF EXISTS `member_sign_in_record`;
CREATE TABLE `member_sign_in_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '签到自增id',
  `user_id` bigint DEFAULT NULL COMMENT '签到用户',
  `day` int DEFAULT NULL COMMENT '第几天签到',
  `point` int NOT NULL DEFAULT '0' COMMENT '签到的分数',
  `experience` int NOT NULL DEFAULT '0' COMMENT '奖励经验',

  `tenant_id` bigint NOT NULL DEFAULT '0' COMMENT '租户编号',
  `creator` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updater` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否删除',

  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='签到记录';
