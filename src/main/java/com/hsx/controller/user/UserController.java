package com.hsx.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.hsx.constant.UserConstant;
import com.hsx.exception.ConditionException;
import com.hsx.pojo.*;
import com.hsx.service.UserFollowingService;
import com.hsx.service.UserService;
import com.hsx.utils.AliyunOSSUtil;
import com.hsx.utils.UserUtils;
import com.mysql.cj.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Api(tags = "用户相关接口")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private UserFollowingService userFollowingService;

    /**
     * 用户注册
     */
    @PostMapping("/users")
    @ApiOperation("用户注册")
    public JsonResponse<String> addUser(@RequestBody User user) throws Exception {
        userService.addUser(user);
        return JsonResponse.success("注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/userLogin")
    @ApiOperation(value = "用户登录")
    public JsonResponse<String> login(@RequestBody User user) throws Exception {
        String token = userService.login(user);
        return JsonResponse.success(token);
    }

    /**
     * 通过token里的userId获取用户信息
     */
    @GetMapping("/userLogin")
    @ApiOperation("通过token里的userId获取用户信息")
    public JsonResponse<User> getUserInfo() {
        Long userId = UserUtils.getCurrentUserId();
        User user = userService.getUserInfo(userId);
        return new JsonResponse<>(user);
    }

    /**
     * 通过token里的userId修改用户信息
     */
    @PostMapping("/user-infos")
    @ApiOperation("通过token里的userId修改用户信息")
    public JsonResponse<UserInfo> updateUserInfos(@RequestBody UserInfo userInfo) {
        Long userId = UserUtils.getCurrentUserId();
        userInfo.setUserId(userId);
        userInfo.setUpdateTime(new Date());
        userService.updateUserInfos(userInfo);
        return new JsonResponse<>(userInfo);
    }



    /**
     * 修改用户头像
     * */
    @PostMapping("/change_avatar")
    @ApiOperation("修改用户头像")
    @Transactional
    public JsonResponse<String> changeAvatar(@RequestPart MultipartFile file) throws IOException {
        Long userId = UserUtils.getCurrentUserId();
        // 判断上传的文件是否为空
        if (file.isEmpty()) {
            // 是：抛出异常
            throw new ConditionException("上传的头像文件不允许为空");
        }

        // 判断上传的文件大小是否超出限制值
        if (file.getSize() > UserConstant.AVATAR_MAX_SIZE) { // getSize()：返回文件的大小，以字节为单位
            // 是：抛出异常
            throw new ConditionException("不允许上传超过" + (UserConstant.AVATAR_MAX_SIZE / 1024) + "KB的头像文件");
        }

        // 判断上传的文件类型是否超出限制
        String contentType = file.getContentType().replace("image/", "");
        System.out.println(contentType);
        // boolean contains(Object o)：当前列表若包含某元素，返回结果为true；若不包含该元素，返回结果为false
        if (!UserConstant.AVATAR_TYPES.contains(contentType)) {
            // 是：抛出异常
            throw new ConditionException("仅支持" + UserConstant.AVATAR_TYPES.stream().collect(Collectors.joining(",")));
        }

        // 上传到阿里云OSS
        String imgUrl =  AliyunOSSUtil.uploadLocalFiles(userId,file);

        // 将头像写入到数据库中
        userService.changeAvatar(userId,imgUrl);
        System.out.println(imgUrl);

        return JsonResponse.success(imgUrl);
    }















    /**
     * 双token登录
     */
    @PostMapping("/user-dts")
    @ApiOperation("双token登录")
    public JsonResponse<Map<String, Object>> loginForDts(@RequestBody User user) throws Exception {
        Map<String, Object> map = userService.loginForDts(user);
        return new JsonResponse<>(map);
    }

    /**
     * 退出
     */
    @DeleteMapping("/refresh-tokens")
    @ApiOperation("用户退出")
    public JsonResponse<String> logout(HttpServletRequest request) { //请求相关的集合 HttpServletRequest
        String refreshToken = request.getHeader("refreshToken");
        Long userId = UserUtils.getCurrentUserId();
        userService.logout(refreshToken, userId);
        return JsonResponse.success();
    }

    /**
     * 刷新token
     */
    @PostMapping("/access-tokens")
    @ApiOperation("刷新token")
    public JsonResponse<String> refreshToken(HttpServletRequest request) throws Exception {
        String refreshToken = request.getHeader("refreshToken");
        String accessToken = userService.refreshAccessToken(refreshToken);
        return new JsonResponse<>(accessToken);
    }




    /**
     * 获取用户的硬币数量
     * */
    @GetMapping("/coinNum")
    @ApiOperation("获取用户的硬币数量")
    public JsonResponse<Map> getCoinNum(){
        Long userId = UserUtils.getCurrentUserId();
        Long countNum = userService.getCoinNum(userId);
        Map<String,Long> map = new HashMap<>();
        map.put("coinNum",countNum);
        return new JsonResponse<>(map);
    }

    /**
     * 关注up主
     */
    @PostMapping("/userFollowings")
    @ApiOperation("关注up主")
    public JsonResponse<String> addUserFollowings(@RequestBody UserFollowing userFollowing) {
        Long userId = UserUtils.getCurrentUserId();
        userFollowing.setUserId(userId);
        userFollowingService.addUserFollowings(userFollowing);
        return JsonResponse.success();
    }


    /**
     * 取消关注
     */
    @DeleteMapping("/userFollowings")
    @ApiOperation("取消关注")
    public JsonResponse<String> deleteUserFollowings(@RequestParam Long followingId) {
        Long userId = UserUtils.getCurrentUserId();
        userFollowingService.deleteUserFollowings(userId, followingId);
        return JsonResponse.success();
    }

    /**
     * 获取用户的粉丝信息和数量
     * */
    @GetMapping("/getUserFans")
    public JsonResponse<List<UserInfo>> getUserFans(@RequestParam("userId") Long userId){

        List<UserFollowing> list = userFollowingService.getUserFans(userId);

        List<UserInfo> result = new ArrayList<>();

        for(UserFollowing u : list){

                result.add( userService.getUserInfoByUserId(u.getUserId()) );
        }
        return new JsonResponse<>(result);
    }

    /**
     * 用户分页查询
     */
    @GetMapping("/user-infos")
    @ApiOperation("用户分页查询")
    public JsonResponse<PageResult<UserInfo>> pageListUserInfos(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String nick) {
        Long userId = UserUtils.getCurrentUserId();
        JSONObject params = new JSONObject();
        params.put("page", page);
        params.put("size", size);
        params.put("nick", nick);
        params.put("userId", userId);
        PageResult<UserInfo> result = userService.pageListUserInfos(params);
        //判断是否关注该用户
        if (result.getTotal() > 0) {
            List<UserInfo> checkedUserInfoList = userFollowingService.checkFollowingStatus(result.getList(), userId);
            result.setList(checkedUserInfoList);
        }
        return new JsonResponse<>(result);
    }


    /**
     * 通过useId查询用户详细信息
     * */
    @GetMapping("/getUserInfoByUserId")
    public JsonResponse<UserInfo> getUserInfoByUserId(@RequestParam("userId") Long userId){
        UserInfo userInfo = userService.getUserInfoByUserId(userId);
        return new JsonResponse<>(userInfo);
    }



}
