package com.github.wxiaoqi.security.info.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.info.sys.entity.CarouselInfo;

import java.util.List;

public interface CarouselInfoMapper extends CommonMapper<CarouselInfo> {
    List<JSONObject> page(Integer page, Integer size, String targetSystem);
}
