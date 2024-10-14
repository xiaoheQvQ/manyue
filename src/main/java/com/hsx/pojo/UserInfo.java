package com.hsx.pojo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户基本信息表
 * @TableName t_user_info
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户基本信息表对应实体类")
public class UserInfo implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 用户id（关联）
     */
    @ApiModelProperty("用户id（关联）")
    private Long userId;

    /**
     * 昵称
     */
    @ApiModelProperty("昵称")
    private String nick;

    /**
     * 头像
     */
    @ApiModelProperty("头像")
    private String avatar;

    /**
     * 签名
     */
    @ApiModelProperty("签名")
    private String sign;

    /**
     * 性别：0男，1女，2未知
     */
    @ApiModelProperty("性别：0男，1女，2未知")
    private String gender;

    /**
     * 生日
     */
    @ApiModelProperty("生日")
    private String birth;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 互关
     */
    @ApiModelProperty("互关")
    private Boolean followed;


    private static final long serialVersionUID = 1L;

}