package com.hsx.pojo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;


/**
 * 视频投稿记录表
 * @TableName t_video
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "v",createIndex = true)
@ApiModel(description = "视频投稿记录表对应实体类")
public class Video implements Serializable {

    /**
     * 主键id
     */
    @Id
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 用户id
     */
    @Field(type = FieldType.Long)
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 投稿id
     */
    @ApiModelProperty("投稿id")
    private Long videoId;

    /**
     * 视频链接
     */
    @Field(type = FieldType.Text)
    @ApiModelProperty("视频链接")
    //elasticSearch注释 @Field(type = FieldType.Text)
    private String videoUrl;

    /**
     * 封面链接
     */
    @ApiModelProperty("封面链接")
    private String imgUrl;

    /**
     * 视频标题
     */
    @Field(type = FieldType.Text)
    @ApiModelProperty("视频标题")
    private String title;

    /**
     * 视频类型：0原创，1转载
     */
    @ApiModelProperty("视频类型：0原创，1转载")
    private String type;

    /**
     * 视频时长
     */
    @ApiModelProperty("视频时长")
    private String duration;

    /**
     * 所在分区
     */
    @ApiModelProperty("所在分区")
    private String area;

    /**
     * 视频简介
     */
    @Field(type = FieldType.Text)
    @ApiModelProperty("视频简介")
    private String description;

    /**
     * 创建时间
     */
    @Field(type = FieldType.Date)
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @Field(type = FieldType.Date)
    @ApiModelProperty("更新时间")
    private Date updateTime;


    /**
     * 视频观看次数
     * */
    @ApiModelProperty("视频观看次数")
    private Integer seeNum;

    /**
     * 投稿用户昵称
     * */
    @ApiModelProperty("投稿用户昵称")
    private String upNick;


    /**
     * 标签列表
     * @ApiModelProperty("上传的视频")
     */

   // private List<VideoTag> videoTagList;

    private static final long serialVersionUID = 1L;
}