package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.zs.sys.biz.BetweenTestItemBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ItemInfoBiz;
import com.github.wxiaoqi.security.zs.sys.entity.ItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 项目
 */
@RestController
@RequestMapping("ItemInfo")
public class ItemInfoController extends BaseController<ItemInfoBiz, ItemInfo> {
    @Autowired
    private BetweenTestItemBiz betweenTestItemBiz;

    @PostMapping("selectByIds")
    public List<ItemInfo> selectByIds(@RequestBody List<Integer> ids) {
        return baseBiz.batchSelectByIds(ids);
    }


    /**
     * @api {delete} /ItemInfo/{id}  删除一条项目、检验_项目
     * @apiDescription 无
     * @apiGroup 项目
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<ItemInfo> remove(@PathVariable int id) throws Exception {
        ItemInfo old = baseBiz.delLink(id);
        if (old != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /ItemInfo/batchDelete 批量删除项目、检验_项目
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 项目
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        Integer sum = baseBiz.delLink(ids);
        if (sum != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, sum);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {get} /ItemInfo/pageByTId 分页展示某一检验类型的某一检验表id的项目（id倒序）
     * @apiDescription 分页展示某一类型的某一检验表id的项目（id倒序）
     * @apiParam {Integer} testId 检验表id（必填，=）
     * @apiParam {String} testType 检验类型（必填，=）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {BigDecimal} type 类型
     * @apiParam {String} description 描述
     * @apiGroup 项目
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByTId")
    public TableResultResponse<ItemInfo> pageByTId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object testId = params.get("testId");
        Object testType = params.get("testType");
        List<Integer> ids = betweenTestItemBiz.getItemIdsByTIdAndTType(testId, testType);
        if (ids != null) {
            Map<String, Iterable> inMap = new HashMap<>();
            inMap.put("id", ids);
            TableResultResponse<ItemInfo> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    null, null, null, null, null, null, inMap, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<ItemInfo> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
