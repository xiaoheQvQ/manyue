package com.hsx.service;

import com.hsx.pojo.UserMoment;

import java.util.List;

public interface UserMomentsService {

    public void addUserMoments(UserMoment userMoment) throws Exception;

    public List<UserMoment> getUserSubscribedMoments(Long userId);

}
