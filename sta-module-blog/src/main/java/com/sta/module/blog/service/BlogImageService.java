package com.sta.module.blog.service;

import com.sta.module.blog.controller.admin.image.vo.BlogImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogImageDO;
import com.sta.module.blog.enums.BlogTypeEnum;

import java.util.List;

/**
 * 博客图片 Service 接口
 */
public interface BlogImageService {

    Long createImage(BlogImageSaveReqVO createReqVO);

    void updateImage(BlogImageSaveReqVO updateReqVO);

    void deleteImage(Long id);

    BlogImageDO getImage(Long id);

    List<BlogImageDO> getImageList();

    List<BlogImageDO> getImageList(BlogTypeEnum type, Long dataId);

    void updateSort(List<BlogImageSaveReqVO> sortList);

}
