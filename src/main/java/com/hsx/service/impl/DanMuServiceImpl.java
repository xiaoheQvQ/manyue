package com.hsx.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hsx.mapper.DanMuMapper;
import com.hsx.pojo.Danmu;
import com.hsx.service.DanMuService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class DanMuServiceImpl implements DanMuService {

    private static final String DANMU_KEY = "dm-video-";

    @Autowired
    private DanMuMapper danMuMapper;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    public void addDanMu(Danmu danmu){
        danMuMapper.addDanMu(danmu);
    }

    @Async //实现异步功能
    public void asyncAddDanmu(Danmu danmu){
        danMuMapper.addDanMu(danmu);
    }

    /**
     * 查询策略是优先查询redis中的弹幕数据
     * 如果没有的话，查询数据库，把查到的数据写入redis中
     * */
    public List<Danmu> getDanMus(Long videoId, String startTime, String endTime) throws ParseException {

        String key = DANMU_KEY + videoId;
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list;
        if(!StringUtils.isNullOrEmpty(value)){
            list = JSONArray.parseArray(value,Danmu.class);
            if (!StringUtils.isNullOrEmpty(startTime) && !StringUtils.isNullOrEmpty(endTime)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
                Date startDate = sdf.parse(startTime);
                Date endDate = sdf.parse(endTime);
                List<Danmu> childList = new ArrayList<>();
                for (Danmu danmu : list){
                    Date createTime = danmu.getCreateTime();
                    if(createTime.after(startDate) && createTime.before(endDate)){
                        childList.add(danmu);
                    }
                }
                list = childList;
            }
        }else {
            Map<String,Object> params = new HashMap<>();
            params.put("videoId",videoId);
            params.put("startTime",startTime);
            params.put("endTime",endTime);
            list = danMuMapper.getDanMus(params);
            //保存到redis
            redisTemplate.opsForValue().set(key, JSONObject.toJSONString(list));
        }
        return list;

    }

    public void addDanMusToRedis(Danmu danmu){

        String key = "danMu-video-"+danmu.getVideoId();
        String value = redisTemplate.opsForValue().get(key);
        List<Danmu> list = new ArrayList<>();
        if(!StringUtils.isNullOrEmpty(value)){
            list = JSONArray.parseArray(value,Danmu.class);
        }
        list.add(danmu);
        redisTemplate.opsForValue().set(key,JSONArray.toJSONString(danmu));
    }

}
