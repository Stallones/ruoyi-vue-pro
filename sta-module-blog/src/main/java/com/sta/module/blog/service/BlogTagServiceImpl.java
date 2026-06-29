package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.tag.vo.BlogTagPageReqVO;
import com.sta.module.blog.controller.admin.tag.vo.BlogTagSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogTagDO;
import com.sta.module.blog.dal.mysql.BlogTagMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.TAG_NOT_EXISTS;

@Service
@Validated
public class BlogTagServiceImpl implements BlogTagService {

    @Resource
    private BlogTagMapper tagMapper;

    @Override
    public Long createTag(BlogTagSaveReqVO createReqVO) {
        BlogTagDO tag = BeanUtils.toBean(createReqVO, BlogTagDO.class);
        tagMapper.insert(tag);
        return tag.getId();
    }

    @Override
    public void updateTag(BlogTagSaveReqVO updateReqVO) {
        validateTagExists(updateReqVO.getId());
        BlogTagDO updateObj = BeanUtils.toBean(updateReqVO, BlogTagDO.class);
        tagMapper.updateById(updateObj);
    }

    @Override
    public void deleteTag(Long id) {
        validateTagExists(id);
        tagMapper.deleteById(id);
    }

    @Override
    public BlogTagDO getTag(Long id) {
        return tagMapper.selectById(id);
    }

    @Override
    public PageResult<BlogTagDO> getTagPage(BlogTagPageReqVO pageReqVO) {
        return tagMapper.selectPage(pageReqVO);
    }

    @Override
    public List<BlogTagDO> getTagList() {
        return tagMapper.selectListAll();
    }

    private void validateTagExists(Long id) {
        if (tagMapper.selectById(id) == null) {
            throw exception(TAG_NOT_EXISTS);
        }
    }

}
