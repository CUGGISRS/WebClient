package com.github.wxiaoqi.security.com.sys.biz;

import com.github.wxiaoqi.security.com.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.com.sys.mapper.DataDictionaryMapper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据字典
 */
@Service
public class DataDictionaryBiz extends BaseBiz<DataDictionaryMapper, DataDictionary> {

    /**
     * （通过类型、名称、代码、备注，与连接，相等条件）查询全部数据(code排序)
     *
     * @param type
     * @param remark
     * @param name
     * @param code
     * @return
     */
    public List<DataDictionary> getAllMayToCondition(String type, String remark, String name, String code) {
        Map<String, Object> map = new HashMap<>();
        String orderBy = "code";
        map.put("type", type);
        map.put("remark", remark);
        map.put("name", name);
        map.put("code", code);
        return getMayCondition(null, orderBy, null, null, null, null,
                map, null, null, null, null, null, null,
                null, null, null, null, null,
                null, null, null, null, null, null,
                null, null);
    }
}
