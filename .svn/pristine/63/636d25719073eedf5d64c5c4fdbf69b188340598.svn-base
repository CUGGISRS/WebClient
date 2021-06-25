package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.*;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceSensor;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceSensorData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 物联网设备传感器
 */
@RestController
@RequestMapping("DeviceSensor")
public class DeviceSensorController extends BaseController<DeviceSensorBiz, DeviceSensor> {
    @Autowired
    private DeviceGroupBiz deviceGroupBiz;


    @Override
    public ObjectRestResponse<DeviceSensor> add(@RequestBody DeviceSensor deviceSensor) throws Exception {

        int index = baseBiz.insertSelective(deviceSensor);
        if (index == 0) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, deviceSensor);
    }


    /**
     * @api {get} /DeviceSensor/pageByGId 分页展示某一分组的物联网设备传感器(id倒序)
     * @apiDescription 分页展示某一分组的物联网设备传感器(id倒序)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} number 传感器编号
     * @apiParam {String} name 传感器名称
     * @apiParam {Integer} type 传感器类型(1要素，2继电器)(=)
     * @apiParam {Integer} facId 设备ID号
     * @apiParam {String} facPass 设备通道
     * @apiParam {Integer} groupId 分组id(非空，=)
     * @apiParam {String} unit 传感器单位
     * @apiParam {String} preC 传感器分辨率
     * @apiParam {BigDecimal} longitude 经度
     * @apiParam {BigDecimal} latitude 纬度
     * @apiParam {Integer} enterpriseWlUserId 企业物联网用户表id
     * @apiParam {Date} lastRecordTime 最后一次同步时的记录时间
     * @apiParam {BigDecimal} lastRecordData 最后一次同步时的记录数据
     * @apiGroup 物联网设备传感器
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByGId")
    public TableResultResponse<DeviceSensor> pageByGId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object groupId = params.get("groupId");
        if (MyObjectUtil.noNullOrEmpty(groupId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("groupId", groupId);
            toMap.put("type", params.get("type"));
            TableResultResponse<DeviceSensor> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<DeviceSensor> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * @api {get} /DeviceSensor/pageByCId 分页展示某一创建者(即某一企业)的物联网设备传感器(id倒序)
     * @apiDescription 无
     * @apiParam {Integer} creatorId 创建者ID(即企业id)(非空，=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} number 传感器编号
     * @apiParam {String} name 传感器名称
     * @apiParam {Integer} type 传感器类型(1要素，2继电器)(=)
     * @apiParam {Integer} facId 设备ID号
     * @apiParam {String} facPass 设备通道
     * @apiParam {Integer} groupId 分组id(非空，=)
     * @apiParam {String} unit 传感器单位
     * @apiParam {String} preC 传感器分辨率
     * @apiParam {BigDecimal} longitude 经度
     * @apiParam {BigDecimal} latitude 纬度
     * @apiParam {Integer} enterpriseWlUserId 企业物联网用户表id
     * @apiParam {Date} lastRecordTime 最后一次同步时的记录时间
     * @apiParam {BigDecimal} lastRecordData 最后一次同步时的记录数据
     * @apiGroup 物联网设备传感器
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByCId")
    public TableResultResponse<DeviceSensor> pageByCId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object creatorId = params.get("creatorId");
        if (MyObjectUtil.noNullOrEmpty(creatorId)) {
            List<Integer> gIds = MyObjectUtil.getIdsByList(deviceGroupBiz.getByCId(creatorId));
            if (gIds != null) {
                Map<String, Iterable> inMap = new HashMap<>();
                inMap.put("groupId", gIds);
                Map<String, Object> toMap = new HashMap<>();
                toMap.put("type", params.get("type"));
                TableResultResponse<DeviceSensor> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                        toMap, null, null, null, null, null, inMap, null,
                        null, null, null, null, null, null,
                        null, null, null, null, null, null);
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }


    /**
     * @api {get} /DeviceSensor/pageByFId 分页展示某一设备的物联网设备传感器(id倒序)
     * @apiDescription 分页展示某一设备的物联网设备传感器(id倒序)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} number 传感器编号
     * @apiParam {String} name 传感器名称
     * @apiParam {Integer} type 传感器类型(1要素，2继电器)(=)
     * @apiParam {Integer} facId 设备ID号(非空，=)
     * @apiParam {String} facPass 设备通道
     * @apiParam {Integer} groupId 分组id
     * @apiParam {String} unit 传感器单位
     * @apiParam {String} preC 传感器分辨率
     * @apiParam {BigDecimal} longitude 经度
     * @apiParam {BigDecimal} latitude 纬度
     * @apiParam {Integer} enterpriseWlUserId 企业物联网用户表id
     * @apiParam {Date} lastRecordTime 最后一次同步时的记录时间
     * @apiParam {BigDecimal} lastRecordData 最后一次同步时的记录数据
     * @apiGroup 物联网设备传感器
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByFId")
    public TableResultResponse<DeviceSensor> pageByFId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object fId = params.get("facId");
        if (MyObjectUtil.noNullOrEmpty(fId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("type", params.get("type"));
            toMap.put("facId", fId);
            TableResultResponse<DeviceSensor> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

    /**
     * @api {delete} /DeviceSensor/{id}  删除一条传感器、传感器数据、生长环境、传感器预警阈值、传感器预警信息
     * @apiDescription 无
     * @apiGroup 物联网设备传感器
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<DeviceSensor> remove(@PathVariable int id) throws Exception {
        DeviceSensor old = baseBiz.selectById(id);
        if (old == null) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        //删除一条传感器、传感器数据、生长环境、传感器预警阈值、传感器预警信息
        baseBiz.delLinkById(id);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
    }
}
