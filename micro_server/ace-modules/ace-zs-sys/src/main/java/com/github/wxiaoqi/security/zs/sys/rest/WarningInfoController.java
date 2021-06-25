package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.WarningInfoBiz;
import com.github.wxiaoqi.security.zs.sys.entity.WarningInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.*;

/**
 * 物联网传感器报警信息
 */
@RestController
@RequestMapping("WarningInfo")
public class WarningInfoController extends BaseController<WarningInfoBiz, WarningInfo> {


    /**
     * @api {get} /WarningInfo/pageByEId 分页展示某一企业的物联网传感器报警信息(报警时间倒序)
     * @apiDescription 无
     * @apiParam {Date} warningDateS 报警时间起点(>=)
     * @apiParam {Date} warningDateE 报警时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} sensorId 传感器id(=)
     * @apiParam {Integer} enterpriseId 企业id(非空，=)
     * @apiParam {Date} warningDate 报警时间
     * @apiParam {Integer} warningLevel 报警级别(=)
     * @apiParam {String} warningInfo 报警信息
     * @apiParam {Integer} status 状态（0未读,1已读）(=)
     * @apiParam {BigDecimal} warningData 报警数据
     * @apiGroup 物联网传感器报警信息
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<WarningInfo> pageByEId(@RequestParam Map<String, Object> params,
                                                      Date warningDateS, Date warningDateE) throws Exception {
        String orderBy = "warning_date desc";
        Object enterpriseId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("enterpriseId", enterpriseId);
            toMap.put("sensorId", params.get("sensorId"));
            toMap.put("warningLevel", params.get("warningLevel"));
            toMap.put("status", params.get("status"));
            Map<String, Object> gtMap = new HashMap<>();
            Map<String, Object> ltMap = new HashMap<>();
            gtMap.put("warningDate", warningDateS);
            ltMap.put("warningDate", warningDateE);
            TableResultResponse<WarningInfo> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, gtMap, null, ltMap, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<WarningInfo> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
