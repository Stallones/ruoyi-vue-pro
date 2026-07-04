package com.sta.module.blog.service.site;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.site.vo.LinkPageReqVO;
import com.sta.module.blog.controller.admin.site.vo.LinkSaveReqVO;
import com.sta.module.blog.dal.dataobject.site.LinkDO;
import com.sta.module.blog.dal.mysql.site.LinkMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.LINK_NOT_EXISTS;

@Service
@Validated
public class LinkServiceImpl implements LinkService {

    @Resource
    private LinkMapper linkMapper;

    @Override
    public Long createLink(LinkSaveReqVO createReqVO) {
        LinkDO link = BeanUtils.toBean(createReqVO, LinkDO.class);
        linkMapper.insert(link);
        return link.getId();
    }

    @Override
    public void updateLink(LinkSaveReqVO updateReqVO) {
        validateLinkExists(updateReqVO.getId());
        LinkDO updateObj = BeanUtils.toBean(updateReqVO, LinkDO.class);
        linkMapper.updateById(updateObj);
    }

    @Override
    public void deleteLink(Long id) {
        validateLinkExists(id);
        linkMapper.deleteById(id);
    }

    @Override
    public LinkDO getLink(Long id) {
        return linkMapper.selectById(id);
    }

    @Override
    public PageResult<LinkDO> getLinkPage(LinkPageReqVO pageReqVO) {
        return linkMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateLinkCheck(Long id, Integer isCheck) {
        validateLinkExists(id);
        linkMapper.updateById(LinkDO.builder().id(id).status(isCheck).build());
    }

    private void validateLinkExists(Long id) {
        if (linkMapper.selectById(id) == null) {
            throw exception(LINK_NOT_EXISTS);
        }
    }

    @Override
    public Long applyForLink(String name, String url, String description, String background, String email) {
        LinkDO link = LinkDO.builder()
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
    public List<LinkDO> getLinkList() {
        return linkMapper.selectListByStatus(1);
    }

}
