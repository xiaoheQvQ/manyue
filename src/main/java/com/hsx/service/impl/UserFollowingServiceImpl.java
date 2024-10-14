package com.hsx.service.impl;

import com.hsx.constant.UserConstant;
import com.hsx.exception.ConditionException;
import com.hsx.mapper.FollowingGroupMapper;
import com.hsx.mapper.UserFollowingMapper;
import com.hsx.pojo.*;
import com.hsx.service.FollowingGroupService;
import com.hsx.service.UserFollowingService;
import com.hsx.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserFollowingServiceImpl implements UserFollowingService {

    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserFollowingMapper userFollowingMapper;

    @Autowired
    private FollowingGroupMapper followingGroupMapper;


    /**
     * 关注用户
     */
    @Transactional //事务处理
    @Override
    public void addUserFollowings(UserFollowing userFollowing) {
        String type = userFollowing.getType();  //用户给up设置的分组
        if(type == null){   //未设置分类，即为默认分类
            //  UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT = 2 默认分组
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_TYPE_DEFAULT, userFollowing.getUserId());
            userFollowing.setType(followingGroup.getType());
        }else{    //已设置分组
            FollowingGroup followingGroup = followingGroupService.getByType(type,userFollowing.getUserId());
            if(followingGroup == null){    //前端传过来的分组不是 0，1，2 或自定义的 即不符合系统要求
                throw new ConditionException("关注的分组不存在");
            }
        }

        Long followingId = userFollowing.getFollowingId();  //up主的id
        User user = userService.getUserById(followingId);   //查到up主
        System.out.println("up："+user);
        if(user == null){    //up主不存在
            throw new ConditionException("关注的用户不存在");
        }

        userFollowingMapper.deleteUserFollowing(userFollowing.getUserId(),followingId);   //删除已存在的关联关系
        userFollowing.setCreateTime(new Date());
        userFollowingMapper.addUserFollowing(userFollowing);   //设置现在的关联关系
    }

    @Override
    public void deleteUserFollowings(Long userId ,Long followingId) {
        userFollowingMapper.deleteUserFollowing(userId,followingId);
    }


    /**
     * 获取用户关注列表
     * */
    @Override
    public List<FollowingGroup> getUserFollowings(Long userId) {

        /**
         * 通过用户的id查询用户关联的所有用户和对应的分组信息
         * list : user_id following_id type
         * */
        List<UserFollowing> list = userFollowingMapper.getUserFollowings(userId);


        /**
         * 把用户关注的up的id封装一个集合
         * */
        Set<Long> followingIdSet = list.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        /**
         * 使用 List<UserInfo>userInfo 统一存放通过up的id查询出来的up信息表
         * userInfoList : user_id(实际是following_id) avatar gender ... 即 up的信息
         * */
        List<UserInfo> userInfoList = new ArrayList<>();
        if(followingIdSet.size() > 0){  //用户关注的up数不为0
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);
        }


        /**
         * 通过用户的id查询用户关联的所有粉丝和对应的分组信息
         * */
        List<UserFollowing> fanList = userFollowingMapper.getUserFans(userId);


        /**
         * 把用户关注的up的信息存入UserFollowing类的userInfo属性中  //这个属性不存储于用户-up信息表中，只在需要的时候通过上述方法获得
         * userFollowing : user_id(用户) following_id type ( up的 avatar gender ...)
         * */

        for (UserFollowing userFollowing : list){

            boolean followed = false;   //默认不是互关
            for (UserFollowing fans:fanList){
                if (userFollowing.getFollowingId().equals(fans.getUserId())){  //关注的upId和粉丝的id相同，即互关
                    followed = true;
                }
            }


            for (UserInfo userInfo : userInfoList){
                if(userFollowing.getFollowingId().equals(userInfo.getUserId())){    //upId和用户信息表的id相同
                    userInfo.setFollowed(followed); //设置是否互关属性（不在数据库）
                    userFollowing.setUserInfo(userInfo); //存放关注的up的详细信息
                }
            }
        }

        /**
         * 通过userId查询用户已经设置的关注分组类型（即设置了 默认 悄悄  自定义关注类型 中的那几个）
         * groupList : user_id name(特别关注 悄悄关注 默默关注 或者自定义) type (0 1 2 或者自定义)
         * */
        List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);

        /**
         * 新建一个列表 allGroup，存放用户关注的所有up的信息
         * allGroup : 全部分组 关注的所有up的详细信息
         * */
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);
        /**
         * 新建一个列表 result，存放最终查询到的用户关注的up的信息
         * */
        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);



        for (FollowingGroup group : groupList){         //依次遍历 默认 悄悄  自定义关注类型 等用户创建的分组
            List<UserInfo> infoList = new ArrayList<>();
            for(UserFollowing userFollowing : list){    //list：通过用户的id查询对应的用户-up关系表 ( userId upId type=getFollowingId ... )
                if(group.getType().equals(userFollowing.getType())){   //按关注类型分组
                    infoList.add(userFollowing.getUserInfo());  //把每个分组类型依次每个存入infoList中
                }
            }

            group.setFollowingUserInfoList(infoList);   //把每种分组类型中的up信息依次分组类型中
            result.add(group);
        }
        return result;

    }

    /**
     * 获取用户的粉丝列表
     * */
    @Override
    public List<UserFollowing> getUserFans(Long userId) {
        /**
         * 通过用户的id查询用户关联的所有粉丝和对应的分组信息
         * */
        List<UserFollowing> fanList = userFollowingMapper.getUserFans(userId);

        /**
         * fanIdSet : 粉丝的id集合
         * */
        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getUserId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        /**
         * userInfoList : 粉丝的详细信息
         * */
        if(fanIdSet.size() > 0){
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }

        /**
         * followingList : 用户关注的列表
         * */
        List<UserFollowing> followingList = userFollowingMapper.getUserFollowings(userId);
        for(UserFollowing fan : fanList){
            for(UserInfo userInfo : userInfoList){
                if(fan.getUserId().equals(userInfo.getUserId())){
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }

            for (UserFollowing following : followingList){
                if (following.getFollowingId().equals(fan.getUserId())){
                    fan.getUserInfo().setFollowed(true);
                }
            }
        }

        return fanList;
    }

    /**
     * 添加分组
     * */
    @Override
    public String addUserFollowingGroups(FollowingGroup followingGroup) {
        followingGroup.setCreateTime(new Date());
        followingGroup.setType(UserConstant.USER_FOLLOWING_GROUP_TYPE_USER);

        List<FollowingGroup>list = followingGroupMapper.getUserFollowingGroups(followingGroup.getUserId()); //获取用户的所有分组

        for (FollowingGroup group : list){
            if (group.getName().equals(followingGroup.getName())){
                throw new ConditionException("分组名称不可重复!");
            }
        }

        followingGroup.setType(String.valueOf(list.size()));
        followingGroupService.addFollowingGroup(followingGroup);
        return followingGroup.getType();
    }

    @Override
    public List<UserInfo> checkFollowingStatus(List<UserInfo> UserInfoList, Long userId) {

            List<UserFollowing> userFollowingList = userFollowingMapper.getUserFollowings(userId);
            for(UserInfo userInfo : UserInfoList){
                userInfo.setFollowed(false);
                for(UserFollowing userFollowing: userFollowingList){
                    if(userFollowing.getFollowingId().equals(userInfo.getUserId())){
                        userInfo.setFollowed(true);
                    }
                }
            }
            return UserInfoList;
    }

    @Override
    public void deleteUserFollowingGroups(String type, Long userId) {
        userFollowingMapper.deleteUserFollowingGroups(type,userId);
    }

    @Override
    public List<UserFollowing> getUserFollowingByTypeAndUserId(String type, Long userId) {
        return userFollowingMapper.getUserFollowingByTypeAndUserId(type,userId);
    }


    /**
     * 获取用户的分组
     * */
    @Override
    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        return followingGroupService.getUserFollowingGroups(userId);
    }


}
