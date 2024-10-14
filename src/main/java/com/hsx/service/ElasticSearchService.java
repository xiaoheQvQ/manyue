package com.hsx.service;

import com.hsx.pojo.Video;

public interface ElasticSearchService {

    void addVideo(Video video);

    Video getVideos(String keyword);

    void deleteAllVideos();
}
