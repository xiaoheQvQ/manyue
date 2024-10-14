package com.hsx.controller.user;

import com.hsx.exception.ConditionException;
import com.hsx.pojo.FollowingGroup;
import com.hsx.pojo.JsonResponse;
import com.hsx.pojo.UserFollowing;
import com.hsx.service.UserFollowingService;
import com.hsx.service.UserMomentsService;
import com.hsx.utils.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "用户分组相关接口")
public class UserFollowingController {

    @Autowired
    private UserFollowingService userFollowingService;

    /**
     * 创建关注分组
     * */
    @PostMapping("/user-following-groups")
    @ApiOperation("添加分组")
    public JsonResponse<List<FollowingGroup>> addUserFollowingsGroups(@RequestBody FollowingGroup followingGroup){
        Long userId = UserUtils.getCurrentUserId();
        followingGroup.setUserId(userId);
        userFollowingService.addUserFollowingGroups(followingGroup);
        List<FollowingGroup> followingGroups = userFollowingService.getUserFollowingGroups(userId);
        return new JsonResponse<>(followingGroups);
    }


    /**
     * 通过type删除关注分组
     * */
    @DeleteMapping("/user-following-groups")
    @ApiOperation("通过分组id删除关注分组")
    public JsonResponse deleteUserFollowingsGroupsById(@RequestParam String type){
        Long userId = UserUtils.getCurrentUserId();

        //先看该分组下有没有用户，有的话不能删除
        List<UserFollowing> list = userFollowingService.getUserFollowingByTypeAndUserId(type,userId);

        if (list.size() > 0) {
            throw new ConditionException("分组下有用户，不能删除！");
        }
        userFollowingService.deleteUserFollowingGroups(type,userId);
        return  JsonResponse.success();
    }

    /**
     * 查询用户分组
     * */
    @GetMapping("/user-following-groups")
    @ApiOperation("查询用户分组")
    public JsonResponse<List<FollowingGroup>> getUserFollowingGroups(){
        Long userId = UserUtils.getCurrentUserId();
        List<FollowingGroup> list = userFollowingService.getUserFollowingGroups(userId);
        return new JsonResponse<>(list);
    }

    /**
     * 获取用户关注列表
     * */
    @GetMapping("/user-followings")
    @ApiOperation("获取用户关注列表")
    public JsonResponse<List<FollowingGroup>> getUserFollowings(){
        Long userId = UserUtils.getCurrentUserId();
        List<FollowingGroup> userFollowings = userFollowingService.getUserFollowings(userId);
        return new JsonResponse<>(userFollowings);
    }

    /**
     * 获取用户粉丝列表
     * */
    @GetMapping("/user-fans")
    @ApiOperation("获取用户粉丝列表")
    public JsonResponse<List<UserFollowing>> getFans(){
        Long userId = UserUtils.getCurrentUserId();
        List<UserFollowing> userFans = userFollowingService.getUserFans(userId);
        return new JsonResponse<>(userFans);
    }


}
