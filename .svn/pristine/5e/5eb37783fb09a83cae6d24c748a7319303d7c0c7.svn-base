package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.DeviceSensorBiz;
import com.github.wxiaoqi.security.zs.sys.biz.GrowEnvironmentBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.entity.DeviceSensor;
import com.github.wxiaoqi.security.zs.sys.entity.GrowEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 生长环境
 */
@RestController
@RequestMapping("GrowEnvironment")
public class GrowEnvironmentController extends BaseController<GrowEnvironmentBiz, GrowEnvironment> {
    @Autowired
    private ProductBiz productBiz;

    @Autowired
    private DeviceSensorBiz deviceSensorBiz;


    /**
     * @api {get} /GrowEnvironment/pageByEId 分页展示某一企业(某一基地类型)下产品或某一产品的生长环境(id倒序)、传感器
     * @apiDescription 分页展示某一企业(某一基地类型)下产品或某一产品的生长环境(id倒序)、传感器
     * @apiParam {Integer} enterpriseId 企业id（=）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 生长环境名称
     * @apiParam {Integer} productId 产品id（=）
     * @apiParam {String} productName 产品名称
     * @apiParam {Integer} deviceSensorId 设备传感器id(=)
     * @apiGroup 生长环境
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<GrowEnvironment> pageByEId(@RequestParam Map<String, Object> params) throws Exception {

        Map<String, Iterable> inMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();
        String orderBy = "id desc";
        Object pId = params.get("productId");
        toMap.put("deviceId", params.get("deviceId"));
        if (MyObjectUtil.noNullOrEmpty(pId)) {
            toMap.put("productId", pId);
        } else {
            List<Integer> pIds = productBiz.getByEIdAndBaseType(params.get("enterpriseId"),
                    params.get("baseType"));
            if (pIds == null) {
                return new TableResultResponse<>(0, new ArrayList<>());
            }
            inMap.put("productId", pIds);
        }

        TableResultResponse<GrowEnvironment> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, null, null, null, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<GrowEnvironment> list = data.getData().getRows();
        if (list != null) {
            List<Integer> dsIds = baseBiz.getDSIdsByList(list);
            List<DeviceSensor> dsList = deviceSensorBiz.batchSelectByIds(dsIds);
            //设置传感器
            DeviceSensorBiz.setInfoList("setDeviceSensor", "getDeviceSensorId", dsList, list);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
