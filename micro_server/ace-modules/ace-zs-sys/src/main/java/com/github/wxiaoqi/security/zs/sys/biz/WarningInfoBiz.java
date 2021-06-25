package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.zs.sys.entity.WarningInfo;
import com.github.wxiaoqi.security.zs.sys.mapper.WarningInfoMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物联网传感器报警信息
 */
@Service
public class WarningInfoBiz extends BaseBiz<WarningInfoMapper, WarningInfo> {

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
}
