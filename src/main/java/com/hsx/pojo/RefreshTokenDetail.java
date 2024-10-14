package com.hsx.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
@ApiModel(description = "刷新令牌记录表对应实体类")
@Data
public class RefreshTokenDetail {

    /**
     * 主键id
     * */
    @ApiModelProperty("主键id")
    private Long id;

    /**
     * 刷新令牌
     * */
    @ApiModelProperty("刷新令牌")
    private String refreshToken;

    /**
     * 用户id
     * */
    @ApiModelProperty("用户id")
    private Long userId;

    /**
     * 创建时间
     * */
    @ApiModelProperty("创建时间")
    private Date createTime;



}
