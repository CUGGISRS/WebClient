package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.Supplier;
import com.github.wxiaoqi.security.zs.sys.mapper.SupplierMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 供应商
 */
@Service
public class SupplierBiz extends BaseBiz<SupplierMapper, Supplier> {

    /**
     * 修改某些id的数据
     */
    public int updateByIds(Supplier obj, List<Integer> ids) {
        String fieldName = "id";
        return updateByInFiledMayToMap(obj, ids, fieldName, null);
    }

    /**
     * 通过id删除供应商(假)
     */
    public Supplier delFalse(Integer id) throws Exception {
        Supplier old = selectById(id);
        if (old != null) {
            int index = updateSelectiveById(new Supplier(id, 1));
            if (index == 0) {
                throw new Exception("删除供应商失败");
            }
        }
        return old;
    }

    /**
     * 通过id集合删除供应商(假)
     */
    public Integer delFalse(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<Supplier> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = updateByIds(new Supplier(null, 1), ids);
            if (index != sum) {
                throw new Exception("供应商批量删除失败");
            }
            return sum;
        }
        return null;
    }

}
