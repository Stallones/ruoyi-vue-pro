package com.sta.module.blog.dal.dataobject.site;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 博客网站信息 DO
 */
@TableName("blog_website_info")
@KeySequence("blog_website_info_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebsiteInfoDO extends BaseDO {

    /**
     * 网站信息ID
     */
    @TableId
    private Long id;
    /**
     * 站长头像
     */
    private String webmasterAvatar;
    /**
     * 站长名称
     */
    private String webmasterName;
    /**
     * 站长文案
     */
    private String webmasterCopy;
    /**
     * 站长资料卡背景图
     */
    private String webmasterProfileBackground;
    /**
     * Gitee链接
     */
    private String giteeLink;
    /**
     * GitHub链接
     */
    private String githubLink;
    /**
     * 网站名称
     */
    private String websiteName;
    /**
     * 头部通知
     */
    private String headerNotification;
    /**
     * 侧面公告
     */
    private String sidebarAnnouncement;
    /**
     * 备案信息
     */
    private String recordInfo;
    /**
     * 开始运行时间
     */
    private LocalDateTime startTime;

}
