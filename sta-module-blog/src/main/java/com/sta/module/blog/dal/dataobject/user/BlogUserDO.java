package com.sta.module.blog.dal.dataobject.user;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 博客用户 DO
 *
 * @author blog
 */
@TableName(value = "blog_user", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserDO extends BaseDO {

    /**
     * 用户编号
     */
    @TableId
    private Long id;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 注册 IP
     */
    private String registerIp;

    /**
     * 最后登录 IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private LocalDateTime loginDate;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 用户性别：0-未知，1-男，2-女
     */
    private Integer sex;

}
