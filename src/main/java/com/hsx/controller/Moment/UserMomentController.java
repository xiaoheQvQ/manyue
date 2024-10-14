package com.hsx.controller.Moment;

import com.hsx.anno.LimitedData;
import com.hsx.anno.LimitedRole;
import com.hsx.constant.AuthRoleConstant;
import com.hsx.mapper.VideoMapper;
import com.hsx.pojo.JsonResponse;
import com.hsx.pojo.UserInfo;
import com.hsx.pojo.UserMoment;
import com.hsx.pojo.Video;
import com.hsx.service.UserMomentsService;
import com.hsx.service.UserService;
import com.hsx.utils.UserUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class UserMomentController {

    @Autowired
    private UserMomentsService userMomentsService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoMapper videoMapper;

    /**
     * 用户创建动态并推送给粉丝
     * */
    @LimitedRole(limitedRoleCodeList = {AuthRoleConstant.ROLE_LV0})
    @LimitedData
    @PostMapping("/user-moments")
    @ApiOperation("用户创建动态并推送给粉丝")
    //暂时  content_id = videoId
    public JsonResponse<String> addUserMoments(@RequestBody UserMoment userMoment) throws Exception {
        Video video = videoMapper.getVideoByVideoId(userMoment.getVideoId());
        //Long video_id = System.currentTimeMillis();
        //video.setId(video_id);
        Long userId = UserUtils.getCurrentUserId();
        userMoment.setUserId(userId);
        userMoment.setVideo(video);
        userMoment.setCreateTime(new Date());
        //userMoment.setContentId(userMoment.getVideoId());
        //System.out.println("content_id"+video_id);
        userMomentsService.addUserMoments(userMoment);
        return JsonResponse.success();
    }

    /**
     * 获取关注用户的动态
     * */
    @GetMapping("/user-subscribed-moments")
    @ApiOperation("获取关注用户的动态")      //返回的是video_id user_id content_id
    public JsonResponse<List<UserMoment>> getUserSubscribedMoments(){
        Long userId = UserUtils.getCurrentUserId();
        List<UserMoment> list = userMomentsService.getUserSubscribedMoments(userId);
        if (list!=null){
            for (UserMoment u:list){
                u.setUserInfo(userService.getUserInfoByUserId(userId));
            }
        }
        /**
         * {
         *     "code": "0",
         *     "msg": "成功",
         *     "data": [
         *         {
         *             "id": null,
         *             "userId": 22,
         *             "type": "0", //0 原创 1 转载
         *             "videoId": 760517547,
         *             "contentId": 1696751154040,  //待开发
         *             "createTime": 1696751154041,
         *             "updateTime": null,
         *             "video": null
         *         },
         *         {
         *             "id": null,
         *             "userId": 22,
         *             "type": "0",
         *             "videoId": 760517547,
         *             "contentId": 1696751174760,
         *             "createTime": 1696751174761,
         *             "updateTime": null,
         *             "video": null
         *         }
         *     ]
         * }
         * */
        return new JsonResponse<>(list);
    }


}
