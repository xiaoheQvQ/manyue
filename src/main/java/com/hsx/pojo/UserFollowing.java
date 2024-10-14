package com.hsx.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关注表
 * @TableName t_user_following
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户关注表对应实体类")
public class UserFollowing implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 关注用户id
     */
    @ApiModelProperty("关注用户id")
    private Long followingId;

    /**
     * 关注分组id
     */
    @ApiModelProperty("关注分组id")
    private String type;

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

    /**
     * 用户信息
     * */
    @ApiModelProperty("用户信息")
    private UserInfo userInfo;

    private static final long serialVersionUID = 1L;


}