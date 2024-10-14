package com.hsx.service;

import com.alibaba.fastjson.JSONObject;
import com.hsx.pojo.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface VideoService {
    void addVideoLike(Long videoId, Long userId);

    boolean getVideoLikeStatus(Long videoId, Long userId);

    Map<String, Object> getVideoLikes(Long videoId, Long userId);

    Video Upload(Long userId, MultipartFile file, String id);

    void deleteVideo(Long userId,Long videoId);

    void addVideoCollection(VideoCollection videoCollection, Long userId);

    Map<String, Object> getVideoCollectionsByVideoId(Long videoId,Long userId);

    void addVideoCoins(CoinCost cost, Long userId);

    Map<String, Object> getVideoCoins(Long videoId, Long userId);

    void addVideo(Video video);


    Video getVideoByVideoId(Long videoId);

    List<Video> getVideoByUserId(Long userId);


    PageResult<Video> pageListVideoInfos(JSONObject params);

    PageResult<Video> pageListVideos(Integer size, Integer no, String area);

    List<CollectionGroup> getVideoCollectionsByUserId(Long userId);

    List<Video> getVideoByTitle(String title);
}
