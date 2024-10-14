package com.hsx.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hsx.constant.UserMomentsConstant;
import com.hsx.exception.ConditionException;
import com.hsx.mapper.UserMomentsMapper;
import com.hsx.pojo.UserMoment;
import com.hsx.service.UserMomentsService;
import com.hsx.utils.RocketMQUtil;
import com.mysql.cj.util.StringUtils;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;


@Service
public class UserMomentsServiceImpl implements UserMomentsService {

    @Autowired
    private UserMomentsMapper userMomentsMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void addUserMoments(UserMoment userMoment) throws Exception {

        userMoment.setCreateTime(new Date());
        //新建用户动态
        userMomentsMapper.addUserMoments(userMoment);
        //向mq发送消息，告诉相关订阅者，用户发送了新的动态
        DefaultMQProducer producer = (DefaultMQProducer) applicationContext.getBean("momentsProducer");
        //message 发送的消息  （  主题  内容 ）
        Message message = new Message(UserMomentsConstant.TOPIC_MOMENTS, JSONObject.toJSONString(userMoment).getBytes());
        RocketMQUtil.syncSendMsg(producer,message);//RocketMQUtil.syncSendMsg(producer,message) 自定义的同步发送

    }

    public List<UserMoment> getUserSubscribedMoments(Long userId) {
        String key = "subscribed-" + userId;
        /** System.out.println(stringRedisTemplate.opsForValue().get(key));*/
        String listStr = (String) stringRedisTemplate.opsForValue().get(key);
        stringRedisTemplate.delete(key);
        //System.out.println("listStr="+listStr);
        //System.out.println("key="+redisTemplate.opsForValue().get(key));
        return JSONArray.parseArray(listStr, UserMoment.class);
    }
}
