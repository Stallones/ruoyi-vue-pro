package com.sta.module.blog.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sta.module.blog.enums.BlogTypeEnum;
import lombok.*;

/**
 * 博客留言 DO
 */
@TableName("blog_message")
@KeySequence("blog_message_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogMessageDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 类型（30留言 31留言回复）
     */
    private BlogTypeEnum type;
    /**
     * 父级ID（RE必填，指向被回复的留言/回复）
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
     * 是否通过（0否 1是）
     */
    private Integer status;

}
