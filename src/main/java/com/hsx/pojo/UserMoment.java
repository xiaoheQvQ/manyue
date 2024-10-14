package com.hsx.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户动态表
 * @TableName t_user_moments
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "用户动态表对应实体类")
public class UserMoment implements Serializable {
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
     * 用户信息
     * */
    private UserInfo userInfo;

    /**
     * 动态类型：0视频，1直播，2动态专栏
     */
    @ApiModelProperty("动态类型：0视频，1直播，2动态专栏")
    private String type;


    @ApiModelProperty("视频id")
    private Long videoId;

    /**
     * 内容详情id
     */
    @ApiModelProperty("内容详情id")
    private Long contentId;

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
     * 上传的视频
     * */
    @ApiModelProperty("上传的视频")
    private Video video;

    private static final long serialVersionUID = 1L;
}