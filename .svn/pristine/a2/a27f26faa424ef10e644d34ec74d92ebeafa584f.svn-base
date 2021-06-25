package com.github.wxiaoqi.security.gyx.sys.mapper;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.gyx.sys.entity.SupplyDemandAndFile;
import com.github.wxiaoqi.security.gyx.sys.entity.SupplyDemandInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface SupplyDemandInfoMapper extends Mapper<SupplyDemandInfo>, CommonMapper<SupplyDemandInfo> {
    List<SupplyDemandInfo> selectSimple();

    List<SupplyDemandInfo> selectSimpleByType(@Param("type") String type);

    /*已审核的供求信息数量*/
    int countInfoListByType(JSONObject jsonObject);

    /*已审核的供求信息*/
    List<JSONObject> listInfoByType(JSONObject jsonObject);


    List<SupplyDemandAndFile> listSupplyDemand(Integer page, Integer size, String pubType, String title, String contactPerson, String status);

    List<SupplyDemandAndFile> listSupplyByUser(Integer page, Integer size, Integer userId);

    List<SupplyDemandAndFile> getOneById(Integer id);
}
