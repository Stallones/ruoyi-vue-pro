package com.sta.module.blog.service.media;

import com.sta.module.blog.controller.admin.media.vo.ImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.enums.TypeEnum;
import jakarta.validation.Valid;

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
     * 记录头像图片到 blog_image 表（用户保存头像时调用）
     *
     * @param userId 用户编号
     * @param url    头像 URL
     * @param size   文件大小（字节，未知传 0）
     */
    void recordAvatarImage(Long userId, String url, Long size);

}
