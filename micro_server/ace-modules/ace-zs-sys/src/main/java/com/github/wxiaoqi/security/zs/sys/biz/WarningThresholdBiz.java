package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.zs.sys.entity.WarningThreshold;
import com.github.wxiaoqi.security.zs.sys.mapper.WarningThresholdMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物联网传感器报警阈值
 */
@Service
public class WarningThresholdBiz extends BaseBiz<WarningThresholdMapper, WarningThreshold> {


    /**
     * 通过传感器id删除数据
     */
    public int delBySId(Integer sId) {
        if (sId == null) {
            return 0;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("sensorId", sId);
        return delByToMap(toMap);
    }

    /**
     * 通过传感器id集合删除数据
     */
    public int delBySIds(List<Integer> sIds) {
        String inFiled = "sensorId";
        return delByInFiledMayToMap(sIds, inFiled, null);
    }

    /**
     * 通过传感器id集合查询数据
     */
    public List<WarningThreshold> getBySIds(List<Integer> sIds) {
        String inFiled = "sensorId";
        return getByInFiledMayToMap(sIds, inFiled, null);
    }
}
