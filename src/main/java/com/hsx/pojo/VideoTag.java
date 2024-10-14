package com.hsx.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 视频标签关联表
 * @TableName t_video_tag
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "视频标签关联表对应实体类")
public class VideoTag implements Serializable {

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 视频id
     */
    @ApiModelProperty("视频id")
    private Long videoId;

    /**
     * 标签id
     */
    @ApiModelProperty("标签id")
    private Long tagId;

    /**
     * 标签
     */
    @ApiModelProperty("标签")
    private String blank;


    private static final long serialVersionUID = 1L;
}