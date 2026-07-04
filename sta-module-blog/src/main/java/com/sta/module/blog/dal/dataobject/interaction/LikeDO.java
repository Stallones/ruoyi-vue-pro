package com.sta.module.blog.dal.dataobject.interaction;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sta.module.blog.enums.TypeEnum;
import lombok.*;

/**
 * 博客点赞 DO
 */
@TableName("blog_like")
@KeySequence("blog_like_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikeDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 点赞类型（ART文章 CMT评论 MSG留言）
     */
    private TypeEnum type;
    /**
     * 数据ID（按 type 关联：ART→文章ID, CMT→评论ID, MSG→留言ID）
     */
    private Long dataId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 目标用户ID
     */
    private Long toUserId;
    /**
     * 状态（0取消 1有效）
     */
    private Integer status;

}
