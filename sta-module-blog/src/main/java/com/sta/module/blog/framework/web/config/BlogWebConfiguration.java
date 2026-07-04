package com.sta.module.blog.framework.web.config;

import com.sta.module.blog.enums.TypeEnum;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * blog 模块的自动配置
 */
@AutoConfiguration
public class BlogWebConfiguration {

    /**
     * blog 模块 Admin 端 API 分组（仅 /admin-api/blog/**）
     */
    @Bean
    public GroupedOpenApi blogAdminGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("blog-admin")
                .pathsToMatch("/admin-api/blog/**")
                .addOperationCustomizer(buildOperationIdCustomizer())
                .addOpenApiCustomizer(buildEnumCustomizer())
                .build();
    }

    /**
     * blog 模块 App 端 API 分组（仅 /app-api/blog/**）
     */
    @Bean
    public GroupedOpenApi blogAppGroupedOpenApi() {
        return GroupedOpenApi.builder()
                .group("blog-app")
                .pathsToMatch("/app-api/blog/**")
                .addOperationCustomizer(buildOperationIdCustomizer())
                .addOpenApiCustomizer(buildEnumCustomizer())
                .build();
    }

    /**
     * 与框架保持一致的 operationId 生成规则：类名前缀_方法名
     */
    private static OperationCustomizer buildOperationIdCustomizer() {
        return (operation, handlerMethod) -> {
            String className = handlerMethod.getBeanType().getSimpleName();
            String classPrefix = className.replaceAll("Controller$", "");
            String methodName = handlerMethod.getMethod().getName();
            operation.setOperationId(classPrefix + "_" + methodName);
            return operation;
        };
    }

    /** 构建枚举元数据注入 customizer */
    private static OpenApiCustomizer buildEnumCustomizer() {
        List<Integer> btValues = Arrays.stream(TypeEnum.values())
                .map(TypeEnum::getValue).toList();
        List<String> btVarnames = Arrays.stream(TypeEnum.values())
                .map(TypeEnum::getEnglishName).toList();
        List<String> btDescriptions = Arrays.stream(TypeEnum.values())
                .map(TypeEnum::getName).toList();

        return openApi -> {
            if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
                return;
            }

            // 0. 确保枚举被注册为命名 Schema（供前端脚本按名查找）
            Schema btSchema = new Schema<>().type("integer").format("int32")
                    ._enum(btValues);
            btSchema.addExtension("x-enum-varnames", btVarnames);
            btSchema.addExtension("x-enum-descriptions", btDescriptions);
            openApi.getComponents().getSchemas().put("TypeEnum", btSchema);

            // 1. 收集内联 Schema 并注入扩展
            List<Schema> allSchemas = new ArrayList<>();
            openApi.getComponents().getSchemas().values().forEach(s -> collectAll(s, allSchemas));
            if (openApi.getPaths() != null) {
                openApi.getPaths().forEach((path, item) -> item.readOperations().forEach(op -> {
                    if (op.getParameters() != null) op.getParameters()
                            .forEach(p -> { if (p.getSchema() != null) allSchemas.add(p.getSchema()); });
                    if (op.getRequestBody() != null) op.getRequestBody().getContent().values()
                            .forEach(mt -> collectAll(mt.getSchema(), allSchemas));
                    op.getResponses().values().forEach(r -> {
                        if (r.getContent() != null) r.getContent().values()
                                .forEach(mt -> collectAll(mt.getSchema(), allSchemas));
                    });
                }));
            }
            allSchemas.forEach(s -> {
                inject(s, btValues, btVarnames, btDescriptions);
            });
        };
    }

    private static void collectAll(Schema schema, List<Schema> out) {
        if (schema == null) return;
        out.add(schema);
        if (schema.getProperties() != null)
            schema.getProperties().values().forEach(v -> collectAll((Schema) v, out));
        if (schema.getItems() != null)
            collectAll(schema.getItems(), out);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void inject(Schema schema, List<?> values, List<String> names, List<String> descs) {
        if (schema.getEnum() == null || schema.getEnum().isEmpty()) return;
        if (values.size() != schema.getEnum().size()) return;
        if (!values.containsAll(schema.getEnum()) || !schema.getEnum().containsAll(values)) return;
        schema.addExtension("x-enum-varnames", names);
        schema.addExtension("x-enum-descriptions", descs);
    }

}
