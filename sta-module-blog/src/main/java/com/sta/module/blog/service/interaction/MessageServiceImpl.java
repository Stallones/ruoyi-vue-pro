package com.sta.module.blog.service.interaction;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.interaction.vo.MessagePageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.MessageDO;
import com.sta.module.blog.dal.mysql.interaction.MessageMapper;
import com.sta.module.blog.enums.TypeEnum;
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
public class MessageServiceImpl implements MessageService {

    @Resource
    private MessageMapper messageMapper;

    @Override
    public PageResult<MessageDO> getMessagePage(MessagePageReqVO pageReqVO) {
        return messageMapper.selectPage(pageReqVO);
    }

    @Override
    public MessageDO getMessage(Long id) {
        return messageMapper.selectById(id);
    }

    @Override
    public void updateMessageCheck(Long id, Integer isCheck) {
        validateMessageExists(id);
        messageMapper.updateById(MessageDO.builder().id(id).status(isCheck).build());
    }

    @Override
    public void deleteMessage(Long id) {
        validateMessageExists(id);
        messageMapper.deleteById(id);
    }

    @Override
    public Long createMessage(Long userId, String content, Long parentId, Long toUserId,
                              String ipLocation, String browser, String os) {
        TypeEnum type = (parentId == null || parentId == 0) ? TypeEnum.MSG : TypeEnum.MSG_RE;

        // 计算 rootId
        Long rootId = 0L;
        if (parentId != null && parentId > 0) {
            MessageDO parent = messageMapper.selectById(parentId);
            if (parent != null) {
                rootId = (parent.getRootId() != null && parent.getRootId() > 0)
                        ? parent.getRootId()   // 回复回复：继承父级的 rootId
                        : parent.getId();       // 回复顶级留言：rootId = 父留言 ID
            }
        }

        MessageDO message = MessageDO.builder()
                .type(type)
                .parentId(parentId != null ? parentId : 0L)
                .rootId(rootId)
                .content(content)
                .userId(userId)
                .toUserId(toUserId != null ? toUserId : 0L)
                .status(0)
                .ipLocation(ipLocation)
                .browser(browser)
                .os(os)
                .build();
        messageMapper.insert(message);
        return message.getId();
    }

    @Override
    public PageResult<MessageDO> getMessagePageByStatus(PageParam pageParam) {
        return messageMapper.selectMessagePageByStatus(pageParam);
    }

    @Override
    public List<MessageDO> getMessageTree() {
        List<MessageDO> allMessages = messageMapper.selectTreeList();
        return buildTree(allMessages);
    }

    @Override
    public PageResult<MessageDO> getTopMessagePage(PageParam pageParam, String orderBy) {
        return messageMapper.selectTopMessagePage(pageParam, orderBy);
    }

    @Override
    public PageResult<MessageDO> getReplyPage(Long rootId, PageParam pageParam) {
        return messageMapper.selectReplyPageByRootId(rootId, pageParam);
    }

    /**
     * 将平铺列表组装为嵌套树
     */
    private List<MessageDO> buildTree(List<MessageDO> allMessages) {
        if (CollUtil.isEmpty(allMessages)) {
            return Collections.emptyList();
        }
        // 找出顶级留言（parentId == 0）
        return allMessages.stream()
                .filter(m -> m.getParentId() == null || m.getParentId() == 0)
                .collect(Collectors.toList());
        // 注意：MessageDO 本身没有 replies 字段，
        // 树结构由 Controller 层转换为 VO 时处理
    }

    private void validateMessageExists(Long id) {
        if (messageMapper.selectById(id) == null) {
            throw exception(MESSAGE_NOT_EXISTS);
        }
    }

}
