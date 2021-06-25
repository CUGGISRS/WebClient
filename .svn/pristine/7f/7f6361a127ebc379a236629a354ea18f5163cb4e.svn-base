package com.github.wxiaoqi.security.info.sys.utils.model;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.info.sys.entity.FileInfo;

import java.util.List;

/**
 * @description: MyBatis的一对多JSON返回对象
 * <p>
 * 处理嵌套查询结果时，MyBatis会根据bean定义的属性类型来初始化嵌套的成员变量，
 * 主要看其是不是Collection
 * 如果这里不定义，那么嵌套返回结果里就只能返回一对一的结果，而不是一对多的
 * <p>
 * @author: gsy
 * @create: 2020-09-16 17:39
 **/

public class One2Many extends JSONObject {
    private List<FileInfo> fileInfos;

    private List<JSONObject> fileLists;

}
