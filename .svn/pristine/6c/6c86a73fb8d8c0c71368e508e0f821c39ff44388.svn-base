package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.DeviceGroupBiz;
import com.github.wxiaoqi.security.zs.sys.biz.DeviceSensorBiz;
import com.github.wxiaoqi.security.zs.sys.biz.DeviceSensorDataBiz;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceGroup;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceSensor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物联网设备分组
 */
@RestController
@RequestMapping("DeviceGroup")
public class DeviceGroupController extends BaseController<DeviceGroupBiz, DeviceGroup> {
    @Autowired
    private DeviceSensorBiz deviceSensorBiz;

    /**
     * @api {delete} /DeviceGroup/{id} 删除一条分组、传感器、传感器数据、生长环境、传感器预警阈值、传感器预警信息
     * @apiDescription 无
     * @apiParam {Integer} id 编号
     * @apiGroup 物联网设备分组
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<DeviceGroup> remove(@PathVariable int id) throws Exception {
        DeviceGroup old = baseBiz.selectById(id);
        if (old == null) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        int changed = baseBiz.deleteById(id);
        if (changed == 0) {
            throw new Exception("分组删除失败");
        }

        List<DeviceSensor> dsList = deviceSensorBiz.getByGId(id);
        List<Integer> sIds = MyObjectUtil.getIdsByList(dsList);
        //删除传感器、传感器数据、生长环境、传感器预警阈值、传感器预警信息
        deviceSensorBiz.delLinkByIds(sIds);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
    }

    /**
     * @api {get} /DeviceGroup/pageByCId 分页展示某一创建者(即某一企业)的物联网设备分组(名称正序)
     * @apiDescription 无
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 组名
     * @apiParam {Integer} creatorId 创建者ID(即企业id)(非空，=)
     * @apiParam {Date} createTime 创建时间
     * @apiGroup 物联网设备分组
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByCId")
    public TableResultResponse<DeviceGroup> pageByCId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "name asc";
        Object creatorId = params.get("creatorId");
        if (MyObjectUtil.noNullOrEmpty(creatorId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("creatorId", creatorId);
            TableResultResponse<DeviceGroup> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<DeviceGroup> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
