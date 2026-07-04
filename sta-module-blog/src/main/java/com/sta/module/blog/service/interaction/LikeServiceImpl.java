package com.sta.module.blog.service.interaction;

import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.sta.module.blog.dal.dataobject.interaction.LikeDO;
import com.sta.module.blog.dal.mysql.interaction.LikeMapper;
import com.sta.module.blog.enums.TypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Service
@Validated
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    @Override
    public Boolean toggleLike(TypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        LikeDO existing = likeMapper.selectByType(type, typeId, userId);
        if (existing != null) {
            // 已有记录：切换 status（1→0 或 0→1）
            int newStatus = Objects.equals(existing.getStatus(), 1) ? 0 : 1;
            likeMapper.updateById(LikeDO.builder().id(existing.getId()).status(newStatus).build());
            return newStatus == 1;
        } else {
            // 无记录：创建新记录，status=1
            LikeDO like = LikeDO.builder()
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
    public Boolean isLike(TypeEnum type, Long typeId) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        LikeDO existing = likeMapper.selectByType(type, typeId, userId);
        return existing != null && Objects.equals(existing.getStatus(), 1);
    }

}
