package com.github.wxiaoqi.security.jd.sys.feign.Service;

import com.alibaba.fastjson.JSONArray;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.jd.sys.feign.IProduct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 产品服务类
 */
@Service
public class IProductService {
    @Autowired
    private IProduct iProduct;

    /**
     * 获得某一企业(某一基地类型)的产品批次号集合
     */
    public List<String> getPBNByEnterpriseId(Object enterpriseId, Object baseType) {
        Integer eId = null;
        String bt = null;
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            eId = Integer.valueOf(enterpriseId.toString());
        }
        if (baseType != null) {
            bt = baseType.toString();
        }
        return iProduct.getPBNByEnterpriseId(eId, bt);
    }

    /**
     * 将企业分区域后存入map
     */
    public Map<String, List<Integer>> enterpriseArea(List<Integer> eIds) {
        if (eIds == null) {
            return null;
        }
        return iProduct.enterpriseArea(eIds);
    }


    /**
     * 将产品详情分水产、种植模块后存入map
     */
    public Map<String, List<Integer>> productDetailModule(List<Integer> pdIds) {
        if (pdIds == null) {
            return null;
        }
        return iProduct.productDetailModule(pdIds);
    }


    /**
     * 通过产品详情id获得所属系统模块
     */
    public Integer getSysModuleByPDId(Integer pdId) {
        return iProduct.getSysModuleByPDId(pdId);
    }

    /**
     * 获得这些加工产品追溯码的预警相关信息
     */
    public JSONArray pbnPartMap(List<String> pbnList) {
        if (pbnList == null) {
            return null;
        }
        return iProduct.pbnPartMap(pbnList);
    }
}
