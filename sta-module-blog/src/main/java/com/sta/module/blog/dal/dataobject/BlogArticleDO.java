package com.sta.module.blog.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 博客文章 DO
 */
@TableName("blog_article")
@KeySequence("blog_article_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogArticleDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 分类一对一ID
     */
    private Long categoryId;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 文章内容
     */
    private String content;
    /**
     * 文章状态（1通过 0不通过）
     */
    private Integer status;
    /**
     * 访问量
     */
    private Long visitCount;

}
