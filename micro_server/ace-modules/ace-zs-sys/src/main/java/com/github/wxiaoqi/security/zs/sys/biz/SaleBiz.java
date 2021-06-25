package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.zs.sys.entity.Sale;
import com.github.wxiaoqi.security.zs.sys.mapper.SaleMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 销售
 */
@Service
public class SaleBiz extends BaseBiz<SaleMapper, Sale> {

    /**
     * 修改某一销售商的数据
     */
    public int updateBySId(Sale obj, Integer sellerId) {
        if (sellerId == null) {
            return 0;
        }
        String fieldName = "sellerId";
        return updateByFiled(obj, sellerId, fieldName);
    }

    /**
     * 删除某一销售商的数据
     */
    public int delBySId(Integer sellerId) {
        if (sellerId == null) {
            return 0;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("sellerId", sellerId);
        return delByToMap(toMap);
    }

    /**
     * 删除某些销售商的数据
     */
    public int delBySIds(List<Integer> sellerIds) {
        String inField = "sellerId";
        return delByInFiledMayToMap(sellerIds, inField, null);
    }


    /**
     * 获得某一产品批次号的数据
     */
    public List<Sale> getByPBN(String traceCode) {
        if (StringHelper.isNullOrEmpty(traceCode)) {
            return null;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("productBatchNumber", traceCode);
        return getByToMap(toMap);
    }


    /**
     * 通过产品批次号集合获得数据
     */
    public List<Sale> getByPBNs(List<String> traceCodes) {
        String propertyName = "productBatchNumber";
        return getByInFiledMayToMap(traceCodes, propertyName, null);
    }


    /**
     * 获得某些产品批次号(发货时间大于等于before，小于等于after)的数据
     */
    public List<Sale> getByPBNs(List<String> productBatchNumbers, Date before, Date after) {
        String propertyName = "productBatchNumber";
        String fieldName = "sendDate";
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put(fieldName, before);
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put(fieldName, after);
        return getByInFiledMayCondition(productBatchNumbers, propertyName, null, null, null,
                null, null, null,
                null, null, null, gtMap, null, ltMap, null,
                null, null, null, null, null,
                null, null, null, null, null, null,
                null, null);
    }

    /**
     * 通过订单编号获得一条数据
     *
     * @param orderNumber
     * @return
     */
    public Sale getOneByOrderNumber(String orderNumber) throws Exception {
        if (MyObjectUtil.noNullOrEmpty(orderNumber)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("orderNumber", orderNumber);
            List<Sale> sales = getByToMap(andToMap);
            int sum = MyObjectUtil.iterableCount(sales);
            if (sum == 1) {
                return sales.get(0);
            } else if (sum > 1) {
                throw new Exception("销售中订单编号必须唯一");
            }
        }
        return null;
    }

}
