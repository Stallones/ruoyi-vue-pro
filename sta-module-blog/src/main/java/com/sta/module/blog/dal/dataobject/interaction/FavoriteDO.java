package com.sta.module.blog.dal.dataobject.interaction;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sta.module.blog.enums.TypeEnum;
import lombok.*;

/**
 * 博客收藏 DO
 */
@TableName("blog_favorite")
@KeySequence("blog_favorite_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 收藏类型（ART文章）
     */
    private TypeEnum type;
    /**
     * 数据ID（按 type 关联：ART→文章ID）
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
