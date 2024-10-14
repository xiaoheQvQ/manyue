package com.hsx.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * 用户表
 * @TableName t_user
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户表对应实体类")
public class User implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String phone;

    /**
     * 邮箱
     */
    @ApiModelProperty("邮箱")
    private String email;

    /**
     * 密码
     */
    @ApiModelProperty("密码")
    private String userPassword;

    /**
     * 盐值
     */
    @ApiModelProperty("盐值")
    private String salt;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    private static final long serialVersionUID = 1L;

    /**
     * 详细信息
     * */
    @ApiModelProperty("详细信息")
    private UserInfo userInfo;


}