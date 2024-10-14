package com.hsx.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 弹幕表
 * @TableName t_danmu
 */
@Data
public class Danmu implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 视频id
     */
    private Long videoId;

    /**
     * 弹幕内容
     */
    private String content;

    /**
     * 弹幕出现时间
     */
    private String danmuTime;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}