package com.sta.module.blog.service.media;

/**
 * 博客文件服务：后端转发方式上传到博客 MinIO 并记录到 infra_file
 *
 * 不污染 infra 模块，blog 专属的上传逻辑集中在此。
 */
public interface BlogFileService {

    /**
     * 上传文件到博客 MinIO 并记录到 infra_file 表
     *
     * @param content   文件内容
     * @param name      原文件名
     * @param directory 目录（如 blog/avatar）
     * @return 文件访问 URL
     */
    String uploadAndRecord(byte[] content, String name, String directory);

}
