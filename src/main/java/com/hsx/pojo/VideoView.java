package com.hsx.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 视频观看记录表
 * @TableName t_video_view
 */

@Data
public class VideoView implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 视频id
     */
    private Long videoId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * ip
     */
    private String ip;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}