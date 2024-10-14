package com.hsx.mapper;

import com.alibaba.fastjson.JSONObject;
import com.hsx.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Mapper
public interface UserMapper {


    User getUserByPhone(String phone);

    Integer addUser(User user);

    void addUserInfo(UserInfo userInfo);

    User login(User user);

    User getUserById(Long userId);

    @Select("select * from t_user_info where user_id = #{userId}")
    UserInfo getUserInfoByUserId(Long userId);

    void updateUserInfos(UserInfo userInfo);

    List<UserInfo> getUserInfoByUserIds(@Param("userIdList") Set<Long> followingIdSet);

    Integer pageCountUserInfos(JSONObject params);

    List<UserInfo> pageListUserInfos(JSONObject params);

    Integer deleteRefreshToken(@Param("refreshToken") String refreshToken,@Param("userId") Long userId);

    Integer addRefreshToken(@Param("refreshToken") String refreshToken,@Param("userId") Long userId,@Param("createTime") Date date);

    RefreshTokenDetail getRefreshTokenDetail(String refreshToken);

    void updateUserCoinsAmount(@Param("userId") Long userId,@Param("amount") int amount);

    List<Video> getVideoByUserId(Long userId);

    Long getCoinNum(Long userId);

    /** 创建默认用户硬币表数据 */
    void createUserCoin(@Param("userId") Long userId,@Param("amount") Long amount);

    void CreateAllCollectionByUserId(CollectionGroup collectionGroup);

    void changeAvatar(@Param("userId") Long userId,@Param("imgUrl") String imgUrl);

    void saveMsg(ChatMsg msgDB);
}
