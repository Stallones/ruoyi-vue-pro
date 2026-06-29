package com.sta.module.blog.service;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.message.vo.BlogMessagePageReqVO;
import com.sta.module.blog.dal.dataobject.BlogMessageDO;
import com.sta.module.blog.dal.mysql.BlogMessageMapper;
import com.sta.module.blog.enums.BlogTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.MESSAGE_NOT_EXISTS;

@Service
@Validated
public class BlogMessageServiceImpl implements BlogMessageService {

    @Resource
    private BlogMessageMapper messageMapper;

    @Override
    public PageResult<BlogMessageDO> getMessagePage(BlogMessagePageReqVO pageReqVO) {
        return messageMapper.selectPage(pageReqVO);
    }

    @Override
    public BlogMessageDO getMessage(Long id) {
        return messageMapper.selectById(id);
    }

    @Override
    public void updateMessageCheck(Long id, Integer isCheck) {
        validateMessageExists(id);
        messageMapper.updateById(BlogMessageDO.builder().id(id).status(isCheck).build());
    }

    @Override
    public void deleteMessage(Long id) {
        validateMessageExists(id);
        messageMapper.deleteById(id);
    }

    @Override
    public Long createMessage(Long userId, String content, Long parentId, Long toUserId) {
        BlogTypeEnum type = (parentId == null || parentId == 0) ? BlogTypeEnum.MSG : BlogTypeEnum.MSG_RE;
        BlogMessageDO message = BlogMessageDO.builder()
                .type(type)
                .parentId(parentId != null ? parentId : 0L)
                .content(content)
                .userId(userId)
                .toUserId(toUserId != null ? toUserId : 0L)
                .status(0)
                .build();
        messageMapper.insert(message);
        return message.getId();
    }

    @Override
    public PageResult<BlogMessageDO> getMessagePageByStatus(cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return messageMapper.selectMessagePageByStatus(pageParam);
    }

    @Override
    public List<BlogMessageDO> getMessageTree() {
        List<BlogMessageDO> allMessages = messageMapper.selectTreeList();
        return buildTree(allMessages);
    }

    /**
     * 将平铺列表组装为嵌套树
     */
    private List<BlogMessageDO> buildTree(List<BlogMessageDO> allMessages) {
        if (CollUtil.isEmpty(allMessages)) {
            return Collections.emptyList();
        }
        // 找出顶级留言（parentId == 0）
        return allMessages.stream()
                .filter(m -> m.getParentId() == null || m.getParentId() == 0)
                .collect(Collectors.toList());
        // 注意：BlogMessageDO 本身没有 replies 字段，
        // 树结构由 Controller 层转换为 VO 时处理
    }

    private void validateMessageExists(Long id) {
        if (messageMapper.selectById(id) == null) {
            throw exception(MESSAGE_NOT_EXISTS);
        }
    }

}
