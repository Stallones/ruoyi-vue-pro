package com.sta.module.blog.service.interaction;

import com.sta.module.blog.enums.TypeEnum;

/**
 * 博客点赞 Service 接口
 */
public interface LikeService {

    /**
     * App - 切换点赞状态（存在则取消，不存在则点赞）
     *
     * @return true=点赞，false=取消点赞
     */
    Boolean toggleLike(TypeEnum type, Long typeId);

    /**
     * App - 是否已点赞
     */
    Boolean isLike(TypeEnum type, Long typeId);

}
