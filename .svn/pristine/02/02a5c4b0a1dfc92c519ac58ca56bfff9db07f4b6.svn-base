package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.EnterpriseWLManagerBiz;
import com.github.wxiaoqi.security.zs.sys.entity.EnterpriseWLManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业物联网管理员
 */
@RestController
@RequestMapping("EnterpriseWLManager")
public class EnterpriseWLManagerController extends BaseController<EnterpriseWLManagerBiz, EnterpriseWLManager> {


    /**
     * @api {get} /EnterpriseWLManager/pageByEId 分页展示某一企业的企业物联网管理员(创建时间倒序)
     * @apiDescription 无
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {String} sex 性别(=)
     * @apiParam {String} phone 联系电话
     * @apiParam {String} mailbox 邮箱
     * @apiParam {Integer} enterpriseId 企业id（非空，=）
     * @apiParam {Date} createTime 创建时间
     * @apiGroup 企业物联网管理员
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<EnterpriseWLManager> pageByEId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "create_time desc";
        Object enterpriseId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("enterpriseId", enterpriseId);
            toMap.put("sex", params.get("sex"));
            TableResultResponse<EnterpriseWLManager> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<EnterpriseWLManager> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
