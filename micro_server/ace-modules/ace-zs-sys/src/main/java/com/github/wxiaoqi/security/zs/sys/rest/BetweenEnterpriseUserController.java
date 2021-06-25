package com.github.wxiaoqi.security.zs.sys.rest;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.BetweenEnterpriseUserBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenEnterpriseUser;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业_用户
 */
@RestController
@RequestMapping("BetweenEnterpriseUser")
public class BetweenEnterpriseUserController extends BaseController<BetweenEnterpriseUserBiz,
        BetweenEnterpriseUser> {

    @DeleteMapping("delByUserId")
    public int delByUserId(Integer userId) {
        return baseBiz.delByUserId(userId);
    }

    @PostMapping("delByUserIds")
    public int delByUserIds(@RequestBody List<Integer> userIds) {
        return baseBiz.delByUserIds(userIds);
    }

    /**
     * @api {get} /BetweenEnterpriseUser/pageByEId 分页展示某一企业id的数据（id倒序）
     * @apiDescription 分页展示某一企业id的数据（id倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} enterpriseId 所属企业id（必填,=）
     * @apiParam {Integer} userId 用户id
     * @apiGroup 企业_用户
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<BetweenEnterpriseUser> pageByEId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object enterpriseId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("enterpriseId", enterpriseId);

            TableResultResponse<BetweenEnterpriseUser> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<BetweenEnterpriseUser> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

}
