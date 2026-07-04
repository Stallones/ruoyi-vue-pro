package com.sta.module.blog.service.site;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.site.vo.WebsiteInfoSaveReqVO;
import com.sta.module.blog.dal.dataobject.site.WebsiteInfoDO;
import com.sta.module.blog.dal.mysql.site.WebsiteInfoMapper;
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
public class WebsiteInfoServiceImpl implements WebsiteInfoService {

    @Resource
    private WebsiteInfoMapper websiteInfoMapper;

    @Override
    public void updateWebsiteInfo(WebsiteInfoSaveReqVO updateReqVO) {
        validateWebsiteInfoExists(updateReqVO.getId());
        WebsiteInfoDO updateObj = BeanUtils.toBean(updateReqVO, WebsiteInfoDO.class);
        websiteInfoMapper.updateById(updateObj);
    }

    @Override
    public WebsiteInfoDO getWebsiteInfo() {
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
