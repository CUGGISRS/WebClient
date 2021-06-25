package com.github.wxiaoqi.security.jd.sys.rest;

import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.jd.sys.biz.BetweenDeptUserBiz;
import com.github.wxiaoqi.security.jd.sys.entity.BetweenDeptUser;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 部门_用户
 */
@RestController
@RequestMapping("BetweenDeptUser")
public class BetweenDeptUserController extends BaseController<BetweenDeptUserBiz, BetweenDeptUser> {
    @DeleteMapping("delByUserId")
    public int delByUserId(Integer userId) {
        return baseBiz.delByUserId(userId);
    }

    @PostMapping("delByUserIds")
    public int delByUserIds(@RequestBody List<Integer> userIds) {
        return baseBiz.delByUserIds(userIds);
    }

    /**
     * @api {get} /BetweenDeptUser/pageByDId 分页展示某一部门id的数据（id倒序）
     * @apiDescription 分页展示某一部门id的数据（id倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} deptId 所属部门id（必填,=）
     * @apiParam {Integer} userId 用户id
     * @apiGroup 部门_用户
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByDId")
    public TableResultResponse<BetweenDeptUser> pageByDId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object deptId = params.get("deptId");
        if (MyObjectUtil.noNullOrEmpty(deptId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("deptId", deptId);

            TableResultResponse<BetweenDeptUser> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<BetweenDeptUser> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
