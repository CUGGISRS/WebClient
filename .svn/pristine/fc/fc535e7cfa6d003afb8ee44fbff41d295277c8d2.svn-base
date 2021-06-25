package com.github.wxiaoqi.security.jd.sys.feign.Service;

import com.github.wxiaoqi.security.jd.sys.feign.IItemInfo;
import com.github.wxiaoqi.security.zs.sys.entity.ItemInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 项目服务类
 */
@Service
public class IItemInfoService {
    @Autowired
    private IItemInfo iItemInfo;

    /**
     * 通过id集合查询数据
     */
    public List<ItemInfo> selectByIds(List<Integer> ids) {
        //@RequestBody注解参数不能为null
        if (ids == null) {
            return null;
        }
        return iItemInfo.selectByIds(ids);
    }
}
