package com.hsx.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户收藏分组表
 * @TableName t_collection_group
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectionGroup implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 收藏分组名称
     */
    private String name;

    /**
     * 收藏分组类型：0默认收藏分组
     */
    private Long type;

    /**
     * 该type下的videoIdList
     * */
    private List<Video> videoList;


    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}