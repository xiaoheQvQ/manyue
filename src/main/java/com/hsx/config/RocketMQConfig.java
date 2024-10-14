package com.hsx.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hsx.constant.UserMomentsConstant;
import com.hsx.pojo.UserFollowing;
import com.hsx.pojo.UserMoment;
import com.hsx.service.UserFollowingService;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RocketMQConfig {

    @Value("${rocketmq.name.server.address}")
    private String nameServerAddr;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private UserFollowingService userFollowingService;

    //用户动态生产者
    @Bean("momentsProducer")
    public DefaultMQProducer momentsProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_MOMENTS);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }


    //用户动态消费者
    @Bean("momentsConsumer")
    public DefaultMQPushConsumer momentsConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_MOMENTS);
        consumer.setNamesrvAddr(nameServerAddr);
        consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS, "*");
        //并发监听
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                MessageExt messageExt = msgs.get(0);
                if (messageExt == null) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                String bodyStr = new String(messageExt.getBody());
                UserMoment userMoments = JSONObject.toJavaObject(JSONObject.parseObject(bodyStr), UserMoment.class);
                Long userId = userMoments.getUserId();
                //获取粉丝，进行推送
                List<UserFollowing> userFans = userFollowingService.getUserFans(userId);
                for (UserFollowing fan : userFans) {
                    String key = "subscribed-" + fan.getUserId();
                    String subscribedListStr = redisTemplate.opsForValue().get(key);
                    List<UserMoment> subscribedList;
                    if (StringUtils.isBlank(subscribedListStr)) {
                        subscribedList = new ArrayList<>();
                    } else {
                        subscribedList = JSONArray.parseArray(subscribedListStr, UserMoment.class);
                    }
                    subscribedList.add(userMoments);
                    redisTemplate.opsForValue().set(key, JSONObject.toJSONString(subscribedList));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        return consumer;
    }

    //弹幕生产者
    @Bean("danmusProducer")
    public DefaultMQProducer danmusProducer() throws MQClientException {
        DefaultMQProducer producer = new DefaultMQProducer(UserMomentsConstant.GROUP_DANMUS);
        producer.setNamesrvAddr(nameServerAddr);
        producer.start();
        return producer;
    }

    //弹幕消费者
    @Bean("danmusConsumer")
    public DefaultMQPushConsumer danmusConsumer() throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(UserMomentsConstant.GROUP_DANMUS);
        consumer.setNamesrvAddr(nameServerAddr);
        //订阅生产者
        consumer.subscribe(UserMomentsConstant.TOPIC_MOMENTS, "*");
        //添加监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt messageExt = list.get(0);
                if (messageExt == null) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                String bodyStr = new String(messageExt.getBody());
                JSONObject jsonObject = JSONObject.parseObject(bodyStr);
                String sessionId = jsonObject.getString("sessionId");
                String message = jsonObject.getString("message");
                /**
                 * WebSocketService webSocketService = WebSocketService.WEBSOCKET_MAP.get(sessionId);
                 *                 if (webSocketService.getSession().isOpen()) {
                 *                     try {
                 *                         webSocketService.sendMessage(message);
                 *                     } catch (Exception e) {
                 *                         e.printStackTrace();
                 *                     }
                 *                 }
                 *
                 * */

                //标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        return consumer;
    }

}