package com.sta.module.blog.dal.dataobject.interaction;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sta.module.blog.enums.TypeEnum;
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
public class MessageDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 类型（30留言 31留言回复）
     */
    private TypeEnum type;
    /**
     * 父级ID（保留层级关系，当前业务不查）
     */
    private Long parentId;
    /**
     * 根节点ID（顶级留言为0，回复指向根留言）
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
     * 是否通过（0否 1是）
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
