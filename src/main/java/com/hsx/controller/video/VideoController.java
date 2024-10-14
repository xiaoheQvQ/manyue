package com.hsx.controller.video;

import com.alibaba.fastjson.JSONObject;
import com.hsx.pojo.*;
import com.hsx.service.ElasticSearchService;
import com.hsx.service.UserService;
import com.hsx.service.VideoService;
import com.hsx.utils.UserUtils;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.common.recycler.Recycler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Api(tags = "视频端相关接口")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @Autowired
    private UserService userService;

    @Autowired
    private ElasticSearchService elasticSearchService;


    /**
     * 上传视频
     * */
    @PostMapping("/upload")
    @ApiOperation("上传视频")
    @Transactional
    public String upload(@RequestPart MultipartFile file,@RequestParam("str") String str ){
        Long userId = UserUtils.getCurrentUserId();

        JSONObject data = JSONObject.parseObject(str);

        String title = String.valueOf(data.get("title"));
        String description = String.valueOf(data.get("description"));
        String type = String.valueOf(data.get("type"));
        String area = String.valueOf(data.get("area"));


       Long id = userId + Math.abs(UUID.randomUUID().hashCode());

        Video video = videoService.Upload(userId,file,title);
        video.setVideoId(id);
        video.setUserId(userId);
        video.setTitle(title);
        video.setType(type);
        video.setArea(area);
        video.setDescription(description);
        video.setCreateTime(new Date());
        video.setUpdateTime(new Date());
        videoService.addVideo(video);

        elasticSearchService.addVideo(video);

        //System.out.println(JSONObject.toJSONString(video));
        return  JSONObject.toJSONString(video);
    }

    @GetMapping("/getEs")
    public JsonResponse<Video> getEsVideo(@RequestParam String keyword){
        System.out.println(keyword);
        Video video = elasticSearchService.getVideos(keyword);
        System.out.println("video"+video);
        return new JsonResponse<>(video);
    }

    @DeleteMapping("/d")
    public void delEsVideo(){
       elasticSearchService.deleteAllVideos();
    }


    /**
     * 删除视频
     * */
    @GetMapping("/delete")
    @ApiOperation("删除视频")
    public JsonResponse<String> delete(String videoId){
        Long video_id = Long.valueOf(videoId);
        Long userId = UserUtils.getCurrentUserId();
        videoService.deleteVideo(userId,video_id);
        return JsonResponse.success("删除成功！");
    }


    /**
     * 按user_id查询用户所有的视频
     */
    @GetMapping("/allVideo")
    @ApiOperation("按user_id查询用户所有的视频")
    public JsonResponse<List<Video>> getAllUserInfo(@RequestParam Long userId) {
        Long user_id = UserUtils.getCurrentUserId();
        List<Video> list = userService.getVideoByUserId(userId);
        return new JsonResponse<>(list);
    }

    /**
     * 瀑布流
     */
    @GetMapping("/videos")
    public JsonResponse<PageResult<Video>> pageListVideos(Integer size,Integer no,String area){
        PageResult<Video> result = videoService.pageListVideos(size,no,area);
        return new JsonResponse<>(result);
    }


    /**
     * 按user_id分页查询视频
     * */
    @GetMapping("/video-infos")
    @ApiOperation("按user_id分页查询视频")
    public JsonResponse<PageResult<Video>> pageListVideoInfos(@RequestParam Integer page, @RequestParam Integer size, @RequestParam  Long userId){
        Long user_id = UserUtils.getCurrentUserId();
        JSONObject params = new JSONObject();
        params.put("page",page);
        params.put("size",size);
        params.put("userId",userId);
        PageResult<Video> result =  videoService.pageListVideoInfos(params);

        return new JsonResponse<>(result);
    }


    /**
     * 点赞/取消点赞视频
     * */
    @PostMapping("/video-likes")
    @ApiOperation("点赞/取消点赞视频")
    public JsonResponse<String> addVideoLike(@RequestParam Long videoId){
        Long userId = UserUtils.getCurrentUserId();
        videoService.addVideoLike(videoId,userId);
        return JsonResponse.success();
    }

    /**
     * 查询点赞状态
     * */
    @GetMapping("/video-likes")
    @ApiOperation("查询点赞状态")
    public JsonResponse<Boolean> VideoLikeStatus(@RequestParam Long videoId){
        Long userId = UserUtils.getCurrentUserId();
        boolean status = videoService.getVideoLikeStatus(videoId,userId);
        return new JsonResponse<>(status);
    }


    /**
     * 查询视频点赞数量
     * */
    @GetMapping("/video-likesAmount")
    @ApiOperation("查询视频点赞数量")
    public JsonResponse<Map<String,Object>> getVideoLikes(@RequestParam Long videoId){
        Long userId = null;
        try{
            //未登录也能查看视频点赞
            userId = UserUtils.getCurrentUserId();
        }catch (Exception ignore){}
        Map<String,Object> result = videoService.getVideoLikes(videoId,userId);
        return new JsonResponse<>(result);
    }

    /**
     * 查询用户的视频收藏列表
     * */
    @GetMapping("/video-collectionsName")
    @ApiOperation("查询用户的视频收藏列表")
    public JsonResponse<List<CollectionGroup>> getVideoCollection(){
        Long userId = UserUtils.getCurrentUserId();
        List<CollectionGroup> result = videoService.getVideoCollectionsByUserId(userId);
        return new JsonResponse<>(result);
    }


    /**
     * 视频收藏/取消收藏
     */
    @PostMapping("/video-collections")
    @ApiOperation("视频收藏/取消收藏")
    public JsonResponse<String> addVideoCollection(@RequestBody VideoCollection videoCollection){
        Long userId = UserUtils.getCurrentUserId();
        videoService.addVideoCollection(videoCollection,userId);
        return JsonResponse.success();
    }


    /**
     * 查询视频收藏数量
     * */
    @GetMapping("/video-collectionsNum")
    @ApiOperation("查询视频收藏数量")
    public JsonResponse<Map<String,Object>> getVideoCollections(@RequestParam Long videoId){
        Long userId = UserUtils.getCurrentUserId();
        Map<String,Object> result = videoService.getVideoCollectionsByVideoId(videoId,userId);
        return new JsonResponse<>(result);
    }


    /**
     * 视频投币
     * */
    @PostMapping("/video-coins")
    @ApiOperation("视频投币")
    public JsonResponse<String> addVideoCoins(@RequestBody CoinCost coinCost){
        Long userId = UserUtils.getCurrentUserId();
        coinCost.setUserId(userId);
        videoService.addVideoCoins(coinCost,userId);
        return JsonResponse.success();
    }


    /**
     * 查询视频投币数量
     * */
    @GetMapping("/video-coins")
    @ApiOperation("查询视频投币数量")
    public JsonResponse<Map<String,Object>> getVideoCoins(@RequestParam Long videoId){
        Long userId = UserUtils.getCurrentUserId();
        Map<String,Object> result = videoService.getVideoCoins(videoId,userId);
        return new JsonResponse<>(result);
    }


    /**
     * 通过video_id查询视频信息
     * */
    @GetMapping("/video_detail")
    @ApiOperation("通过video_id查询视频信息")
    public JsonResponse<Video> getVideoByVideoId(@RequestParam Long videoId){
        Long userId = UserUtils.getCurrentUserId();
        Video video =  videoService.getVideoByVideoId(videoId);
        return new JsonResponse<>(video);
    }

    /**
     * 获取自己的所有视频
     * */
    @GetMapping("video_mine_detail")
    public JsonResponse<List<Video>> getVideoAll(){
        Long userId = UserUtils.getCurrentUserId();
        List<Video> video = videoService.getVideoByUserId(userId);
        return new JsonResponse<>(video);
    }

    /**
     * 获取某个用户的所有视频
     * */
    @GetMapping("/video_other_detail")
    public JsonResponse<List<Video>> getVideoAllByUserId(@RequestParam Long userId){
        Long user_id = UserUtils.getCurrentUserId();
        List<Video> video = videoService.getVideoByUserId(userId);
        return new JsonResponse<>(video);
    }


    /**
     * 根据title查询视频
     * */
    @GetMapping("/video_title")
    public JsonResponse<List<Video>> getVideoByTitle(@RequestParam("title") String title){

        Long user_id = UserUtils.getCurrentUserId();
        if (StringUtils.isNullOrEmpty(title)){
            return new JsonResponse<>(null);
        }
        List<Video> video = videoService.getVideoByTitle(title);
        return new JsonResponse<>(video);

    }


}
