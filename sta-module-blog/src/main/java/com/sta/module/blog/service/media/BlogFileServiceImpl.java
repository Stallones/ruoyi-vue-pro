package com.sta.module.blog.service.media;

import cn.hutool.core.date.DateUtil;
import cn.iocoder.yudao.module.infra.controller.admin.file.vo.file.FileCreateReqVO;
import cn.iocoder.yudao.module.infra.framework.file.core.client.FileClient;
import cn.iocoder.yudao.module.infra.framework.file.core.utils.FilePathUtils;
import cn.iocoder.yudao.module.infra.framework.file.core.utils.FileTypeUtils;
import cn.iocoder.yudao.module.infra.service.file.FileConfigService;
import cn.iocoder.yudao.module.infra.service.file.FileService;
import com.sta.module.blog.constants.BlogInfraConstants;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 博客文件服务实现：通过 infra 的公开 Service（FileConfigService / FileService）
 * 实现后端转发上传和文件记录，无需在 infra 模块添加任何方法。
 */
@Service
public class BlogFileServiceImpl implements BlogFileService {

    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private FileService fileService;

    @Override
    @SneakyThrows
    public String uploadAndRecord(byte[] content, String name, String directory) {
        // 1. 校验文件名和目录合法性
        name = FilePathUtils.validateFileName(name);
        FilePathUtils.validateDirectory(directory);
        // 2. 生成上传 path（directory/yyyyMMdd/name）
        String path = generateBlogPath(name, directory);
        // 3. 获取博客 MinIO 的 FileClient
        FileClient client = fileConfigService.getFileClient(BlogInfraConstants.BLOG_FILE_CONFIG_ID);
        // 4. 上传到 MinIO
        String type = FileTypeUtils.getMineType(content, name);
        String url = client.upload(content, path, type);
        // 5. 记录到 infra_file 表
        FileCreateReqVO reqVO = new FileCreateReqVO();
        reqVO.setConfigId(BlogInfraConstants.BLOG_FILE_CONFIG_ID);
        reqVO.setPath(path);
        reqVO.setName(name);
        reqVO.setUrl(url);
        reqVO.setType(type);
        reqVO.setSize((long) content.length);
        fileService.createFile(reqVO);
        return url;
    }

    /**
     * 生成博客文件路径：directory/yyyyMMdd/name
     */
    private String generateBlogPath(String name, String directory) {
        String datePrefix = DateUtil.format(new Date(), "yyyyMMdd");
        return directory + "/" + datePrefix + "/" + name;
    }

}
