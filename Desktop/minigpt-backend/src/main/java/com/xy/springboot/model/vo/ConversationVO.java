package com.xy.springboot.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ConversationVO implements Serializable {
    /**
     * 对话id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 标题
     */
    private String title;

    /**
     * 是否已经对话
     */
    private Integer hasTalked;

    private static final long serialVersionUID = 1L;
}
