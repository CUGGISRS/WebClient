package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import com.github.wxiaoqi.security.zs.sys.mapper.BetweenTestItemMapper;
import com.github.wxiaoqi.security.zs.sys.util.TestItemUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 检验-项目
 */
@Service
public class BetweenTestItemBiz extends BaseBiz<BetweenTestItemMapper, BetweenTestItem> {

    /**
     * 修改某一项目的数据
     */
    public int updateByIId(BetweenTestItem obj, Integer itemId) {
        if (itemId == null) {
            return 0;
        }
        String fieldName = "itemId";
        return updateByFiled(obj, itemId, fieldName);
    }

    /**
     * 删除某一项目的数据
     */
    public int delByIId(Integer itemId) {
        if (itemId == null) {
            return 0;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("itemId", itemId);
        return delByToMap(toMap);
    }

    /**
     * 删除某些项目的数据
     */
    public int delByIIds(List<Integer> itemIds) {
        String infield = "itemId";
        return delByInFiledMayToMap(itemIds, infield, null);
    }


    /**
     * 通过检验表id和类型获得数据
     */
    public List<BetweenTestItem> getByTIdAndTType(Object testId, Object testType) {
        if (MyObjectUtil.noNullOrEmpty(testId) && MyObjectUtil.noNullOrEmpty(testType)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("testId", testId);
            toMap.put("testType", testType);
            return getByToMap(toMap);
        }
        return null;
    }

    /**
     * 通过检验表id和类型获得项目id集合
     */
    public List<Integer> getItemIdsByTIdAndTType(Object testId, Object testType) {
        List<BetweenTestItem> list = getByTIdAndTType(testId, testType);
        return TestItemUtil.getItemIdsByItems(list);
    }


    /**
     * 通过检验_项目集合删除数据
     */
    public void delByItems(List<BetweenTestItem> list) throws Exception {
        if (list != null) {
            List<Integer> btIds = list.stream().map(BetweenTestItem::getId).collect(Collectors.toList());
            int btIndex = batchDeleteByIds(btIds);
            if (btIndex != btIds.size()) {
                throw new Exception("检验_项目删除失败");
            }
        }
    }


    /**
     * 通过检验表id集合和类型获得数据
     */
    public List<BetweenTestItem> getByTIdsAndTType(List<Integer> testIds, String testType) {
        if (MyObjectUtil.noNullOrEmpty(testType)) {
            String propertyName = "testId";
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("testType", testType);
            return getByInFiledMayToMap(testIds, propertyName, toMap);
        }
        return null;
    }


}
