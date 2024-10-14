package com.hsx.mapper;

import com.hsx.pojo.UserMoment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMomentsMapper {
    void addUserMoments(UserMoment userMoment);
}
