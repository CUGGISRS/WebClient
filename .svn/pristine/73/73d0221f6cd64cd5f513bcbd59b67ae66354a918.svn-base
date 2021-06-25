package com.github.wxiaoqi.security.zs.sys.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceSensorData;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface DeviceSensorDataMapper extends CommonMapper<DeviceSensorData> {

    public Date getMinTimeBySId(Integer sId);

    public List<DeviceSensorData> pageBySId(
            @Param("sId") Integer sId,
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("interval") Integer interval,
            @Param("page") Integer page,
            @Param("limit") Integer limit,
            @Param("orderBy") String orderBy);

    public int getTotalBySId(
            @Param("sId") Integer sId,
            @Param("start") Date start,
            @Param("end") Date end,
            @Param("interval") Integer interval);
}
