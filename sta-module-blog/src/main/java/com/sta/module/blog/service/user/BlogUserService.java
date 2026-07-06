package com.sta.module.blog.service.user;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.user.vo.UserPageReqVO;
import com.sta.module.blog.controller.app.user.vo.AppUserUpdateReqVO;
import com.sta.module.blog.dal.dataobject.user.BlogUserDO;

import java.util.Collection;
import java.util.List;

/**
 * 博客用户 Service 接口
 *
 * @author blog
 */
public interface BlogUserService {

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象
     */
    BlogUserDO getUserByEmail(String email);

    /**
     * 基于用户昵称，模糊匹配用户列表
     *
     * @param nickname 用户昵称
     * @return 用户信息列表
     */
    List<BlogUserDO> getUserListByNickname(String nickname);

    /**
     * 创建用户（三方登录时使用）
     *
     * @param nickname   昵称
     * @param avatar     头像
     * @param registerIp 注册 IP
     * @return 用户对象
     */
    BlogUserDO createUser(String nickname, String avatar, String registerIp);

    /**
     * 更新用户的最后登陆信息
     *
     * @param id      用户编号
     * @param loginIp 登陆 IP
     */
    void updateUserLogin(Long id, String loginIp);

    /**
     * 通过用户 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    BlogUserDO getUser(Long id);

    /**
     * 通过用户 ID 查询用户们
     *
     * @param ids 用户 ID
     * @return 用户对象信息数组
     */
    List<BlogUserDO> getUserList(Collection<Long> ids);

    /**
     * 【C端】修改基本信息
     *
     * @param userId 用户编号
     * @param reqVO  基本信息
     */
    void updateUser(Long userId, AppUserUpdateReqVO reqVO);

    /**
     * 判断密码是否匹配
     *
     * @param rawPassword     未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

    /**
     * 修改密码
     *
     * @param userId      用户编号
     * @param oldPassword 旧密码（明文）
     * @param newPassword 新密码（明文）
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 【管理员】获得用户分页
     *
     * @param pageReqVO 分页查询
     * @return 用户分页
     */
    PageResult<BlogUserDO> getUserPage(UserPageReqVO pageReqVO);

    /**
     * 【管理员】删除用户（软删除）
     *
     * @param id 用户编号
     */
    void deleteUser(Long id);

    /**
     * 【管理员】更新用户状态
     *
     * @param id     用户编号
     * @param status 新状态
     */
    void updateUserStatus(Long id, Integer status);

    /**
     * 对密码进行加密
     *
     * @param password 原始密码
     * @return 加密后的密码
     */
    String encodePassword(String password);

}
