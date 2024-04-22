package com.xy.springboot.model.dto.conversion;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConversationQueryRequest implements Serializable {
    private Long conversationId;

    private static final long serialVersionUID = 1L;
}
