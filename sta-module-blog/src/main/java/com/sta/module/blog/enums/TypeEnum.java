package com.sta.module.blog.enums;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 博客业务类型常量
 *
 * @author sta
 */
@RequiredArgsConstructor
@Getter
public enum TypeEnum implements ArrayValuable<Integer> {

    ART(10, "文章", "article"),
    CMT(20, "评论", "comment"),
    CMT_RE(21, "评论回复", "commentReply"),
    MSG(30, "留言", "message"),
    MSG_RE(31, "留言回复", "messageReply"),

    IMG_COVER(51, "封面图", "imgCover"),
    IMG_HOME(52, "轮播图", "imgHome"),
    IMG_PAGE(53, "banner图", "imgPage"),
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(TypeEnum::getValue).toArray(Integer[]::new);

    @EnumValue   // MyBatis Plus：入库/查询用这个值
    @JsonValue   // Jackson：序列化/反序列化用这个值
    private final Integer value;

    private final String name;

    /** 英文语义名，供 OpenAPI x-enum-varnames 导出，前端自动生成常量 key */
    private final String englishName;

    @JsonCreator
    public static TypeEnum of(Integer value) {
        if (value == null) return null;
        for (TypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown TypeEnum value: " + value);
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
