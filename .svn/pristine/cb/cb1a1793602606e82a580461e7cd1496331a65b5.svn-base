package com.github.wxiaoqi.security.dzsw.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.dzsw.sys.entity.QualityProductsAndFile;
import com.github.wxiaoqi.security.dzsw.sys.entity.QualityProductsInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface QualityProductsInfoMapper extends CommonMapper<QualityProductsInfo> {
    List<QualityProductsAndFile> getMostPopularityProducts(int limit);

    List<QualityProductsAndFile> listQualityProducts(Integer page, Integer size, String productCategory, String status, String productName);
}
