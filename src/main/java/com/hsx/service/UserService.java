package com.hsx.service;

import com.alibaba.fastjson.JSONObject;
import com.hsx.netty.ChatMsg;
import com.hsx.pojo.PageResult;
import com.hsx.pojo.User;
import com.hsx.pojo.UserInfo;
import com.hsx.pojo.Video;


import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserService {
    void addUser(User user) throws Exception;

    String login(User user) throws Exception;

    User getUserInfo(Long userId);

    void updateUserInfos(UserInfo userInfo);

    User getUserById(Long followingId);

    List<UserInfo> getUserInfoByUserIds(Set<Long> followingIdSet);

    PageResult<UserInfo> pageListUserInfos(JSONObject params);

    Map<String, Object> loginForDts(User user) throws Exception;

    void logout(String refreshToken, Long userId);

    String refreshAccessToken(String refreshToken) throws Exception;

    void updateUserCoinsAmount(Long userId, Integer amount);

    List<Video> getVideoByUserId(Long userId);

    Long getCoinNum(Long userId);

    void changeAvatar(Long userId,String imgUrl);

    UserInfo getUserInfoByUserId(Long userId);

    Long saveMsg(ChatMsg chatMsg);
}
