package com.hsx.service;

import com.hsx.pojo.Danmu;

import java.text.ParseException;
import java.util.List;

public interface DanMuService {

    public void addDanMu(Danmu danmu);

    public List<Danmu> getDanMus(Long videoId, String startTime, String endTime) throws ParseException;

    public void asyncAddDanmu(Danmu danmu);

    public void addDanMusToRedis(Danmu danmu);

}
