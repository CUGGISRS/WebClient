package com.github.wxiaoqi.security.gyx.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.gyx.sys.entity.ExpertInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ExpertInfoMapper extends CommonMapper<ExpertInfo> {
    int countExpertInfoList(String expertName, String serviceArea,String expertise);

    List<JSONObject> listExpertInfo(Integer page, Integer limit, String expertName, String serviceArea,String expertise);
}
