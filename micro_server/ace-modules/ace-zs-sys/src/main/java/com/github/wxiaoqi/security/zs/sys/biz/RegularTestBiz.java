package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.zs.sys.entity.RegularTest;
import com.github.wxiaoqi.security.zs.sys.mapper.RegularTestMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定期检验
 */
@Service
public class RegularTestBiz extends BaseBiz<RegularTestMapper, RegularTest> {
    /**
     * 修改某一产品的数据
     */
    public int updateByPId(RegularTest obj, Integer productId) {
        if (productId == null) {
            return 0;
        }
        String fieldName = "productId";
        return updateByFiled(obj, productId, fieldName);
    }

    /**
     * 获得某一产品的数据
     */
    public List<RegularTest> getByPId(Integer pId) {
        if (pId != null) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("productId", pId);
            return getByToMap(andToMap);
        }
        return null;
    }

    /**
     * 通过产品id集合获得数据
     */
    public List<RegularTest> getByPIds(List<Integer> pIds) {
        String propertyName = "productId";
        return getByInFiledMayToMap(pIds, propertyName, null);
    }
}
