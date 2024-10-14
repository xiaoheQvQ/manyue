package com.hsx.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 视频收藏表
 * @TableName t_video_collection
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "视频收藏表对应实体类")
public class VideoCollection implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 视频投稿id
     */
    @ApiModelProperty("视频投稿id")
    private Long videoId;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 收藏分组
     */
    @ApiModelProperty("收藏分组")
    private Long groupId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}