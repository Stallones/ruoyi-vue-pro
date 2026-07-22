package com.sta.module.blog.service.user;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.user.vo.UserPageReqVO;
import com.sta.module.blog.controller.app.user.vo.AppUserUpdateReqVO;
import com.sta.module.blog.dal.dataobject.user.BlogUserDO;
import com.sta.module.blog.dal.mysql.user.BlogUserMapper;
import com.sta.module.blog.service.media.ImageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import static com.sta.module.blog.enums.ErrorCodeConstants.*;

import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;

/**
 * 博客用户 Service 实现类
 *
 * @author blog
 */
@Service
@Validated
@Slf4j
public class BlogUserServiceImpl implements BlogUserService {

    @Resource
    private BlogUserMapper blogUserMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private ImageService imageService;

    @Override
    public BlogUserDO getUserByEmail(String email) {
        return blogUserMapper.selectByEmail(email);
    }

    @Override
    public List<BlogUserDO> getUserListByNickname(String nickname) {
        return blogUserMapper.selectListByNicknameLike(nickname);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BlogUserDO createUser(String nickname, String avatar, String registerIp) {
        // 生成随机密码
        String password = IdUtil.fastSimpleUUID();
        // 插入用户
        BlogUserDO user = new BlogUserDO();
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(encodePassword(password)); // 加密密码
        user.setRegisterIp(registerIp);
        user.setNickname(nickname).setAvatar(avatar);
        if (StrUtil.isEmpty(nickname)) {
            user.setNickname("用户" + RandomUtil.randomNumbers(6));
        }
        blogUserMapper.insert(user);
        return user;
    }

    @Override
    public void updateUserLogin(Long id, String loginIp) {
        blogUserMapper.updateById(BlogUserDO.builder()
                .id(id)
                .loginIp(loginIp)
                .loginDate(LocalDateTime.now())
                .build());
    }

    @Override
    public BlogUserDO getUser(Long id) {
        return blogUserMapper.selectById(id);
    }

    @Override
    public List<BlogUserDO> getUserList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return ListUtil.empty();
        }
        return blogUserMapper.selectByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(Long userId, AppUserUpdateReqVO reqVO) {
        // 校验用户存在
        BlogUserDO user = validateUserExists(userId);
        // 校验邮箱唯一
        validateEmailUnique(userId, reqVO.getEmail());
        // 更新用户
        BlogUserDO updateObj = BeanUtils.toBean(reqVO, BlogUserDO.class).setId(userId);
        blogUserMapper.updateById(updateObj);
        // 头像变化时，在 blog_image 记录
        if (!StrUtil.equals(user.getAvatar(), reqVO.getAvatar()) && StrUtil.isNotBlank(reqVO.getAvatar())) {
            imageService.recordAvatarImage(userId, reqVO.getAvatar(), 0L);
        }
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public PageResult<BlogUserDO> getUserPage(UserPageReqVO pageReqVO) {
        return blogUserMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        // 软删除
        validateUserExists(id);
        blogUserMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserStatus(Long id, Integer status) {
        validateUserExists(id);
        blogUserMapper.updateById(BlogUserDO.builder().id(id).status(status).build());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        BlogUserDO user = validateUserExists(userId);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw ServiceExceptionUtil.exception(USER_PASSWORD_ERROR);
        }
        blogUserMapper.updateById(BlogUserDO.builder()
                .id(userId)
                .password(encodePassword(newPassword))
                .build());
    }

    // ========== 内部方法 ==========

    /**
     * 对密码进行加密
     */
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private BlogUserDO validateUserExists(Long id) {
        if (id == null) {
            return null;
        }
        BlogUserDO user = blogUserMapper.selectById(id);
        if (user == null) {
            throw cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception(USER_NOT_EXISTS);
        }
        return user;
    }

    void validateEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        BlogUserDO user = blogUserMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        if (id == null || !user.getId().equals(id)) {
            throw cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception(USER_EMAIL_USED, email);
        }
    }

}
