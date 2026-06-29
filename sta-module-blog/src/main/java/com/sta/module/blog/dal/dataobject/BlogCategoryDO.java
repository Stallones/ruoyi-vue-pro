package com.sta.module.blog.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 博客分类 DO
 */
@TableName("blog_category")
@KeySequence("blog_category_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogCategoryDO extends BaseDO {

    /**
     * 分类ID
     */
    @TableId
    private Long id;
    /**
     * 分类名称
     */
    private String categoryName;

}
