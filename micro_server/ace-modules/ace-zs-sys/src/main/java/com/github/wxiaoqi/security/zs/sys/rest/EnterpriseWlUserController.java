package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.EnterpriseWlUserBiz;
import com.github.wxiaoqi.security.zs.sys.entity.EnterpriseWlUser;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 企业物联网用户
 */
@RestController
@RequestMapping("EnterpriseWlUser")
public class EnterpriseWlUserController extends BaseController<EnterpriseWlUserBiz, EnterpriseWlUser> {

    /**
     * @api {get} /EnterpriseWlUser/pageByEId 分页展示某一企业的企业物联网用户
     * @apiDescription 无
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} name 名称
     * @apiParam {String} username 用户名
     * @apiParam {String} password 密码
     * @apiParam {String} type 供应类型
     * @apiGroup 企业物联网用户
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<EnterpriseWlUser> pageByEId(@RequestParam Map<String, Object> params) throws Exception {

        Object parentId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(parentId)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("enterpriseId", parentId);
            TableResultResponse<EnterpriseWlUser> data = baseBiz.selectByQuery(null, null, params, null, null, null,
                    andToMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
