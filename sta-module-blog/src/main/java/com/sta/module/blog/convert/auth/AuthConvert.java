package com.sta.module.blog.convert.auth;

import cn.iocoder.yudao.framework.common.biz.system.oauth2.dto.OAuth2AccessTokenRespDTO;
import com.sta.module.blog.controller.app.user.vo.AppAuthLoginRespVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 认证转换器
 *
 * @author blog
 */
@Mapper
public interface AuthConvert {

    AuthConvert INSTANCE = Mappers.getMapper(AuthConvert.class);

    AppAuthLoginRespVO convert(OAuth2AccessTokenRespDTO bean, String openid);

}
