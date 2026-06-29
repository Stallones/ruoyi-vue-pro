package com.sta.module.blog.enums;

import cn.iocoder.yudao.framework.common.core.ArrayValuable;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 博客归档类型枚举
 *
 * @author sta
 */
@RequiredArgsConstructor
@Getter
public enum ArchiveTypeEnum implements ArrayValuable<String> {

    CATEGORY("category", "分类", "category"),
    TAG("tag", "标签", "tag"),
    ;

    public static final String[] ARRAYS = Arrays.stream(values()).map(ArchiveTypeEnum::getValue).toArray(String[]::new);

    @EnumValue   // MyBatis Plus
    @JsonValue   // Jackson：序列化/反序列化用这个值
    private final String value;

    private final String name;

    /** 英文语义名，供 OpenAPI x-enum-varnames 导出 */
    private final String englishName;

    @JsonCreator
    public static ArchiveTypeEnum of(String value) {
        if (value == null) return null;
        for (ArchiveTypeEnum e : values()) {
            if (e.value.equals(value)) return e;
        }
        throw new IllegalArgumentException("Unknown ArchiveTypeEnum value: " + value);
    }

    /**
     * 获取带前缀的完整键（用于内部业务逻辑如 Redis key 等）
     */
    public String getFullKey() {
        return "blog_archive_" + value;
    }

    @Override
    public String[] array() {
        return ARRAYS;
    }

}
