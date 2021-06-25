package com.github.wxiaoqi.security.zs.sys.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.DeviceSensorBiz;
import com.github.wxiaoqi.security.zs.sys.biz.DeviceSensorDataBiz;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceSensor;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceSensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 物联网设备传感器数据
 */
@RestController
@RequestMapping("DeviceSensorData")
public class DeviceSensorDataController extends BaseController<DeviceSensorDataBiz, DeviceSensorData> {
    @Autowired
    private DeviceSensorBiz deviceSensorBiz;

    /**
     * @api {get} /DeviceSensorData/pageBySId 分页展示某一传感器的数据(记录时间倒序)
     * @apiDescription 无
     * @apiParam {Date} recordTimeS 记录时间起点(>=)
     * @apiParam {Date} recordTimeE 记录时间终点(<=)
     * @apiParam {Integer} interval 记录时间间隔(单位分钟)
     * @apiParam {Integer} sensorId 传感器id(非空，=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiGroup 物联网设备传感器数据
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageBySId")
    public TableResultResponse<DeviceSensorData> pageBySId(Integer sensorId, Date recordTimeS,
                                                           Date recordTimeE, Integer interval,
                                                           Integer page, Integer limit) throws Exception {
        String orderBy = "record_time desc";
        //如何时间间隔存在，开始时间不存在则将最小记录时间赋值给开始时间
        if (interval != null && recordTimeS == null) {
            recordTimeS = baseBiz.getMinTimeBySId(sensorId);
        }
        int total = baseBiz.getTotalBySId(sensorId, recordTimeS, recordTimeE, interval);
        List<DeviceSensorData> list = baseBiz.pageBySId(sensorId, recordTimeS, recordTimeE, interval, page, limit, orderBy);
        return new TableResultResponse<>(total, list);
    }


    /**
     * @api {get} /DeviceSensorData/pageByGId 分页展示某一分组下传感器或某一传感器的数据(记录时间倒序)
     * @apiDescription 无
     * @apiParam {Integer} groupId 分组id(非空，=)
     * @apiParam {Date} recordTimeS 记录时间起点(>=)
     * @apiParam {Date} recordTimeE 记录时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} sensorId 传感器id(=)
     * @apiParam {Date} recordTime 记录时间
     * @apiParam {BigDecimal} recordData 记录数据
     * @apiParam {Date} syncTime 同步时间
     * @apiGroup 物联网设备传感器数据
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByGId")
    public TableResultResponse<JSONObject> pageByGId(@RequestParam Map<String, Object> params,
                                                     Date recordTimeS, Date recordTimeE) throws Exception {
        String orderBy = "record_time desc";
        Object groupId = params.get("groupId");
        Object sensorId = params.get("sensorId");
        Map<String, Iterable> inMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();

        if (MyObjectUtil.noNullOrEmpty(sensorId)) {
            toMap.put("sensorId", sensorId);
        } else {
            List<DeviceSensor> dsList = deviceSensorBiz.getByGId(groupId);
            List<Integer> sIds = MyObjectUtil.getIdsByList(dsList);
            if (sIds == null) {
                return new TableResultResponse<>(0, new ArrayList<>());
            } else {
                inMap.put("sensorId", sIds);
            }
        }
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("recordTime", recordTimeS);
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("recordTime", recordTimeE);
        TableResultResponse<DeviceSensorData> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, gtMap, null, ltMap, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<DeviceSensorData> list = data.getData().getRows();
        long total = data.getData().getTotal();

        List<JSONObject> objects = baseBiz.getJsonByList(list);
        return new TableResultResponse<>(total, objects);
    }

    /**
     * @api {get} /DeviceSensorData/getByGId 获得某一分组下传感器、传感器数据(记录时间倒序)
     * @apiDescription 无
     * @apiParam {Integer} groupId 分组id(非空，=)
     * @apiParam {Integer} interval 记录时间间隔(单位分钟)
     * @apiParam {Date} recordTimeS 记录时间起点(>=)
     * @apiParam {Date} recordTimeE 记录时间终点(<=)
     * @apiGroup 物联网设备传感器数据
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("getByGId")
    public ObjectRestResponse<JSONObject> getByGId(Integer groupId, Integer interval,
                                                   Date recordTimeS, Date recordTimeE) throws Exception {

        JSONObject obj = baseBiz.getByGId(groupId, interval, recordTimeS, recordTimeE);
        if (obj == null) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }


}
