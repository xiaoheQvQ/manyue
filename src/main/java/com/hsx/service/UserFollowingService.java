package com.hsx.service;

import com.hsx.pojo.FollowingGroup;
import com.hsx.pojo.UserFollowing;
import com.hsx.pojo.UserInfo;

import java.util.List;

public interface UserFollowingService {
    void addUserFollowings(UserFollowing userFollowing);

    void deleteUserFollowings(Long userId ,Long followingId);

    List<FollowingGroup> getUserFollowings(Long userId);

    List<UserFollowing> getUserFans(Long userId);


    List<FollowingGroup> getUserFollowingGroups(Long userId);

    String addUserFollowingGroups(FollowingGroup followingGroup);

    List<UserInfo> checkFollowingStatus(List<UserInfo> list, Long userId);

    void deleteUserFollowingGroups(String type, Long userId);

    List<UserFollowing> getUserFollowingByTypeAndUserId(String type, Long userId);


}
