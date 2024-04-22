package com.xy.springboot.model.dto.user;

import java.io.Serializable;
import lombok.Data;

/**
 * 用户更新请求，这里只用于更新密码
 *
 */
@Data
public class UserUpdateRequest implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户旧密码
     */
    private String oldPassword;

    /**
     * 用户新密码
     */
    private String newPassword;

    private static final long serialVersionUID = 1L;
}