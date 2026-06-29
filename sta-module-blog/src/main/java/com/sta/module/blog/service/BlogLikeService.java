package com.sta.module.blog.service;

import com.sta.module.blog.enums.BlogTypeEnum;

/**
 * 博客点赞 Service 接口
 */
public interface BlogLikeService {

    /**
     * App - 切换点赞状态（存在则取消，不存在则点赞）
     *
     * @return true=点赞，false=取消点赞
     */
    Boolean toggleLike(BlogTypeEnum type, Long typeId);

    /**
     * App - 是否已点赞
     */
    Boolean isLike(BlogTypeEnum type, Long typeId);

}
