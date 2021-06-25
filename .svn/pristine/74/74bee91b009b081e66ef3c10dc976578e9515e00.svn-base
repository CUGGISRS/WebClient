package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.BaseWork;
import com.github.wxiaoqi.security.zs.sys.mapper.BaseWorkMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基本作业
 */
@Service
public class BaseWorkBiz extends BaseBiz<BaseWorkMapper, BaseWork> {

    /**
     * 获得某一产品的数据
     */
    public List<BaseWork> getByProductId(Integer productId) {
        if (productId != null) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("productId", productId);
            return getByToMap(andToMap);
        }
        return null;
    }

    /**
     * 获得某些产品的数据
     */
    public List<BaseWork> getByProductIds(List<Integer> productIds) {
        String infield = "productId";
        return getByInFiledMayToMap(productIds, infield, null);
    }


    /**
     * 修改某一产品的数据
     */
    public int updateByPId(BaseWork obj, Integer productId) {
        if (productId == null) {
            return 0;
        }
        String fieldName = "productId";
        return updateByFiled(obj, productId, fieldName);
    }
}
