package com.github.wxiaoqi.security.gyx.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.gyx.sys.entity.ConsultationInfo;

import java.util.List;

public interface ConsultationInfoMapper extends CommonMapper<ConsultationInfo> {
    List<JSONObject> getPageByState(Integer page, Integer size, String state, String title, String expertName, String userName);

    Integer getPageByStateCount(Integer page, Integer size, String state, String title, String expertName, String userName);
}
