package com.hsx.repository;

import com.hsx.pojo.Video;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
public interface VideoRepository extends ElasticsearchRepository<Video,Long> { //对应类型 : 主键类型

    Video findByTitleLike(String keyword);


}
