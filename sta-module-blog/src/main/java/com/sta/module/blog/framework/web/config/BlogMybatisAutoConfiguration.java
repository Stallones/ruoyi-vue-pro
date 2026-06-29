package com.sta.module.blog.framework.web.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * blog 模块的 MyBatis 自动配置
 * <p>
 * 在 MybatisPlusAutoConfiguration 之后加载，避免 Mapper 扫描冲突
 */
@AutoConfiguration(after = MybatisPlusAutoConfiguration.class)
@MapperScan(value = "com.sta.module.blog.dal.mysql", annotationClass = Mapper.class)
public class BlogMybatisAutoConfiguration {

}
