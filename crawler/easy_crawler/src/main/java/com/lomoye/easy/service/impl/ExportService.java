package com.lomoye.easy.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.lomoye.easy.dao.ExportMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: gsy
 * @create: 2020-10-21 10:31
 **/
@Service
public class ExportService {

    @Resource
    private ExportMapper exportMapper;

    public List<JSONObject> getDataById(String tableName) {
        return exportMapper.getDataById(tableName);
    }

    public Integer getDataNumById(String tableName, String fieldName, String fieldValue) {
//        try {
            return exportMapper.getDataNumById(tableName, fieldName, fieldValue);
//        } catch (Exception e) {
//            return 0;
//        }
    }
}
