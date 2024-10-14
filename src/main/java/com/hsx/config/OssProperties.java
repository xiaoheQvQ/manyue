package com.hsx.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix="aliyun.oss")
public class OssProperties {
    private String endpoint;
    private String keyid;
    private String keysecret;
    private String bucketname;
}