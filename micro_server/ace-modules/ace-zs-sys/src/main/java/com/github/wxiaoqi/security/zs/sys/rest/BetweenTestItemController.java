package com.github.wxiaoqi.security.zs.sys.rest;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.BetweenTestItemBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 检验_项目
 */
@RestController
@RequestMapping("BetweenTestItem")
public class BetweenTestItemController extends BaseController<BetweenTestItemBiz, BetweenTestItem> {


    @GetMapping("getByTIdAndTType")
    public List<BetweenTestItem> getByTIdAndTType(Integer testId, String testType) {
        return baseBiz.getByTIdAndTType(testId, testType);
    }

    @PostMapping("getByTIdsAndTType")
    public List<BetweenTestItem> getByTIdsAndTType(@RequestBody List<Integer> testIds,
                                                   @RequestParam("testType") String testType) {
        return baseBiz.getByTIdsAndTType(testIds, testType);
    }


    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @PostMapping("deleteByIds")
    public Integer deleteByIds(@RequestBody List<Integer> ids) {
        return baseBiz.batchDeleteByIds(ids);
    }


    /**
     * @api {get} /BetweenTestItem/pageByTIdAndTType 分页展示某一检验类型表某一检验表id的数据（id倒序）
     * @apiDescription 分页展示某一检验类型表某一检验表id的数据（id倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} testId 检验表id（必填,=）
     * @apiParam {String} testType 检验类型（必填,=）
     * @apiParam {Integer} itemId 项目id
     * @apiParam {String} itemName 项目名称
     * @apiParam {String} testResult 检验结果
     * @apiGroup 检验_项目
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByTIdAndTType")
    public TableResultResponse<BetweenTestItem> pageByTIdAndTType(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object testId = params.get("testId");
        Object testType = params.get("testType");
        if (MyObjectUtil.noNullOrEmpty(testId) && MyObjectUtil.noNullOrEmpty(testType)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("testId", testId);
            toMap.put("testType", testType);

            TableResultResponse<BetweenTestItem> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<BetweenTestItem> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
