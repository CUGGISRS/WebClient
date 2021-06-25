package com.github.wxiaoqi.security.dzsw.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.dzsw.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.dzsw.sys.entity.MarketingArticleInfo;

import java.util.List;

public interface MarketingArticleInfoMapper extends CommonMapper<MarketingArticleInfo> {
    List<JSONObject> getMostPopularityMarketing(JSONObject jsonObject);

    int countListByType(JSONObject jsonObject);

    List<MarketingArticleInfo> listByType(JSONObject jsonObject);

    int countAllList(JSONObject jsonObject);

    List<MarketingArticleInfo> listAllInfo(JSONObject jsonObject);

    List<DataDictionary> listDic();
}
