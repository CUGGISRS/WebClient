package com.lomoye.easy.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lomoye.easy.domain.Job;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ExportMapper{

    List<JSONObject> getDataById(String tableName);

    Integer getDataNumById(@Param("tableName") String tableName,@Param("fieldName") String fieldName,@Param("fieldValue") String fieldValue);

}
