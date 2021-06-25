package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.WarningThresholdBiz;
import com.github.wxiaoqi.security.zs.sys.entity.WarningThreshold;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 物联网传感器报警阈值
 */
@RestController
@RequestMapping("WarningThreshold")
public class WarningThresholdController extends BaseController<WarningThresholdBiz, WarningThreshold> {


    /**
     * @api {get} /WarningThreshold/pageBySId 分页展示某一传感器的物联网传感器报警阈值(级别正序)
     * @apiDescription 无
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} sensorId 传感器id(非空，=)
     * @apiParam {Integer} level 级别(=)
     * @apiParam {BigDecimal} maxValue 最大值
     * @apiParam {BigDecimal} minValue 最小值
     * @apiParam {String} warningInfo 报警信息
     * @apiParam {Integer} isSend 是否发送短信(1是，0否)（=）
     * @apiParam {Integer} enterpriseWLManagerId 企业物联网管理员id
     * @apiParam {Integer} sendInterval 发送短信间隔（分钟,默认15）
     * @apiParam {Date} lastRecordTime 最后发送短信时间
     * @apiGroup 物联网传感器报警阈值
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageBySId")
    public TableResultResponse<WarningThreshold> pageBySId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "level";
        Object sensorId = params.get("sensorId");
        if (MyObjectUtil.noNullOrEmpty(sensorId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("sensorId", sensorId);
            toMap.put("isSend", params.get("isSend"));
            toMap.put("level", params.get("level"));
            TableResultResponse<WarningThreshold> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<WarningThreshold> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
