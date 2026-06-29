package com.sta.module.blog.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sta.module.blog.enums.BlogTypeEnum;
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
public class BlogCommentDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 类型（20评论 21评论回复）
     */
    private BlogTypeEnum type;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 父级ID（RE必填，指向被回复的评论/回复）
     */
    private Long parentId;
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

}
