package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.GrowEnvironment;
import com.github.wxiaoqi.security.zs.sys.mapper.GrowEnvironmentMapper;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 生长环境
 */
@Service
public class GrowEnvironmentBiz extends BaseBiz<GrowEnvironmentMapper, GrowEnvironment> {
    /**
     * 修改某一产品的数据
     */
    public int updateByPId(GrowEnvironment obj, Integer productId) {
        if (productId == null) {
            return 0;
        }
        String fieldName = "productId";
        return updateByFiled(obj, productId, fieldName);
    }

    /**
     * 通过传感器id删除数据
     */
    public int delBySId(Integer sId) {
        if (sId == null) {
            return 0;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("deviceSensorId", sId);
        return delByToMap(toMap);
    }

    /**
     * 通过传感器id集合删除数据
     */
    public int delBySIds(List<Integer> sIds) {
        String inFiled = "deviceSensorId";
        return delByInFiledMayToMap(sIds, inFiled, null);
    }

    /**
     * 获得某一产品的信息
     */
    public List<GrowEnvironment> getByPId(Object pId) {
        if (MyObjectUtil.noNullOrEmpty(pId)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("productId", pId);
            return getByToMap(andToMap);
        }
        return null;
    }

    /**
     * 删除某一产品的信息
     */
    public int delByPId(Integer pId) {
        if (pId != null) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("productId", pId);
            return delByToMap(andToMap);
        }
        return 0;
    }

    /**
     * 通过产品id集合删除数据
     */
    public int delByPIds(List<Integer> pIds) {
        String inFiled = "productId";
        return delByInFiledMayToMap(pIds, inFiled, null);
    }


    /**
     * 通过集合获得传感器id集合
     */
    public List<Integer> getDSIdsByList(List<GrowEnvironment> list) {
        if (list != null) {
            List<Integer> dsIds = list.stream().map(GrowEnvironment::getDeviceSensorId)
                    .filter(Objects::nonNull).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(dsIds) > 0) {
                return dsIds;
            }
        }
        return null;
    }

}
