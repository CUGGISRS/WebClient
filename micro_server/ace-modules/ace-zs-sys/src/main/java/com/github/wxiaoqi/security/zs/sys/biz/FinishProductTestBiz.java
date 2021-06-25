package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.zs.sys.entity.FinishProductTest;
import com.github.wxiaoqi.security.zs.sys.mapper.FinishProductTestMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成品检验
 */
@Service
public class FinishProductTestBiz extends BaseBiz<FinishProductTestMapper, FinishProductTest> {


    /**
     * 获得某一产品批次号的数据
     */
    public List<FinishProductTest> getByPBN(String traceCode) {
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
    public List<FinishProductTest> getByPBNs(List<String> traceCodes) {
        String propertyName = "productBatchNumber";
        return getByInFiledMayToMap(traceCodes, propertyName, null);
    }

    /**
     * 获得某些产品批次号(检验时间大于等于before，小于等于after)的数据
     */
    public List<FinishProductTest> getByPBNs(List<String> productBatchNumbers, Date before, Date after) {
        String propertyName = "productBatchNumber";
        String fieldName = "testDate";
        Map<String, Object> gtMap = new HashMap<>();
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put(fieldName, after);
        gtMap.put(fieldName, before);
        return getByInFiledMayCondition(productBatchNumbers, propertyName, null, null, null,
                null, null, null,
                null, null, null, gtMap, null, ltMap, null,
                null, null, null, null, null,
                null, null, null, null, null, null,
                null, null);
    }

}
