package com.sta.module.blog.dal.dataobject.interaction;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sta.module.blog.enums.TypeEnum;
import lombok.*;

/**
 * 博客文章评论 DO
 */
@TableName("blog_comment")
@KeySequence("blog_comment_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 类型（20评论 21评论回复）
     */
    private TypeEnum type;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 父级ID（保留层级关系，当前业务不查）
     */
    private Long parentId;
    /**
     * 根节点ID（顶级评论为0，回复指向根评论）
     */
    private Long rootId;
    /**
     * 内容
     */
    private String content;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 被回复用户ID
     */
    private Long toUserId;
    /**
     * 是否过审（0否 1是）
     */
    private Integer status;
    /**
     * IP属地
     */
    private String ipLocation;
    /**
     * 浏览器
     */
    private String browser;
    /**
     * 操作系统
     */
    private String os;

}
