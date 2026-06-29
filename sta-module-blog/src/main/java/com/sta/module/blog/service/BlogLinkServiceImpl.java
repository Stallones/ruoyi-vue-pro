package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.link.vo.BlogLinkPageReqVO;
import com.sta.module.blog.controller.admin.link.vo.BlogLinkSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogLinkDO;
import com.sta.module.blog.dal.mysql.BlogLinkMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.LINK_NOT_EXISTS;

@Service
@Validated
public class BlogLinkServiceImpl implements BlogLinkService {

    @Resource
    private BlogLinkMapper linkMapper;

    @Override
    public Long createLink(BlogLinkSaveReqVO createReqVO) {
        BlogLinkDO link = BeanUtils.toBean(createReqVO, BlogLinkDO.class);
        linkMapper.insert(link);
        return link.getId();
    }

    @Override
    public void updateLink(BlogLinkSaveReqVO updateReqVO) {
        validateLinkExists(updateReqVO.getId());
        BlogLinkDO updateObj = BeanUtils.toBean(updateReqVO, BlogLinkDO.class);
        linkMapper.updateById(updateObj);
    }

    @Override
    public void deleteLink(Long id) {
        validateLinkExists(id);
        linkMapper.deleteById(id);
    }

    @Override
    public BlogLinkDO getLink(Long id) {
        return linkMapper.selectById(id);
    }

    @Override
    public PageResult<BlogLinkDO> getLinkPage(BlogLinkPageReqVO pageReqVO) {
        return linkMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateLinkCheck(Long id, Integer isCheck) {
        validateLinkExists(id);
        linkMapper.updateById(BlogLinkDO.builder().id(id).status(isCheck).build());
    }

    private void validateLinkExists(Long id) {
        if (linkMapper.selectById(id) == null) {
            throw exception(LINK_NOT_EXISTS);
        }
    }

    @Override
    public Long applyLink(String name, String url, String description, String background, String email) {
        BlogLinkDO link = BlogLinkDO.builder()
                .name(name)
                .url(url)
                .description(description)
                .background(background)
                .email(email)
                .status(0)
                .build();
        linkMapper.insert(link);
        return link.getId();
    }

    @Override
    public List<BlogLinkDO> getLinkList() {
        return linkMapper.selectListByStatus(1);
    }

}
