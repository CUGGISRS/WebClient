package com.github.wxiaoqi.security.zs.sys.rest;

import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.biz.SaleBiz;
import com.github.wxiaoqi.security.zs.sys.entity.Sale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 销售
 */
@RestController
@RequestMapping("Sale")
public class SaleController extends BaseController<SaleBiz, Sale> {

    @Autowired
    private ProductBiz productBiz;

    @Override
    @ResponseBody
    public ObjectRestResponse<Sale> add(@RequestBody Sale sale) throws Exception {
        String orderNumber = sale.getOrderNumber();
        Sale old = baseBiz.getOneByOrderNumber(orderNumber);
        if (old != null) {
            throw new Exception("该订单编号已存在");
        }
        return super.add(sale);
    }

    @Override
    @ResponseBody
    public ObjectRestResponse<Sale> update(@RequestBody Sale sale) throws Exception {
        Integer id = sale.getId();
        if (id == null) {
            throw new Exception("id不能为null");
        }
        String orderNumber = sale.getOrderNumber();
        Sale old = baseBiz.getOneByOrderNumber(orderNumber);
        if (old != null && !id.equals(old.getId())) {
            throw new Exception("该订单编号已存在");
        }
        return super.update(sale);
    }

    /**
     * @api {get} /Sale/pageByEId 分页展示某一企业下产品批次的销售（发货时间倒序）
     * @apiDescription 无
     * @apiParam {Integer} enterpriseId 企业id（必填，=）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {Date} sendDateS 发货时间起点(>=)
     * @apiParam {Date} sendDateE 发货时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} orderNumber 订单编号(唯一)
     * @apiParam {String} productBatchNumber 产品批次号(=)
     * @apiParam {Integer} sellerId 销售商id（=）
     * @apiParam {String} sellerName 销售商名称
     * @apiParam {Date} sendDate 发货时间
     * @apiParam {String} sender 发货人
     * @apiParam {String} receiver 收货人
     * @apiParam {String} startPlace 始发地
     * @apiParam {String} endPlace 目的地
     * @apiParam {BigDecimal} saleAmount 销售数量
     * @apiParam {String} saleAmountUnit 销售数量单位
     * @apiGroup 销售
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<Sale> pageByEId(@RequestParam Map<String, Object> params,
                                               Date sendDateS, Date sendDateE) throws Exception {
        String orderBy = "send_date desc";
        Map<String, Iterable> inMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();
        List<String> productBatchNumbers = productBiz.getPBNByEnterpriseId(params.get("enterpriseId"),
                params.get("baseType"));
        if (productBatchNumbers != null) {
            inMap.put("productBatchNumber", productBatchNumbers);
        } else {
            return new TableResultResponse<>(0, new ArrayList<>());
        }
        toMap.put("sellerId", params.get("sellerId"));
        toMap.put("productBatchNumber", params.get("productBatchNumber"));
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("sendDate", sendDateE);
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("sendDate", sendDateS);
        TableResultResponse<Sale> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, gtMap, null, ltMap, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<Sale> list = data.getData().getRows();
        if (list != null) {
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

}
