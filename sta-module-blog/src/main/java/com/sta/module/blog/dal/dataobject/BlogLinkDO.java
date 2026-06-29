package com.sta.module.blog.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 博客友链 DO
 */
@TableName("blog_link")
@KeySequence("blog_link_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogLinkDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 网站名称
     */
    private String name;
    /**
     * 网站地址
     */
    private String url;
    /**
     * 网站描述
     */
    private String description;
    /**
     * 网站背景图
     */
    private String background;
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 审核状态（0未通过 1已通过）
     */
    private Integer status;

}
