package com.hsx.mapper;

import com.hsx.pojo.Danmu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DanMuMapper {

    Integer addDanMu(Danmu danmu);
    List<Danmu> getDanMus(Map<String,Object> params);

}