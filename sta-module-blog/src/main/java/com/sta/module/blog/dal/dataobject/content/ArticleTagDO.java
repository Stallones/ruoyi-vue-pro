package com.sta.module.blog.dal.dataobject.content;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 博客文章-标签关联 DO
 */
@TableName("blog_article_tag")
@KeySequence("blog_article_tag_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleTagDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 文章ID
     */
    private Long articleId;
    /**
     * 标签ID
     */
    private Long tagId;
    /**
     * 标签名称
     */
    private String tagName;

}
