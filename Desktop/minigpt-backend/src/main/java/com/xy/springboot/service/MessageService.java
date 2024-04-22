package com.xy.springboot.service;

import com.xy.springboot.model.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author XY
* @description 针对表【message(消息)】的数据库操作Service
* @createDate 2024-04-21 17:24:52
*/
public interface MessageService extends IService<Message> {
    /**
     * 根据消息ID查询消息
     */
    List<Message> getMessageByConversationId(Long conversationId);
}
