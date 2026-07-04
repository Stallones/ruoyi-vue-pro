package com.sta.module.blog.service.content;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.content.vo.TagPageReqVO;
import com.sta.module.blog.controller.admin.content.vo.TagSaveReqVO;
import com.sta.module.blog.dal.dataobject.content.TagDO;
import com.sta.module.blog.dal.mysql.content.TagMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.TAG_NOT_EXISTS;

@Service
@Validated
public class TagServiceImpl implements TagService {

    @Resource
    private TagMapper tagMapper;

    @Override
    public Long createTag(TagSaveReqVO createReqVO) {
        TagDO tag = BeanUtils.toBean(createReqVO, TagDO.class);
        tagMapper.insert(tag);
        return tag.getId();
    }

    @Override
    public void updateTag(TagSaveReqVO updateReqVO) {
        validateTagExists(updateReqVO.getId());
        TagDO updateObj = BeanUtils.toBean(updateReqVO, TagDO.class);
        tagMapper.updateById(updateObj);
    }

    @Override
    public void deleteTag(Long id) {
        validateTagExists(id);
        tagMapper.deleteById(id);
    }

    @Override
    public TagDO getTag(Long id) {
        return tagMapper.selectById(id);
    }

    @Override
    public PageResult<TagDO> getTagPage(TagPageReqVO pageReqVO) {
        return tagMapper.selectPage(pageReqVO);
    }

    @Override
    public List<TagDO> getTagList() {
        return tagMapper.selectList();
    }

    private void validateTagExists(Long id) {
        if (tagMapper.selectById(id) == null) {
            throw exception(TAG_NOT_EXISTS);
        }
    }

}
