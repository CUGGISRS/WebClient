package com.github.wxiaoqi.security.jd.sys.rest;

import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.jd.sys.biz.DeptTestBiz;
import com.github.wxiaoqi.security.jd.sys.biz.WarningThresholdBiz;
import com.github.wxiaoqi.security.jd.sys.entity.WarningThreshold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 预警阈值
 */
@RestController
@RequestMapping("WarningThreshold")
public class WarningThresholdController extends BaseController<WarningThresholdBiz, WarningThreshold> {
    @Autowired
    private DeptTestBiz deptTestBiz;

    @Override
    public ObjectRestResponse<WarningThreshold> update(@RequestBody WarningThreshold warningThreshold) throws Exception {
        Integer id = warningThreshold.getId();
        WarningThreshold old = baseBiz.selectById(id);
        if (old == null) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        int index = baseBiz.updateSelectiveById(warningThreshold);
        if (index == 0) {
            throw new Exception("预警阈值修改失败");
        }

        //是否需要添加预警信息
        deptTestBiz.isAddWarning();
        /*deptTestBiz.isAddWarningModule(CommonConstants.SYS_MODULE_SC);
        deptTestBiz.isAddWarningModule(CommonConstants.SYS_MODULE_ZZ);*/

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, warningThreshold);
    }

    /**
     * @api {get} /WarningThreshold/pageByCondition 分页展示（id倒序）
     * @apiDescription 分页展示（id倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {BigDecimal} threshold 预警阈值
     * @apiGroup 预警阈值
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByCondition")
    public TableResultResponse<WarningThreshold> pageByCondition(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        TableResultResponse<WarningThreshold> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<WarningThreshold> list = data.getData().getRows();
        if (list != null) {
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
