package com.hsx.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hsx.constant.MsgSignFlagEnum;
import com.hsx.constant.UserConstant;
import com.hsx.exception.ConditionException;
import com.hsx.mapper.FollowingGroupMapper;
import com.hsx.mapper.UserMapper;
import com.hsx.pojo.*;
import com.hsx.service.UserAuthService;
import com.hsx.service.UserService;
import com.hsx.utils.MD5Util;
import com.hsx.utils.RSAUtil;
import com.hsx.utils.TokenUtil;
import com.hsx.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.mysql.cj.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private FollowingGroupMapper followingGroupMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserAuthService userAuthService;

    /**
     * 用户注册
     * */
    @Override
    @Transactional
    public void addUser(User user) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser!=null){
            throw new ConditionException("该手机号已经注册！");
        }
        Date date = new Date();

        String salt = String.valueOf(date.getTime());
        String userPassword = RSAUtil.encrypt(user.getUserPassword());
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(userPassword);
        } catch (Exception e) {
            throw new ConditionException("数据解析异常！");
        }
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        user.setSalt(salt);
        user.setUserPassword(md5Password);
        user.setCreateTime(date);
        userMapper.addUser(user);



        //添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setAvatar(UserConstant.DEFAULT_AVATAR);
        userInfo.setSign(UserConstant.DEFAULT_SIGN);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_UNKNOW);
        userInfo.setUpdateTime(new Date());
        userInfo.setCreateTime(new Date());
        userMapper.addUserInfo(userInfo);


        //添加用户默认权限角色
        userAuthService.addUserDefaultRole(user.getId());

        //创建初始用户关注表
        List<FollowingGroup> list = this.CreateAllGroupsByUserId(user.getId());
        followingGroupMapper.createFollowingGroups(list);

        //创建初始用户硬币表
        this.createUserCoin(user.getId());

        //创建默认收藏分组
        this.CreateAllCollectionByUserId(user.getId());


    }

    /**
     * 用户登录
     * */
    @Override
    public String login(User user) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser==null){
            throw  new ConditionException("当前用户不存在");
        }

        String userPassword = user.getUserPassword();
        userPassword = RSAUtil.encrypt(userPassword);
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(userPassword);
        } catch (Exception e) {
            throw new ConditionException("数据解析异常!");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(!dbUser.getUserPassword().equals(md5Password)){
            throw new ConditionException("密码错误！");
        }
        //生成用户令牌，返回
        return TokenUtil.generateToken(dbUser.getId());

    }

    /**
     * 获取用户信息
     * */
    @Override
    public User getUserInfo(Long userId) {
        User user = userMapper.getUserById(userId);
        UserInfo userInfo = userMapper.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }

    /**
     * 更新用户信息
     * */
    @Override
    public void updateUserInfos(UserInfo userInfo) {
         userMapper.updateUserInfos(userInfo);
    }

    /**
     * 通过id查询用户
     * */
    @Override
    public User getUserById(Long followingId) {
        return userMapper.getUserById(followingId);
    }

    /**
     * 获取用户详细信息
     * */
    @Override
    public List<UserInfo> getUserInfoByUserIds(Set<Long> followingIdSet) {
        return userMapper.getUserInfoByUserIds(followingIdSet);
    }


    /**
     * 按nick模糊分页查询
     * */
    @Override
    public PageResult<UserInfo> pageListUserInfos(JSONObject params) {
        Integer page = params.getInteger("page");
        Integer size = params.getInteger("size");
        params.put("start",(page-1)*size);
        params.put("limit",size);
        Integer total = userMapper.pageCountUserInfos(params);
        List<UserInfo> list = new ArrayList<>();
        if(total > 0){
            list = userMapper.pageListUserInfos(params);
        }
        return new PageResult<>(total,list);
    }

    /**
     * 通过电话查询用户信息
     * */
    private User getUserByPhone(String phone) {
        return userMapper.getUserByPhone(phone);
    }

    /**
     * 双Token登录
     * */
    @Override
    public Map<String, Object> loginForDts(User user) throws Exception {
        String phone = user.getPhone();
        if(StringUtils.isNullOrEmpty(phone)){
            throw new ConditionException("手机号不能为空！");
        }
        User dbUser = this.getUserByPhone(phone);
        if(dbUser==null){
            throw  new ConditionException("当前用户不存在");
        }
        String userPassword = RSAUtil.encrypt(user.getUserPassword());
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(userPassword);
        } catch (Exception e) {
            throw new ConditionException("数据解析异常!");
        }
        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");
        if(!dbUser.getUserPassword().equals(md5Password)){
            throw new ConditionException("密码错误！");
        }
        //生成用户令牌，返回
        Long userId = dbUser.getId();
        String accessToken = TokenUtil.generateToken(userId);
        String refreshToken = TokenUtil.generateRefreshToken(userId);

        //保存refresh token到数据库
        userMapper.deleteRefreshToken(refreshToken,userId);
        userMapper.addRefreshToken(refreshToken,userId,new Date());
        Map<String,Object> result = new HashMap<>();
        result.put("accessToken",accessToken);
        result.put("refreshToken",refreshToken);


        //存入redis
        String key1 = userId + "-accessToken" ;
        String key2 = userId + "-refreshToken" ;
        redisTemplate.opsForValue().set(key1, JSONObject.toJSONString(accessToken),800, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(key2, JSONObject.toJSONString(refreshToken),7,TimeUnit.DAYS);

        return result;
    }

    /**
     * 退出登录
     * */
    @Override
    public void logout(String refreshToken, Long userId) {
        userMapper.deleteRefreshToken(refreshToken,userId);
        //删除redis里的数据
        redisTemplate.delete(refreshToken);
    }

    /**
     * 刷新Token
     * */
    @Override
    public String refreshAccessToken(String refreshToken) throws Exception {
        RefreshTokenDetail refreshTokenDetail = userMapper.getRefreshTokenDetail(refreshToken);
        if (refreshTokenDetail == null){
            throw new ConditionException("555","token过期");
        }
        Long userId = refreshTokenDetail.getUserId();
        userMapper.deleteRefreshToken(refreshToken,userId);
        String newRefreshToken = TokenUtil.generateToken(userId);
        userMapper.addRefreshToken(newRefreshToken,userId,new Date());

        //刷新redis里的refreshToken
        String key = userId + "-refreshToken" ;
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(refreshToken),7,TimeUnit.DAYS);

        return newRefreshToken;
    }

    @Override
    public void updateUserCoinsAmount(Long userId, Integer amount) {
        userMapper.updateUserCoinsAmount(userId,amount);
    }

    @Override
    public List<Video> getVideoByUserId(Long userId) {
        return userMapper.getVideoByUserId(userId);
    }

    @Override
    public Long getCoinNum(Long userId) {
        return userMapper.getCoinNum(userId);
    }

    @Override
    public void changeAvatar(Long userId,String imgUrl) {
        System.out.println("111");
        userMapper.changeAvatar(userId,imgUrl);
    }

    @Override
    public UserInfo getUserInfoByUserId(Long userId) {
        return userMapper.getUserInfoByUserId(userId);
    }


    /**创建默认用户硬币表数据
     * @TableName t_user_coin
     */
    public  void createUserCoin(Long userId){
        userMapper.createUserCoin(userId,0L);
    }

    /** 创建默认用户分组
     * @TableName t_following_group
     * */
    public  List<FollowingGroup> CreateAllGroupsByUserId(Long userId){
        List<FollowingGroup> list = new ArrayList<>();
        FollowingGroup followingGroup = new FollowingGroup();
        followingGroup.setUserId(userId);
        followingGroup.setCreateTime(new Date());
        followingGroup.setUpdateTime(new Date());
        followingGroup.setName("特别关注");
        followingGroup.setType("0");

        FollowingGroup followingGroup1 = new FollowingGroup();
        followingGroup1.setUserId(userId);
        followingGroup1.setCreateTime(new Date());
        followingGroup1.setUpdateTime(new Date());
        followingGroup1.setName("悄悄关注");
        followingGroup1.setType("1");

        FollowingGroup followingGroup2 = new FollowingGroup();
        followingGroup2.setUserId(userId);
        followingGroup2.setCreateTime(new Date());
        followingGroup2.setUpdateTime(new Date());
        followingGroup2.setName("默认关注");
        followingGroup2.setType("2");

        list.add(followingGroup);
        list.add(followingGroup1);
        list.add(followingGroup2);

        return list;
    }

    /**创建默认收藏分组
     * @TableName t_collection_group
     * */
    public void CreateAllCollectionByUserId(Long userId) {

        CollectionGroup collectionGroup = new CollectionGroup();
        collectionGroup.setUserId(userId);
        collectionGroup.setCreateTime(new Date());
        collectionGroup.setUpdateTime(new Date());
        collectionGroup.setName("默认收藏");
        collectionGroup.setType(0L);

        userMapper.CreateAllCollectionByUserId(collectionGroup);

    }

    @Transactional
    @Override
    public Long saveMsg(com.hsx.netty.ChatMsg chatMsg) {
        com.hsx.pojo.ChatMsg msgDB = new com.hsx.pojo.ChatMsg();
        Long msgId = (long) Math.abs(UUID.randomUUID().hashCode());
        msgDB.setId(msgId);
        msgDB.setAcceptUserId(chatMsg.getReceiverId());
        msgDB.setSendUserId(chatMsg.getSenderId());
        msgDB.setCreateTime(new Date());
        msgDB.setSignFlag(MsgSignFlagEnum.unsign.type); // 0是未签收，1是已签收
        msgDB.setMsg(chatMsg.getMsg());
        userMapper.saveMsg(msgDB);
        return msgId;
    }


}
