package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.sta.module.blog.controller.admin.message.vo.BlogMessagePageReqVO;
import com.sta.module.blog.dal.dataobject.BlogMessageDO;

import java.util.List;

/**
 * 博客留言 Service 接口
 */
public interface BlogMessageService {

    PageResult<BlogMessageDO> getMessagePage(BlogMessagePageReqVO pageReqVO);

    BlogMessageDO getMessage(Long id);

    void updateMessageCheck(Long id, Integer isCheck);

    void deleteMessage(Long id);

    /**
     * App - 创建留言/回复
     *
     * @param userId      用户ID
     * @param content     内容
     * @param parentId    父级ID（留言为0，回复指向被回复的留言/回复ID）
     * @param replyUserId 被回复用户ID（留言为0）
     * @return 留言ID
     */
    Long createMessage(Long userId, String content, Long parentId, Long toUserId);

    /**
     * App - 获取留言列表（分页，仅已审核）
     */
    PageResult<BlogMessageDO> getMessagePageByStatus(cn.iocoder.yudao.framework.common.pojo.PageParam pageParam);

    /**
     * App - 获取留言树（嵌套结构）
     */
    List<BlogMessageDO> getMessageTree();

}
