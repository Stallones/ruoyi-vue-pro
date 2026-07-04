package com.sta.module.blog.service.media;

import com.sta.module.blog.controller.admin.media.vo.ImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.enums.TypeEnum;

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

}
