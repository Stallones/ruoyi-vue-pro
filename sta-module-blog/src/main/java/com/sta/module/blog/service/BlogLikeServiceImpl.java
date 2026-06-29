package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.dal.dataobject.BlogLikeDO;
import com.sta.module.blog.dal.mysql.BlogLikeMapper;
import com.sta.module.blog.enums.BlogTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Service
@Validated
public class BlogLikeServiceImpl implements BlogLikeService {

    @Resource
    private BlogLikeMapper likeMapper;

    @Override
    public Boolean toggleLike(BlogTypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        BlogLikeDO existing = likeMapper.selectByTypeAndDataId(type, typeId, userId);
        if (existing != null) {
            // 已有记录：切换 status（1→0 或 0→1）
            int newStatus = Objects.equals(existing.getStatus(), 1) ? 0 : 1;
            likeMapper.updateById(BlogLikeDO.builder().id(existing.getId()).status(newStatus).build());
            return newStatus == 1;
        } else {
            // 无记录：创建新记录，status=1
            BlogLikeDO like = BlogLikeDO.builder()
                    .type(type)
                    .dataId(typeId)
                    .userId(userId)
                    .status(1)
                    .build();
            likeMapper.insert(like);
            return true;
        }
    }

    @Override
    public Boolean isLike(BlogTypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        BlogLikeDO existing = likeMapper.selectByTypeAndDataId(type, typeId, userId);
        return existing != null && Objects.equals(existing.getStatus(), 1);
    }

}
