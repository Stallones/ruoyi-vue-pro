package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.image.vo.BlogImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogImageDO;
import com.sta.module.blog.dal.mysql.BlogImageMapper;
import com.sta.module.blog.enums.BlogTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.IMAGE_NOT_EXISTS;

/**
 * 博客图片 Service 实现类
 */
@Service
@Validated
public class BlogImageServiceImpl implements BlogImageService {

    @Resource
    private BlogImageMapper imageMapper;

    @Override
    public Long createImage(BlogImageSaveReqVO createReqVO) {
        BlogImageDO image = BeanUtils.toBean(createReqVO, BlogImageDO.class);
        imageMapper.insert(image);
        return image.getId();
    }

    @Override
    public void updateImage(BlogImageSaveReqVO updateReqVO) {
        validateImageExists(updateReqVO.getId());
        BlogImageDO updateObj = BeanUtils.toBean(updateReqVO, BlogImageDO.class);
        imageMapper.updateById(updateObj);
    }

    @Override
    public void deleteImage(Long id) {
        validateImageExists(id);
        imageMapper.deleteById(id);
    }

    @Override
    public BlogImageDO getImage(Long id) {
        return imageMapper.selectById(id);
    }

    @Override
    public List<BlogImageDO> getImageList() {
        return imageMapper.selectListAll();
    }

    @Override
    public List<BlogImageDO> getImageList(BlogTypeEnum type, Long dataId) {
        return imageMapper.selectListByTypeAndDataId(type, dataId);
    }

    @Override
    public void updateSort(List<BlogImageSaveReqVO> sortList) {
        sortList.forEach(item -> {
            BlogImageDO updateObj = new BlogImageDO();
            updateObj.setId(item.getId());
            updateObj.setSort(item.getSort());
            imageMapper.updateById(updateObj);
        });
    }

    private void validateImageExists(Long id) {
        if (imageMapper.selectById(id) == null) {
            throw exception(IMAGE_NOT_EXISTS);
        }
    }

}
