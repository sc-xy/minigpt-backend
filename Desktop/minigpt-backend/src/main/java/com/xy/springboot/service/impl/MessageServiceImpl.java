package com.xy.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.springboot.model.entity.Message;
import com.xy.springboot.service.MessageService;
import com.xy.springboot.mapper.MessageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author XY
 * @description 针对表【message(消息)】的数据库操作Service实现
 * @createDate 2024-04-21 17:24:52
 */
@Service
@Slf4j
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message>
        implements MessageService {
    @Override
    public List<Message> getMessageByConversationId(Long conversationId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversationId", conversationId)
                .orderByAsc("createTime");
//        System.out.println("根据消息ID查询消息" + conversationId + "成功");
        return this.list(queryWrapper);
    }
}




