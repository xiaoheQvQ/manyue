package com.hsx.mapper;

import com.hsx.pojo.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserFollowingMapper {
    void addUserFollowing(UserFollowing userFollowing);

    void deleteUserFollowing(@Param("user_id") Long userId,@Param("following_id") Long followingId);

    List<UserFollowing> getUserFollowings(Long userId);

    List<UserFollowing> getUserFans(Long userId);

    void deleteUserFollowingGroups(@Param("type") String type,@Param("user_id") Long userId);

    List<UserFollowing> getUserFollowingByTypeAndUserId(@Param("type") String type,@Param("user_id") Long userId);


}
