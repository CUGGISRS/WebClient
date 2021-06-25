package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceSensor;
import com.github.wxiaoqi.security.zs.sys.mapper.DeviceSensorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 物联网设备传感器
 */
@Service
public class DeviceSensorBiz extends BaseBiz<DeviceSensorMapper, DeviceSensor> {
    @Autowired
    private DeviceSensorDataBiz deviceSensorDataBiz;
    @Autowired
    private GrowEnvironmentBiz growEnvironmentBiz;
    @Autowired
    private WarningInfoBiz warningInfoBiz;
    @Autowired
    private WarningThresholdBiz warningThresholdBiz;


    /**
     * 查询某些id的数据
     */
    public List<DeviceSensor> getByIds(List<Integer> ids) {
        String inFiled = "id";
        return getByInFiledMayToMap(ids, inFiled, null);
    }

    /**
     * 修改某些id的数据
     */
    public int updByIds(DeviceSensor obj, List<Integer> ids) {
        String inFiled = "id";
        return updateByInFiledMayToMap(obj, ids, inFiled, null);
    }

    /**
     * 查询某一分组的传感器
     */
    public List<DeviceSensor> getByGId(Object gId) {
        if (gId != null) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("groupId", gId);
            return getByToMap(toMap);
        }
        return null;
    }

    /**
     * 删除某一分组的传感器
     */
    public int delByGId(Integer gId) {
        if (gId != null) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("groupId", gId);
            return delByToMap(toMap);
        }
        return 0;
    }

    /**
     * 删除某些分组的传感器
     */
    public int delByGIds(List<Integer> gIds) {
        String inField = "groupId";
        return delByInFiledMayToMap(gIds, inField, null);
    }

    /**
     * 删除一条传感器、传感器数据、生长环境、传感器预警阈值、传感器预警信息
     */
    public void delLinkById(Integer id) throws Exception {
        int index = deleteById(id);
        if (index == 0) {
            throw new Exception("传感器删除失败");
        }
        //同时删除关联传感器数据
        deviceSensorDataBiz.delBySId(id);
        //同时删除关联生长环境
        growEnvironmentBiz.delBySId(id);
        //删除关联传感器预警阈值、传感器预警信息
        warningThresholdBiz.delBySId(id);
        warningInfoBiz.delBySId(id);
    }

    /**
     * 删除多条传感器、传感器数据、生长环境、传感器预警阈值、传感器预警信息
     */
    public void delLinkByIds(List<Integer> ids) throws Exception {
        //删除传感器
        batchDeleteByIds(ids);
        //同时删除关联传感器数据
        deviceSensorDataBiz.delBySIds(ids);
        //同时删除关联生长环境
        growEnvironmentBiz.delBySIds(ids);
        //删除关联传感器预警阈值、传感器预警信息
        warningThresholdBiz.delBySIds(ids);
        warningInfoBiz.delBySIds(ids);
    }


    /**
     * 给相关表设置传感器信息
     */
    public static <T> List<T> setInfoList(String setMN, String getMN, List<DeviceSensor> infoList, List<T> data) {
        if (infoList != null && data != null) {
            int l = infoList.size();
            for (int i = 0, len = data.size(); i < len; i++) {
                try {
                    T obj = data.get(i);
                    Class clazz = obj.getClass();
                    Method m1 = clazz.getMethod(getMN);
                    Object id0 = m1.invoke(obj);
                    if (id0 instanceof Integer) {
                        Integer id = (Integer) id0;
                        for (int j = 0; j < l; j++) {
                            DeviceSensor item = infoList.get(j);
                            if (item != null && item.getId().equals(id)) {
                                Method m2 = clazz.getMethod(setMN, DeviceSensor.class);
                                m2.invoke(obj, item);
                                continue;
                            }
                        }
                    }
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;

    }
}
