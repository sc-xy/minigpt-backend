package com.xy.springboot.model.vo;

import com.xy.springboot.model.entity.Message;
import com.xy.springboot.model.entity.Response;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class ConversationInfoVO implements Serializable {
    /**
     * 对话id
     */
    private Long id;

    /**
     * 用户询问
     */
    private List<Message> asks;

    /**
     * llm回答
     */
    private List<Response> answers;

    private static final long serialVersionUID = 1L;
}
