package com.github.wxiaoqi.security.jd.sys.feign.Service;

import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.jd.sys.feign.IBetweenTestItem;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenTestItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 检验_项目服务类
 */
@Service
public class IBetweenTestItemService {
    @Autowired
    private IBetweenTestItem iBetweenTestItem;

    /**
     * 通过检验表id和类型获得数据
     */
    public List<BetweenTestItem> getByTIdAndTType(Integer testId, String testType) {
        return iBetweenTestItem.getByTIdAndTType(testId, testType);
    }

    /**
     * 通过检验_项目集合删除数据
     */
    public void delByItems(List<BetweenTestItem> list) throws Exception {
        if (list != null) {
            List<Integer> btIds = list.stream().map(BetweenTestItem::getId).collect(Collectors.toList());
            int btIndex = iBetweenTestItem.deleteByIds(btIds);
            if (btIndex != btIds.size()) {
                throw new Exception("检验_项目删除失败");
            }
        }
    }

    /**
     * 通过检验表id集合和类型获得数据
     */
    public List<BetweenTestItem> getByTIdsAndTType(List<Integer> testIds, String testType) {
        //@RequestBody注解参数不能为null
        if (testIds == null) {
            return null;
        }
        return iBetweenTestItem.getByTIdsAndTType(testIds, testType);
    }

}
