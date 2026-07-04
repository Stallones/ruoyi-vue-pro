package com.sta.module.blog.dal.mysql.interaction;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.sta.module.blog.controller.admin.interaction.vo.MessagePageReqVO;
import com.sta.module.blog.dal.dataobject.interaction.MessageDO;
import com.sta.module.blog.enums.TypeEnum;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper extends BaseMapperX<MessageDO> {

    default PageResult<MessageDO> selectPage(MessagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MessageDO>()
                .eqIfPresent(MessageDO::getType, reqVO.getType())
                .eqIfPresent(MessageDO::getStatus, reqVO.getIsCheck())
                .betweenIfPresent(MessageDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MessageDO::getId));
    }

    /**
     * App - 查询已审核留言列表（分页）
     */
    default PageResult<MessageDO> selectMessagePageByStatus(cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<MessageDO>()
                .eq(MessageDO::getStatus, 1)
                .orderByDesc(MessageDO::getCreateTime));
    }

    /**
     * App - 顶级留言分页（type=30 + status=1 + 排序）
     */
    default PageResult<MessageDO> selectTopMessagePage(cn.iocoder.yudao.framework.common.pojo.PageParam pageParam,
                                                       String orderBy) {
        LambdaQueryWrapperX<MessageDO> wrapper = new LambdaQueryWrapperX<MessageDO>()
                .eq(MessageDO::getType, TypeEnum.MSG)
                .eq(MessageDO::getStatus, 1);
        wrapper.orderByDesc(MessageDO::getCreateTime);
        return selectPage(pageParam, wrapper);
    }

    /**
     * App - 回复分页（root_id=X + type=31 + status=1 + 按时间升序）
     */
    default PageResult<MessageDO> selectReplyPageByRootId(Long rootId,
                                                          cn.iocoder.yudao.framework.common.pojo.PageParam pageParam) {
        return selectPage(pageParam, new LambdaQueryWrapperX<MessageDO>()
                .eq(MessageDO::getRootId, rootId)
                .eq(MessageDO::getType, TypeEnum.MSG_RE)
                .eq(MessageDO::getStatus, 1)
                .orderByAsc(MessageDO::getCreateTime));
    }

    /**
     * App - 统计某根留言下的回复数（已审核）
     */
    default Long selectReplyCountByRootId(Long rootId) {
        return selectCount(new LambdaQueryWrapperX<MessageDO>()
                .eq(MessageDO::getRootId, rootId)
                .eq(MessageDO::getType, TypeEnum.MSG_RE)
                .eq(MessageDO::getStatus, 1));
    }

    /**
     * App - 批量查询多个根留言下的所有回复（用于批量计算 replyCount）
     */
    default List<MessageDO> selectRepliesByRootIds(List<Long> rootIds) {
        return selectList(new LambdaQueryWrapperX<MessageDO>()
                .in(MessageDO::getRootId, rootIds)
                .eq(MessageDO::getType, TypeEnum.MSG_RE)
                .eq(MessageDO::getStatus, 1)
                .select(MessageDO::getRootId));
    }

    /**
     * App - 查询所有已审核留言（树结构用，不分页）
     */
    default List<MessageDO> selectTreeList() {
        return selectList(new LambdaQueryWrapperX<MessageDO>()
                .eq(MessageDO::getStatus, 1)
                .orderByAsc(MessageDO::getCreateTime));
    }

}
