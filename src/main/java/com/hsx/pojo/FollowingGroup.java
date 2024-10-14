package com.hsx.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户关注分组表
 * @TableName t_following_group
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户关注分组表对应实体类")
public class FollowingGroup implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty("主键值")
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 关注分组名称
     */
    @ApiModelProperty("关注分组名称")
    private String name;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 关注分组类型：0 特别关注，1 悄悄关注，2 默认关注，3 用户自定义关注
     */
    @ApiModelProperty("关注分组类型：0 特别关注，1 悄悄关注，2 默认关注，3 用户自定义关注")
    private String type;

    /**
     * 关注的用户信息
     * */
    @ApiModelProperty("关注的用户信息")
    private List<UserInfo> followingUserInfoList;

    private static final long serialVersionUID = 1L;

}