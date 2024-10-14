package com.hsx.service;

import com.hsx.pojo.FollowingGroup;

import java.util.List;

public interface FollowingGroupService {
    FollowingGroup getByType(String userFollowingGroupTypeDefault, Long userId);

    FollowingGroup getById(Long groupId);

    List<FollowingGroup> getByUserId(Long userId);


    List<FollowingGroup> getUserFollowingGroups(Long userId);

    void addFollowingGroup(FollowingGroup followingGroup);
}
