package com.hsx.controller.Danmu;

import com.hsx.pojo.Danmu;
import com.hsx.pojo.JsonResponse;
import com.hsx.service.DanMuService;
import com.hsx.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DanMuController {


    @Autowired
    private DanMuService danmuService;

    //查询弹幕
    @GetMapping("/danmus")
    public JsonResponse<List<Danmu>> getDanmus(@RequestParam Long videoId,
                                               String startTime,
                                               String endTime) throws Exception {
        List<Danmu> list;
        try{
            //判断是游客还是用户登录
            UserUtils.getCurrentUserId();
            //用户模式可以使用按时间筛选
            Map<String,Object> map = new HashMap<>();
            map.put("startDate",startTime);
            map.put("endDate",endTime);
            map.put("videoId",videoId);
            list = danmuService.getDanMus(videoId,startTime,endTime);
        }catch (Exception e){
            //游客模式，不允许用户进行时间排序
            Map<String,Object> map = new HashMap<>();
            map.put("videoId",videoId);
            list = danmuService.getDanMus(videoId,startTime,endTime);
        }
        return new JsonResponse<>(list);
    }

}
