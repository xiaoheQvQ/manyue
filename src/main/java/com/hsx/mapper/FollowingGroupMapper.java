package com.hsx.mapper;

import com.hsx.pojo.FollowingGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FollowingGroupMapper {
    FollowingGroup getByType(@Param("type") String type,@Param("user_id") Long userId);

    FollowingGroup getById(Long groupId);

    List<FollowingGroup> getByUserId(Long userId);

    void createFollowingGroups(List<FollowingGroup> list);

    List<FollowingGroup> getUserFollowingGroups(Long userId);

    void addFollowingGroup(FollowingGroup followingGroup);
}
