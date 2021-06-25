package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.ItemInfo;
import com.github.wxiaoqi.security.zs.sys.mapper.ItemInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目
 */
@Service
public class ItemInfoBiz extends BaseBiz<ItemInfoMapper, ItemInfo> {
    @Autowired
    private BetweenTestItemBiz btBiz;


    /**
     * 通过id删除项目、检验_项目
     */
    public ItemInfo delLink(Integer id) throws Exception {
        ItemInfo old = selectById(id);
        if (old == null) {
            return null;
        }
        int index = deleteById(id);
        if (index == 0) {
            throw new Exception("项目删除失败");
        }
        //检验_项目
        btBiz.delByIId(id);
        return old;
    }

    /**
     * 通过id集合删除项目、检验_项目
     */
    public Integer delLink(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<ItemInfo> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = batchDeleteByIds(ids);
            if (index != sum) {
                throw new Exception("项目批量删除失败");
            }
            //检验_项目
            btBiz.delByIIds(ids);
            return sum;
        }
        return null;
    }
}
