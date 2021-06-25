package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceGroup;
import com.github.wxiaoqi.security.zs.sys.mapper.DeviceGroupMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物联网设备分组
 */
@Service
public class DeviceGroupBiz extends BaseBiz<DeviceGroupMapper, DeviceGroup> {
    /**
     * 获得某一创建者（即某一企业）的分组
     */
    public List<DeviceGroup> getByCId(Object cId) {
        if (cId != null) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("creatorId", cId);
            return getByToMap(toMap);
        }
        return null;
    }
}
