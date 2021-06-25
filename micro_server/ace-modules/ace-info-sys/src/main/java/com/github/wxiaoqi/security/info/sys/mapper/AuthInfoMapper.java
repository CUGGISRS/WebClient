package com.github.wxiaoqi.security.info.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.info.sys.entity.AuthInfo;

import java.util.List;

public interface AuthInfoMapper extends CommonMapper<AuthInfo> {
    List<JSONObject> query(Integer page, Integer size);

    List<JSONObject> getByProductId();

    List<JSONObject> getAuthInfoById(Integer id);
}
