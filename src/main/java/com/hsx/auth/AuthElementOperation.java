package com.hsx.auth;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限控制--页面元素操作表
 * @TableName t_auth_element_operation
 */
@Data
public class AuthElementOperation implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 页面元素名称
     */
    private String elementName;

    /**
     * 页面元素唯一编码
     */
    private String elementCode;

    /**
     * 操作类型：0可点击，1可见
     */
    private String operationType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}