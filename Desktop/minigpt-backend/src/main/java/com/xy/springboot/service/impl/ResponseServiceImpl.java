package com.xy.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xy.springboot.model.entity.Response;
import com.xy.springboot.service.ResponseService;
import com.xy.springboot.mapper.ResponseMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author XY
* @description 针对表【response(消息)】的数据库操作Service实现
* @createDate 2024-04-21 17:34:44
*/
@Service
@Slf4j
public class ResponseServiceImpl extends ServiceImpl<ResponseMapper, Response>
    implements ResponseService{
    @Override
    public Response getResponseByMessageId(Long messageId) {
        QueryWrapper<Response> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("messageId", messageId);
        return this.getOne(queryWrapper);
    }

    @Override
    public List<Response> getResponseByMessageIds(List<Long> messageIds) {
        if (messageIds == null || messageIds.isEmpty()) {
            return new ArrayList<>();
        }
        QueryWrapper<Response> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("messageId", messageIds);
        return this.list(queryWrapper);
    }
}




