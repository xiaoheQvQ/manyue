package com.hsx.service.impl;

import com.hsx.pojo.Video;
import com.hsx.repository.VideoRepository;
import com.hsx.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

    @Autowired
    private VideoRepository videoRepository;

    @Override
    public void addVideo(Video video) {
        System.out.println("save"+video);
        videoRepository.save(video);
    }

    public Video getVideos(String keyword){
       return videoRepository.findByTitleLike(keyword);
    }

    public void deleteAllVideos(){
        videoRepository.deleteAll();
    }
}
