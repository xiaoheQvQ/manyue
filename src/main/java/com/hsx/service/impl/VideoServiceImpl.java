package com.hsx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hsx.exception.ConditionException;
import com.hsx.mapper.VideoMapper;
import com.hsx.pojo.*;
import com.hsx.service.UserService;
import com.hsx.service.VideoService;
import com.hsx.utils.AliyunOSSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.*;

@Service
public class VideoServiceImpl implements VideoService {

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private UserService userService;

    /**
     * 点赞/取消点赞视频
     * */
    @Override
    public void addVideoLike(Long videoId, Long userId) {
        Video video = videoMapper.getVideoByVideoId(videoId);
        if (video == null){
            throw new ConditionException("非法视频");
        }
        VideoLike videoLike = videoMapper.getVideoLikeByVideoIdAndUserId(videoId,userId);
        if (videoLike != null){
            //已经点过赞，再点就是取消点赞
            videoMapper.deleteVideoLikeByVideoIdAndUserId(videoId,userId);
            return;
        }
        videoLike = new VideoLike();
        videoLike.setVideoId(videoId);
        videoLike.setUserId(userId);
        videoLike.setCreateTime(new Date());
        videoMapper.addVideoLike(videoLike);
    }

    @Override
    public boolean getVideoLikeStatus(Long videoId, Long userId) {

        boolean status = false;

        VideoLike videoLike = videoMapper.getVideoLikeByVideoIdAndUserId(videoId,userId);
        if (videoLike != null){
            status = true;
        }
        return status;
    }

    /**
     * 获取视频点赞数量
     * */
    @Override
    public Map<String, Object> getVideoLikes(Long videoId, Long userId) {
        Long count = videoMapper.getVideoLikes(videoId);
        VideoLike videoLike = videoMapper.getVideoLikeByVideoIdAndUserId(videoId,userId);
        boolean like = videoLike != null;
        Map<String,Object> result = new HashMap<>();
        result.put("count",count);
        result.put("like",like);
        return result;
    }

    /**
     * 上传视频
     * */
    @Override
    public Video Upload(Long userId, MultipartFile file, String id) {
        //获取文件名
        String filename = file.getOriginalFilename();
        //给文件拼接时间戳
        String newName = new Date().getTime() + "-" + filename;
        String fileNames = userId + "/" + "video/" + newName;
        //文件上传
        AliyunOSSUtil.uploadFileByte(file,  fileNames);

        //截取视频封面
        //拆分字符串
        String[] split = newName.split("\\.");
        String coverName = split[0];
        //拼接封面名
        String coverNames = userId + "/" + "cover/" + coverName + ".jpg";
        /*截取视频封面
         * 视频截取帧
         * */
        URL url = AliyunOSSUtil.videoInterceptCover(fileNames);
        /*
         * 上传视频封面至阿里云
         * 参数：
         *   netPath:网络路径
         * */
        try {
            AliyunOSSUtil.uploadNetFile(url.toString(), coverNames);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //拼接视频网络地址
        String videoUrl = "https://hsx-hb.oss-cn-beijing.aliyuncs.com/" + fileNames;
        String imgUrl =  "https://hsx-hb.oss-cn-beijing.aliyuncs.com/" + coverNames;
        Video video = new Video();
        video.setVideoUrl(videoUrl);
        video.setImgUrl(imgUrl);

        return video;
    }

    @Override
    @Transactional
    public void addVideoCollection(VideoCollection videoCollection, Long userId) {
        Long videoId = videoCollection.getVideoId();
        Long groupId = videoCollection.getGroupId();
        if (videoId == null || groupId == null){
            throw new ConditionException("参数异常");
        }
        Video video = videoMapper.getVideoByVideoId(videoId);
        if (video == null){
            throw new ConditionException("非法视频");
        }

        VideoCollection collection = videoMapper.getVideoCollectionByVideoIdAndUserId(videoId,userId);
        if(collection != null){ //已经收藏过
            //删除原有视频收藏
            videoMapper.deleteVideoCollection(videoId,userId);
            return;
        }

        //添加视频收藏
        videoCollection.setUserId(userId);
        videoCollection.setCreateTime(new Date());
        videoMapper.addVideoCollection(videoCollection);
    }

    @Override
    public Map<String, Object> getVideoCollectionsByVideoId(Long videoId, Long userId) {
        Long count = videoMapper.getVideoCollectionsByVideoId(videoId);
        VideoCollection videoCollection = videoMapper.getVideoCollectionByVideoIdAndUserId(videoId,userId);
        boolean collect = videoCollection != null;
        Map<String,Object> result = new HashMap<>();
        result.put("count",count);
        result.put("collection",collect);
        return result;
    }

    @Override
    @Transactional
    public void addVideoCoins(CoinCost coinCost, Long userId) {
        Long videoId = coinCost.getVideoId();
        Integer cost = coinCost.getCost(); //cost 这次要投币的数量
        if (videoId == null){
            throw new ConditionException("参数异常");
        }
        Video video = videoMapper.getVideoByVideoId(videoId);
        if(video == null){
            throw new ConditionException("非法视频");
        }
        //查询当前登录用户是否拥有足够的硬币
        Integer userCoinsAmount = videoMapper.getUserCoinsAmount(userId);
        userCoinsAmount = userCoinsAmount == null ? 0 : userCoinsAmount;
        if(cost > userCoinsAmount){
            throw new ConditionException("硬币数量不足");
        }
        //查询当前用户对该视频已经投了多少硬币
        List<CoinCost> costCoin = videoMapper.getCoinCostByVideoIdAndUserId(videoId,userId);
        Integer num = 0; //用户已经投币的数量
        for (CoinCost c : costCoin){
            if (c.getCost() != null){
                num += c.getCost();
            }
        }
        if (num > 2 || num + cost > 2){    //已经投过两个币
            throw new ConditionException("最多只能投两个币");
        }

        //查询当前视频已有投币量
        Map map =  getVideoCoins(videoId,userId);
        Integer amount = 0;
        if (map.get("count") != null){
            amount = (Integer) map.get("count");
        }


        VideoCoin videoCoin = new VideoCoin();
        videoCoin.setAmount(amount+cost);
        videoCoin.setUserId(userId);
        videoCoin.setVideoId(videoId);
        videoCoin.setCreateTime(new Date());

        System.out.println("amount="+amount);
        //更新视频的总投币量
        if (amount > 0){
            videoMapper.updateVideoCoin(videoCoin);
        }else{
            videoMapper.addVideoCoin(videoCoin);
        }

        //更新用户的硬币流水(这里是直接加入一条记录，不是在原有的记录上更新)
        coinCost.setCost(cost);
        coinCost.setCreateTime(new Date());
        videoMapper.addCoinCost(coinCost);

        //更新用户当前的硬币总数
        userService.updateUserCoinsAmount(userId,(userCoinsAmount - cost));
    }

    @Override
    public Map<String, Object> getVideoCoins(Long videoId, Long userId) {
        Integer count = videoMapper.getVideoCoinsAmount(videoId);
        List<CoinCost> coinCost = videoMapper.getCoinCostByVideoIdAndUserId(videoId,userId);
        boolean isCost = false;
        if (coinCost.size()>0){
            isCost = true;
        }
        Map<String,Object> result = new HashMap<>();
        result.put("count",count);
        result.put("cost",isCost); //coin > 0 投过币
        return result;
    }


    @Override
    @Transactional
    public void deleteVideo(Long userId,Long videoId) {

        Video video = videoMapper.getVideoByVideoId(videoId);
        /**
         * 通过id和user_id查找要删除的video
         * String imgPath = "video/1696327633166-阿姐挑逗.mp4";
         *         String videoPath = "video/1696327633166-阿姐挑逗.mp4";
         *         AliyunOSSUtil.deleteFile(fileName);
         *
         *          https://hsx-hb.oss-cn-beijing.aliyuncs.com/cover/1696393756191-小北星云.jpg
         *         https://hsx-hb.oss-cn-beijing.aliyuncs.com/video/1696393756191-小北星云.mp4
         * */

        String imgPath = userId + "/" + video.getImgUrl().substring(46);
        String videoPath = userId + "/" + video.getVideoUrl().substring(46);
        System.out.println(imgPath);
        System.out.println(videoPath);
        AliyunOSSUtil.deleteFile(imgPath);
        AliyunOSSUtil.deleteFile(videoPath);
        videoMapper.deleteVideoByVideoId(videoId);
    }
    @Override
    @Transactional
    public void addVideo(Video video) {
        videoMapper.addVideo(video);

    }

    @Override
    public Video getVideoByVideoId(Long videoId) {
        return videoMapper.getVideoByVideoId(videoId);
    }

    @Override
    public List<Video> getVideoByUserId(Long userId) {
        return videoMapper.getVideoByUserId(userId);
    }

    @Override
    public PageResult<Video> pageListVideoInfos(JSONObject params) {
        Integer page = params.getInteger("page");
        Integer size = params.getInteger("size");
        params.put("start",(page-1)*size);
        params.put("limit",size);
        Integer total = videoMapper.pageCountVideoInfos(params);
        List<Video> list = new ArrayList<>();
        if(total > 0){
            list = videoMapper.pageListVideoInfos(params);
        }
        return new PageResult<>(total,list);
    }

    @Override
    public PageResult<Video> pageListVideos(Integer size, Integer no, String area) {
        if (size == null || no == null){
            throw new ConditionException("参数异常");
        }
        Map<String, Object> params = new HashMap<>();
        params.put("start",(no-1)*size);
        params.put("limit",size);
        params.put("area",area);
        List<Video> list = new ArrayList<>();
        Integer total = videoMapper.pageCountVideos(params);
        if (total > 0){
            list = videoMapper.pageListVideos(params);

            for(Video v : list){

              Integer num =   videoMapper.getVideoNum(v.getVideoId());
              String nick = videoMapper.getUserNickByUserId(v.getUserId());
              v.setSeeNum(num);
              v.setUpNick(nick);

            }

        }
        return new PageResult<>(total,list);
    }

    @Override
    public List<CollectionGroup> getVideoCollectionsByUserId(Long userId) {

        List<VideoCollection> videoCollectionList = videoMapper.getVideoCollectionsByUserId(userId);

        List<CollectionGroup> collectionNames = videoMapper.getVideoCollectionGroupNames(userId);

        List<CollectionGroup> result = new ArrayList<>();

        for (CollectionGroup collectionGroup : collectionNames){

            List<Video> videoList = new ArrayList<>();
            for (VideoCollection videoCollection : videoCollectionList){
                if(collectionGroup.getType().equals(videoCollection.getGroupId())){
                    Video video1 = videoMapper.getVideoByVideoId(videoCollection.getVideoId());
                    System.out.println("///////"+video1);
                    videoList.add(video1);
                }
            }

            collectionGroup.setVideoList(videoList);
            result.add(collectionGroup);

        }

        return result;
    }

    @Override
    public List<Video> getVideoByTitle(String title) {

        return videoMapper.getVideoByTitle(title);
    }


}
