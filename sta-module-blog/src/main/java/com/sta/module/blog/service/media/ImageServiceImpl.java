package com.sta.module.blog.service.media;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.media.vo.ImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.dal.mysql.media.ImageMapper;
import com.sta.module.blog.enums.TypeEnum;
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
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageMapper imageMapper;

    @Override
    public Long createImage(ImageSaveReqVO createReqVO) {
        ImageDO image = BeanUtils.toBean(createReqVO, ImageDO.class);
        imageMapper.insert(image);
        return image.getId();
    }

    @Override
    public void updateImage(ImageSaveReqVO updateReqVO) {
        validateImageExists(updateReqVO.getId());
        ImageDO updateObj = BeanUtils.toBean(updateReqVO, ImageDO.class);
        imageMapper.updateById(updateObj);
    }

    @Override
    public void deleteImage(Long id) {
        validateImageExists(id);
        imageMapper.deleteById(id);
    }

    @Override
    public ImageDO getImage(Long id) {
        return imageMapper.selectById(id);
    }

    @Override
    public List<ImageDO> getImageList() {
        return imageMapper.selectList();
    }

    @Override
    public List<ImageDO> getImageListByType(TypeEnum type, Long dataId) {
        return imageMapper.selectListByType(type, dataId);
    }

    @Override
    public void updateSort(List<ImageSaveReqVO> sortList) {
        sortList.forEach(item -> {
            ImageDO updateObj = new ImageDO();
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
