package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.DeviceInfoBiz;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物联网设备(设备接口不能用，设备信息要使用别人提供的接口！)
 */
@RestController
@RequestMapping("DeviceInfo")
public class DeviceInfoController extends BaseController<DeviceInfoBiz, DeviceInfo> {

    /**
     * @api {get} /DeviceInfo/pageByCId (设备接口不能用，设备信息要使用别人提供的接口！)分页展示某一创建者的物联网设备(创建时间倒序)
     * @apiDescription (设备接口不能用 ， 设备信息要使用别人提供的接口 ！)分页展示某一创建者的物联网设备(创建时间倒序)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} facId 设备ID号
     * @apiParam {Date} createTime 创建时间
     * @apiParam {String} remark 备注
     * @apiParam {String} facName 设备名称
     * @apiParam {Integer} facType 设备类型
     * @apiParam {String} eleNum 要素配置索引
     * @apiParam {String} eleName 要素配置名称
     * @apiParam {String} relayNum 继电器配置索引
     * @apiParam {String} relayName 继电器配置名称
     * @apiParam {Float} longitude 经度
     * @apiParam {Float} latitude 纬度
     * @apiParam {Integer} readInterval 读取实时数据间隔，单位是分钟
     * @apiParam {Integer} address 硬件地址
     * @apiParam {Boolean} photo 是否有串口摄像头
     * @apiParam {Integer} creatorId 创建者ID(非空，=)
     * @apiParam {Boolean} relayExtend 是否继电器扩展
     * @apiParam {Integer} relayExtendCount 继电器扩展的数量，不包含默认的32路
     * @apiParam {String} relayExtendNum 继电器扩展序号
     * @apiParam {String} relayExtendName 继电器扩展名称
     * @apiParam {String} coverUrl 设备封面
     * @apiParam {String} pestImei 虫情IMEI
     * @apiGroup 物联网设备
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByCId")
    public TableResultResponse<DeviceInfo> pageByCId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "create_time desc";
        Object creatorId = params.get("creatorId");
        if (MyObjectUtil.noNullOrEmpty(creatorId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("creatorId", creatorId);
            TableResultResponse<DeviceInfo> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<DeviceInfo> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
