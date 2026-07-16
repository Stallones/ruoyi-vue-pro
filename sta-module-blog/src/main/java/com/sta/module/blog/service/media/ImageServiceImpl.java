package com.sta.module.blog.service.media;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.infra.api.file.FileApi;
import com.sta.module.blog.constants.BlogInfraConstants;
import com.sta.module.blog.controller.admin.media.vo.ImageSaveReqVO;
import com.sta.module.blog.dal.dataobject.media.ImageDO;
import com.sta.module.blog.dal.mysql.media.ImageMapper;
import com.sta.module.blog.enums.TypeEnum;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.*;

/**
 * 博客图片 Service 实现类
 */
@Service
@Validated
public class ImageServiceImpl implements ImageService {

    @Resource
    private ImageMapper imageMapper;

    @Resource
    private FileApi fileApi;

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

    @Override
    @SneakyThrows
    public ImageDO uploadImage(MultipartFile file, TypeEnum type, Long dataId, Long loginUserId) {
        validateImageFile(file);
        String directory = resolveDirectory(type);

        byte[] content = IoUtil.readBytes(file.getInputStream());
        String url = fileApi.createFile(BlogInfraConstants.BLOG_FILE_CONFIG_ID, content,
                file.getOriginalFilename(), directory, file.getContentType());

        Long finalDataId = resolveDataId(type, dataId, loginUserId);
        ImageDO image = new ImageDO()
                .setType(type)
                .setDataId(finalDataId)
                .setPath(url)
                .setSize(file.getSize())
                .setExtension(FileUtil.extName(file.getOriginalFilename()))
                .setSort(0);
        imageMapper.insert(image);
        return image;
    }

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("image/jpeg") && !contentType.equals("image/png"))) {
            throw exception(IMAGE_FILE_TYPE_INVALID);
        }
        if (file.getSize() > 2 * 1024 * 1024) {
            throw exception(IMAGE_FILE_SIZE_EXCEEDED);
        }
    }

    private String resolveDirectory(TypeEnum type) {
        return switch (type) {
            case IMG_AVATAR -> "blog/avatar";
            case IMG_COVER -> "blog/article";
            case IMG_HOME, IMG_PAGE -> "blog/other";
            default -> throw exception(IMAGE_TYPE_INVALID);
        };
    }

    private Long resolveDataId(TypeEnum type, Long dataId, Long loginUserId) {
        if (type == TypeEnum.IMG_AVATAR) {
            return loginUserId;
        }
        return dataId;
    }

    private void validateImageExists(Long id) {
        if (imageMapper.selectById(id) == null) {
            throw exception(IMAGE_NOT_EXISTS);
        }
    }

}
