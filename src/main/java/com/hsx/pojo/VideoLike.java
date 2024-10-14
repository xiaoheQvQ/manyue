package com.hsx.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 视频点赞表
 * @TableName t_video_like
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "视频点赞表对应实体类")
public class VideoLike implements Serializable {

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
     * 视频投稿id
     */
    @ApiModelProperty("视频投稿id")
    private Long videoId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 是否点赞
     * */
    @ApiModelProperty("是否点赞")
    private boolean like;

    private static final long serialVersionUID = 1L;
}