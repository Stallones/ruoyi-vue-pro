package com.sta.module.blog.dal.dataobject.media;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sta.module.blog.enums.TypeEnum;
import lombok.*;

/**
 * 博客图片 DO
 */
@TableName("blog_image")
@KeySequence("blog_image_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDO extends BaseDO {

    @TableId
    private Long id;
    /**
     * 图类型（51封面图 52轮播图 53 banner图）
     */
    private TypeEnum type;
    /**
     * 目标ID（按 type 关联：cover→文章ID, home→首页轮播图ID, banner→路由页头ID）
     */
    private Long dataId;
    /**
     * 图片路径
     */
    private String path;
    /**
     * 图片大小（字节）
     */
    private Long size;
    /**
     * 图片扩展名
     */
    private String extension;
    /**
     * 排序
     */
    private Integer sort;

}
