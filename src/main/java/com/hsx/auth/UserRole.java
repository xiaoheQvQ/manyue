package com.hsx.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户角色关联表
 * @TableName t_user_role
 */

@Data
public class UserRole implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 创建时间
     */
    private Date createTime;

    private String roleName;

    private String roleCode;

    private static final long serialVersionUID = 1L;
}