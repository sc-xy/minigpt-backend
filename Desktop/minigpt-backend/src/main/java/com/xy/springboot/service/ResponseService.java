package com.xy.springboot.service;

import com.xy.springboot.model.entity.Response;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author XY
* @description 针对表【response(消息)】的数据库操作Service
* @createDate 2024-04-21 17:34:44
*/
public interface ResponseService extends IService<Response> {
    /**
     * 根据消息ID查询回复
     */
    Response getResponseByMessageId(Long messageId);
    /**
     * 根据消息ID批量查询回复
     */
    List<Response> getResponseByMessageIds(List<Long> messageIds);
}
