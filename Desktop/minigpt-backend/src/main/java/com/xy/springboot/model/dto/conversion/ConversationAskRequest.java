package com.xy.springboot.model.dto.conversion;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConversationAskRequest implements Serializable {
    /**
     * 对话id
     */
    private Long conversationId;
    /**
     * 询问
     */
    private String ask;

    private static final long serialVersionUID = 1L;
}
