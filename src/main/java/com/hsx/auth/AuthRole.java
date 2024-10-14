package com.hsx.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限控制--角色表
 * @TableName t_auth_role
 */
@Data
public class AuthRole implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 唯一编码
     */
    private String code;

    private static final long serialVersionUID = 1L;
}