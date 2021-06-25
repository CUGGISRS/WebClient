package com.github.wxiaoqi.security.dzsw.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.dzsw.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.dzsw.sys.entity.PriceQuotationInfo;

import java.util.List;

public interface PriceQuotationInfoMapper extends CommonMapper<PriceQuotationInfo> {
    int countInfoListByTypeAndTime(JSONObject jsonObject);

    int countAllInfoListByTime(JSONObject jsonObject);

    List<JSONObject> listInfoByTypeAndTime(JSONObject jsonObject);

    List<JSONObject> listAllInfoByTime(JSONObject jsonObject);

    List<DataDictionary> listDic();

    List<JSONObject> getListRecentPriceInfo(JSONObject jsonObject);

    List<JSONObject> getAvailableDate(Integer page, Integer size);

    Integer getAvailableDateTotal();

    List<JSONObject> getPriceListByOneDate(String date);
}
