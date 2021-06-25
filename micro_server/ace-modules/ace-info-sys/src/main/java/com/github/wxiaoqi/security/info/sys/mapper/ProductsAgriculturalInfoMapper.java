package com.github.wxiaoqi.security.info.sys.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.info.sys.entity.ProductsAgriculturalInfo;
import com.github.wxiaoqi.security.info.sys.entity.ProductsAndFile;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface ProductsAgriculturalInfoMapper extends Mapper<ProductsAgriculturalInfo>, CommonMapper<ProductsAgriculturalInfo> {

    //    JSONObject listProduct();
    List<ProductsAndFile> listProduct(Integer page, Integer size, String authType, String allType);

    int getLinkCount(String authType, String allType);

}
