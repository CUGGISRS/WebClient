package com.github.wxiaoqi.security.zs.sys.feign.service;

import com.github.wxiaoqi.security.com.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.zs.sys.feign.IDataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典服务类
 */
@Service
public class IDataDictionaryService {
    @Autowired
    private IDataDictionary iDataDictionary;

    public List<DataDictionary> getAllMayToCondition(String type, String remark, String name, String code) {
        return iDataDictionary.getAllMayToCondition(type, remark, name, code).getData();
    }
}
