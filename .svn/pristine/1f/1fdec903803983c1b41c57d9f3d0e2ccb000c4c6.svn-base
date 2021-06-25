package com.github.wxiaoqi.security.jd.sys.rest;

import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.jd.sys.biz.DeptBiz;
import com.github.wxiaoqi.security.jd.sys.entity.BetweenDeptUser;
import com.github.wxiaoqi.security.jd.sys.entity.Dept;
import com.github.wxiaoqi.security.zs.sys.biz.BetweenTestItemBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监督部门
 */
@RestController
@RequestMapping("Dept")
public class DeptController extends BaseController<DeptBiz, Dept> {

    /**
     * @api {get} /Dept/pageByCondition 分页展示（id倒序）
     * @apiDescription 分页展示（id倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {String} address 地址
     * @apiParam {String} type 类型
     * @apiParam {String} phone 联系电话
     * @apiParam {String} postcode 邮编
     * @apiParam {Integer} parentId 父级id
     * @apiGroup 监督部门
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByCondition")
    public TableResultResponse<Dept> pageByCondition(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        TableResultResponse<Dept> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<Dept> list = data.getData().getRows();
        if (list != null) {
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }


    /**
     * @api {delete} /Dept/{id}  删除一条、部门-用户、用户(带文件)
     * @apiDescription 无
     * @apiGroup 监督部门
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Dept> remove(@PathVariable int id) throws Exception {
        Dept dept = baseBiz.delLinkOne(id);
        if (dept != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, dept);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /Dept/batchDelete 批量删除、部门-用户、用户(带文件)
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 监督部门
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        Integer sum = baseBiz.delLinkData(ids);
        if (sum != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, sum);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }
}
