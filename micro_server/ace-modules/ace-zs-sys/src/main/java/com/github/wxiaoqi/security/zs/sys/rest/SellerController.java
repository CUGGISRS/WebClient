package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.SellerBiz;
import com.github.wxiaoqi.security.zs.sys.entity.Seller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 销售商
 */
@RestController
@RequestMapping("Seller")
public class SellerController extends BaseController<SellerBiz, Seller> {

    /**
     * @api {delete} /Seller/{id}  删除一条(假)
     * @apiDescription 无
     * @apiGroup 销售商
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Seller> remove(@PathVariable int id) throws Exception {
        Seller old = baseBiz.delFalse(id);
        if (old != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /Seller/batchDelete 批量删除(假)
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 销售商
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        Integer sum = baseBiz.delFalse(ids);
        if (sum != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, sum);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {get} /Seller/pageByEId 分页展示某一企业的信息（id倒序）
     * @apiDescription 无
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {String} address 地址
     * @apiParam {String} phone 联系电话
     * @apiParam {String} mailbox 邮箱
     * @apiParam {String} contactPerson 联系人
     * @apiParam {String} saleLicenseNumber 销售许可证号
     * @apiParam {String} organizationCode 组织机构代码
     * @apiParam {Integer} isOpen 是否向公众公开（1是，0否）
     * @apiParam {String} nature 性质
     * @apiParam {Integer} enterpriseId 企业id（必填，=）
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {Integer} isDel 是否删除（0否，1是）(默认0,=)
     * @apiGroup 销售商
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<Seller> pageByEId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object parentId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(parentId)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("enterpriseId", parentId);

            Object isDel = params.get("isDel");
            andToMap.put("isDel", isDel == null ? 0 : isDel);
            TableResultResponse<Seller> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    andToMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<Seller> list = data.getData().getRows();
            if (list != null) {
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
