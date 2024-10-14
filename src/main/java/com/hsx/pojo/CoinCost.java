package com.hsx.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 硬币流水表
 * @TableName t_coin_cost
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "硬币流水表对应实体类")
public class CoinCost implements Serializable {
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
     * 视频id
     */
    @ApiModelProperty("视频id")
    private Long videoId;

    /**
     * 硬币流水
     */
    @ApiModelProperty("硬币流水")
    private Integer cost;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    private static final long serialVersionUID = 1L;
}