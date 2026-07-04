package com.sta.module.blog.service.interaction;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.interaction.vo.MessagePageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.MessageDO;

import java.util.List;

/**
 * 博客留言 Service 接口
 */
public interface MessageService {

    PageResult<MessageDO> getMessagePage(MessagePageReqVO pageReqVO);

    MessageDO getMessage(Long id);

    void updateMessageCheck(Long id, Integer isCheck);

    void deleteMessage(Long id);

    /**
     * App - 创建留言/回复
     *
     * @param userId      用户ID
     * @param content     内容
     * @param parentId    父级ID（留言为0，回复传被回复的留言/回复ID）
     * @param toUserId    被回复用户ID（留言为0）
     * @param ipLocation  IP属地
     * @param browser     浏览器
     * @param os          操作系统
     * @return 留言ID
     */
    Long createMessage(Long userId, String content, Long parentId, Long toUserId,
                       String ipLocation, String browser, String os);

    /**
     * App - 获取留言列表（分页，仅已审核）
     */
    PageResult<MessageDO> getMessagePageByStatus(PageParam pageParam);

    /**
     * App - 获取留言树（嵌套结构）
     */
    List<MessageDO> getMessageTree();

    /**
     * App - 获取顶级留言分页（type=30）
     */
    PageResult<MessageDO> getTopMessagePage(PageParam pageParam, String orderBy);

    /**
     * App - 获取回复分页（type=31，按 rootId）
     */
    PageResult<MessageDO> getReplyPage(Long rootId, PageParam pageParam);

}
