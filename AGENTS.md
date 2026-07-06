# ruoyi-vue-pro

**Generated:** 2026-07-05

## OVERVIEW

Yudao/RuoYi-Vue-Pro Java Spring Boot monolith. The backend for both the main platform and the sta-blog module. Manages all business modules (system, infra, erp, blog, +11 more), framework starters, and database migrations.

## STRUCTURE

```
ruoyi-vue-pro/
├── yudao-server/              # Spring Boot entry point (application.yml, main class)
├── yudao-framework/           # 15 starter modules (security, mybatis, redis, web, etc.)
├── yudao-dependencies/        # BOM / dependency management
├── yudao-module-system/       # System management (users, roles, depts)
├── yudao-module-infra/        # Infrastructure (file, config, audit)
├── yudao-module-erp/          # ERP module
├── sta-module-blog/           # Blog module (the project's blog backend)
├── yudao-module-{ai,bpm,crm,im,iot,mall,member,mes,mp,pay,report,wms}/  # Other modules
├── sql/                       # DB migrations (mysql, oracle, postgresql, sqlserver, dm, etc.)
└── script/                    # Docker, Jenkins, shell scripts
```

(15 yudao-module-* business modules total, most disabled in root pom.xml)

## WHERE TO LOOK

| Task | Location | Notes |
|------|----------|-------|
| Blog API controllers | `sta-module-blog/src/main/java/com/sta/blog/` | AppArticleController, AppCommentController, etc. |
| Blog API config | `sta-module-blog/src/main/resources/` | blog-app group API docs |
| Security framework | `yudao-framework/yudao-spring-boot-starter-security/` | JWT, auth filters |
| MyBatis helpers | `yudao-framework/yudao-spring-boot-starter-mybatis/` | BaseMapper, page plugins |
| Web config | `yudao-framework/yudao-spring-boot-starter-web/` | Global exception handler, CORS |
| Server config | `yudao-server/src/main/resources/` | application.yml, application-local.yml |
| SQL migrations | `sql/mysql/` | Blog module: `sta-module-blog.sql` |

## CONVENTIONS

- Java 17, Spring Boot 3.x, MyBatis, MySQL
- Standard 3-layer: Controller → Service → Mapper (MyBatis XML)
- CommonResult wrapper for all API responses (`{code, msg, data}`)
- OpenAPI 3 docs auto-generated per module group
- MapStruct for DTO/VO mapping, Lombok for models
- Maven multi-module with BOM (yudao-dependencies)
- Most modules disabled by default in root pom.xml — enable when needed

## COMMANDS

```bash
# Build entire project (skip blog module tests)
mvn clean install -DskipTests -pl sta-module-blog -am

# Run server
cd yudao-server && mvn spring-boot:run

# Build specific module
mvn clean install -pl yudao-module-system -am
```
