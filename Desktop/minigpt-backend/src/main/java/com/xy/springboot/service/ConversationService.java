package com.xy.springboot.service;

import com.xy.springboot.model.entity.Conversation;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xy.springboot.model.entity.Message;
import com.xy.springboot.model.entity.Response;
import com.xy.springboot.model.vo.ConversationInfoVO;
import com.xy.springboot.model.vo.ConversationVO;

import java.util.List;

/**
 * @author XY
 * @description 针对表【conversation(对话)】的数据库操作Service
 * @createDate 2024-04-21 13:00:40
 */
public interface ConversationService extends IService<Conversation> {
    /**
     * 转化成VO
     */
    ConversationVO convertConversationVO(Conversation conversation);

    List<ConversationVO> convertConversationVOList(List<Conversation> conversations);

    /**
     * 创建新的对话
     */
    Long createConversation(Long userId);

    /**
     * 删除对话
     */
    void deleteConversation(Long conversationId, Long userId);

    /**
     * 更新标题
     * 只更新没有对话的对话标题，更新成功的话将是否对话字段设置为1
     */
    void updateTitle(Long conversationId, String title);

    /**
     * 构建ConversationInfoVO
     *
     * @param conversationId 对话ID
     * @param messages      消息列表
     * @param responses     回复列表
     * @return ConversationInfoVO
     */
    ConversationInfoVO buildConversationInfoVO(Long conversationId, List<Message> messages, List<Response> responses);
}
