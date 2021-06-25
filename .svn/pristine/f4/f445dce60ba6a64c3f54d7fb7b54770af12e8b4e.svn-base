package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.Seller;
import com.github.wxiaoqi.security.zs.sys.mapper.SellerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 销售商
 */
@Service
public class SellerBiz extends BaseBiz<SellerMapper, Seller> {
    @Autowired
    private SaleBiz saleBiz;

    /**
     * 修改某一企业的数据
     */
    public int updateByEId(Seller obj, Integer eId) {
        if (eId == null) {
            return 0;
        }
        String fieldName = "enterpriseId";
        return updateByFiled(obj, eId, fieldName);
    }

    /**
     * 修改某些id的数据
     */
    public int updateByIds(Seller obj, List<Integer> ids) {
        String fieldName = "id";
        return updateByInFiledMayToMap(obj, ids, fieldName, null);
    }

    /**
     * 获得某一企业的数据
     */
    public List<Seller> getByEnterpriseId(Integer enterpriseId) {
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("enterpriseId", enterpriseId);
            return getByToMap(andToMap);
        }
        return null;
    }


    /**
     * 通过id删除销售商(假)
     */
    public Seller delFalse(Integer id) throws Exception {
        Seller old = selectById(id);
        if (old != null) {
            int index = updateSelectiveById(new Seller(id, 1));
            if (index == 0) {
                throw new Exception("删除销售商失败");
            }
        }
        return old;
    }

    /**
     * 通过id集合删除销售商(假)
     */
    public Integer delFalse(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<Seller> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = updateByIds(new Seller(null, 1), ids);
            if (index != sum) {
                throw new Exception("销售商批量删除失败");
            }
            return sum;
        }
        return null;
    }


}
