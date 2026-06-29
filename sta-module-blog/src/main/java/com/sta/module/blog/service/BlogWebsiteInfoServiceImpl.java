package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.websiteinfo.vo.BlogWebsiteInfoSaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogWebsiteInfoDO;
import com.sta.module.blog.dal.mysql.BlogWebsiteInfoMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.WEBSITE_INFO_NOT_EXISTS;

/**
 * 博客网站信息 Service 实现类
 * <p>
 * 网站信息为单条记录，只查询和修改
 */
@Service
@Validated
public class BlogWebsiteInfoServiceImpl implements BlogWebsiteInfoService {

    @Resource
    private BlogWebsiteInfoMapper websiteInfoMapper;

    @Override
    public void updateWebsiteInfo(BlogWebsiteInfoSaveReqVO updateReqVO) {
        validateWebsiteInfoExists(updateReqVO.getId());
        BlogWebsiteInfoDO updateObj = BeanUtils.toBean(updateReqVO, BlogWebsiteInfoDO.class);
        websiteInfoMapper.updateById(updateObj);
    }

    @Override
    public BlogWebsiteInfoDO getWebsiteInfo() {
        // 网站信息只有一条记录，取第一条
        return websiteInfoMapper.selectOne(null);
    }

    @Override
    public Boolean healthCheck() {
        // 简化实现：只要数据库能查询到网站信息，说明服务可用
        return websiteInfoMapper.selectCount() > 0;
    }

    private void validateWebsiteInfoExists(Long id) {
        if (websiteInfoMapper.selectById(id) == null) {
            throw exception(WEBSITE_INFO_NOT_EXISTS);
        }
    }

}
