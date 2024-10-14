package com.hsx.pojo;


import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Comment {
    /**
     * 主键
     */
    private Long id;

    /**
     * 评论者
     */
    private Long userId;

    /**
     * 上级评论
     */
    private Long parentId;

    /**
     * 文章
     */
    private Long videoId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 下级评论
     */
    private List<Comment> sons;

    /**
     *  子评论数目
     * */
    private Long sonsCount;

    /**
     * 发生日期
     */

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 显示评论发布具查看的时间
     * */
    private String timeDifference;

    /**
     * 是否是根评论
     * */
    private int commentStatus;

    /**
     *  评论发布者的信息
     * */
    private UserInfo userInfo;

    /**
     *  被回复的用户的信息
     * */
    private UserInfo replyUserInfo;

}
