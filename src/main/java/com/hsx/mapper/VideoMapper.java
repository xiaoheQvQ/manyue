package com.hsx.mapper;

import com.alibaba.fastjson.JSONObject;
import com.hsx.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface VideoMapper {

    VideoLike getVideoLikeByVideoIdAndUserId(@Param("video_id") Long videoId,@Param("user_id") Long userId);

    void deleteVideoLikeByVideoIdAndUserId(@Param("video_id")Long videoId,@Param("user_id") Long userId);

    void addVideoLike(VideoLike videoLike);

    Long getVideoLikes(Long videoId);

    Integer insertVideo(Video video);

    void deleteVideoByVideoId(Long videoId);

    VideoCollection getVideoCollectionByVideoIdAndUserId(@Param("video_id") Long videoId,@Param("user_id") Long userId);

    void deleteVideoCollection(@Param("video_id") Long videoId,@Param("user_id") Long userId);

    void addVideoCollection(VideoCollection videoCollection);

    Long getVideoCollectionsByVideoId(Long videoId);

    Integer getUserCoinsAmount(Long userId);

    List<CoinCost> getCoinCostByVideoIdAndUserId(@Param("video_id") Long videoId, @Param("user_id") Long userId);

    void addVideoCoin(VideoCoin videoCoin);

    void updateVideoCoin(VideoCoin videoCoin);

    Integer getVideoCoinsAmount(Long videoId);

    void addCoinCost(CoinCost coinCost);

    VideoCoin getVideoCoinByVideoIdAndUserId(Long videoId, Long userId);

    void addVideo(Video video);


    Video getVideoByVideoId(Long videoId);

    List<Video> getVideoByUserId(Long userId);

    Integer pageCountVideoInfos(JSONObject params);

    List<Video> pageListVideoInfos(JSONObject params);

    Integer pageCountVideos(Map<String, Object> params);

    List<Video> pageListVideos(Map<String, Object> params);

    List<VideoCollection> getVideoCollectionsByUserId(Long userId);

    List<CollectionGroup> getVideoCollectionGroupNames(Long userId);

    Integer getVideoNum(Long videoId);

    String getUserNickByUserId(Long userId);

    List<Video> getVideoByTitle(String title);
}
