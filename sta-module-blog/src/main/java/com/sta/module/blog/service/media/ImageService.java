package com.sta.module.blog.service.media;

import com.sta.module.blog.controller.admin.media.vo.ImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.enums.TypeEnum;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * 博客图片 Service 接口
 */
public interface ImageService {

    Long createImage(ImageSaveReqVO createReqVO);

    void updateImage(ImageSaveReqVO updateReqVO);

    void deleteImage(Long id);

    ImageDO getImage(Long id);

    List<ImageDO> getImageList();

    List<ImageDO> getImageListByType(TypeEnum type, Long dataId);

    void updateSort(List<ImageSaveReqVO> sortList);

    /**
     * 上传图片，保存到 infra 文件存储并在 blog_image 中记录
     *
     * @param file        图片文件
     * @param type        图片类型
     * @param dataId      关联数据 ID（头像可为 null，后端取当前登录用户）
     * @param loginUserId 当前登录用户 ID
     * @return 保存后的图片 DO
     */
    ImageDO uploadImage(MultipartFile file, TypeEnum type, Long dataId, Long loginUserId) throws IOException;

}
