package com.hsx.service.impl;

import com.hsx.constant.UserConstant;
import com.hsx.mapper.FollowingGroupMapper;
import com.hsx.pojo.FollowingGroup;
import com.hsx.service.FollowingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FollowingGroupServiceImpl implements FollowingGroupService {
    @Autowired
    private FollowingGroupMapper followingGroupMapper;

    @Override
    public FollowingGroup getByType(String type, Long userId) {
        return followingGroupMapper.getByType(type,userId);
    }

    @Override
    public FollowingGroup getById(Long groupId) {
        return followingGroupMapper.getById(groupId);
    }

    @Override
    public List<FollowingGroup> getByUserId(Long userId) {
        return followingGroupMapper.getByUserId(userId);
    }

    @Override
    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupMapper.getUserFollowingGroups(userId);
    }

    @Override
    public void addFollowingGroup(FollowingGroup followingGroup) {
        followingGroupMapper.addFollowingGroup(followingGroup);
    }


}
