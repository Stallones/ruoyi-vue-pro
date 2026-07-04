package com.sta.module.blog.dal.mysql.user;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.user.vo.UserPageReqVO;
import com.sta.module.blog.dal.dataobject.user.BlogUserDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 博客用户 Mapper
 *
 * @author blog
 */
@Mapper
public interface BlogUserMapper extends BaseMapperX<BlogUserDO> {

    /**
     * 根据邮箱查询用户
     */
    default BlogUserDO selectByEmail(String email) {
        return selectOne(BlogUserDO::getEmail, email);
    }

    /**
     * 根据昵称模糊查询用户列表
     */
    default List<BlogUserDO> selectListByNicknameLike(String nickname) {
        return selectList(new LambdaQueryWrapperX<BlogUserDO>()
                .likeIfPresent(BlogUserDO::getNickname, nickname));
    }

    /**
     * 分页查询用户
     */
    default PageResult<BlogUserDO> selectPage(UserPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BlogUserDO>()
                .likeIfPresent(BlogUserDO::getEmail, reqVO.getEmail())
                .likeIfPresent(BlogUserDO::getNickname, reqVO.getNickname())
                .eqIfPresent(BlogUserDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BlogUserDO::getCreateTime, reqVO.getCreateTime()));
    }

}
