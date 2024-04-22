package com.xy.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.springboot.common.ErrorCode;
import com.xy.springboot.exception.BusinessException;
import com.xy.springboot.model.entity.Conversation;
import com.xy.springboot.model.entity.Message;
import com.xy.springboot.model.entity.Response;
import com.xy.springboot.model.vo.ConversationInfoVO;
import com.xy.springboot.model.vo.ConversationVO;
import com.xy.springboot.service.ConversationService;
import com.xy.springboot.mapper.ConversationMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author XY
 * @description 针对表【conversation(对话)】的数据库操作Service实现
 * @createDate 2024-04-21 13:00:40
 */
@Service
@Slf4j
public class ConversationServiceImpl extends ServiceImpl<ConversationMapper, Conversation>
        implements ConversationService {
    @Override
    public ConversationVO convertConversationVO(Conversation conversation) {
        if (conversation == null) {
            return null;
        }
        ConversationVO conversationVO = new ConversationVO();
        BeanUtils.copyProperties(conversation, conversationVO);
        return conversationVO;
    }

    @Override
    public List<ConversationVO> convertConversationVOList(List<Conversation> conversations) {
        if (conversations == null || conversations.isEmpty()) {
            return new ArrayList<>();
        }
        return conversations.stream().map(this::convertConversationVO).collect(Collectors.toList());
    }

    @Override
    public Long createConversation(Long userId) {
        // 1. 创建对话
        Conversation conversation = new Conversation();
        conversation.setUserId(userId);
        conversation.setHasTalked(0);
        boolean result = this.save(conversation);
        if (!result) {
            log.info("用户 {} 创建对话失败", userId);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "创建对话失败");
        }
        return conversation.getId();
    }

    @Override
    public void deleteConversation(Long conversationId, Long userId) {
        // 1. 删除对话
        Conversation conversation = this.getById(conversationId);
        if (conversation == null) {
            log.info("对话 {} 不存在", conversationId);
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "对话不存在");
        }
        if (!conversation.getUserId().equals(userId)) {
            log.info("用户 {} 无权删除对话 {}", userId, conversationId);
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权删除对话");
        }
        boolean result = this.removeById(conversationId);
        if (!result) {
            log.info("对话 {} 删除失败", conversationId);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除对话失败");
        }
    }

    @Override
    public void updateTitle(Long conversationId, String title) {
        LambdaUpdateWrapper<Conversation> wrapper =
                new LambdaUpdateWrapper<Conversation>().eq(Conversation::getId, conversationId)
                .eq(Conversation::getHasTalked, 0)
                .set(Conversation::getTitle, title)
                .set(Conversation::getHasTalked, 1);
        this.update(wrapper);
    }

    @Override
    public ConversationInfoVO buildConversationInfoVO(Long conversationId, List<Message> messages, List<Response> responses) {
        ConversationInfoVO conversationInfoVO = new ConversationInfoVO();
        conversationInfoVO.setId(conversationId);
        conversationInfoVO.setAsks(messages);
        conversationInfoVO.setAnswers(responses);
        return conversationInfoVO;
    }
}




