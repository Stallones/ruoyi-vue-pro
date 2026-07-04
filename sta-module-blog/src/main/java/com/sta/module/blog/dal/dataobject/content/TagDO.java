package com.sta.module.blog.dal.dataobject.content;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 博客标签 DO
 */
@TableName("blog_tag")
@KeySequence("blog_tag_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDO extends BaseDO {

    /**
     * 标签ID
     */
    @TableId
    private Long id;
    /**
     * 标签名称
     */
    private String tagName;

}
