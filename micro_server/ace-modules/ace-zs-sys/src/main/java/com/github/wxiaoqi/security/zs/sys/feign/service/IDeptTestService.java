package com.github.wxiaoqi.security.zs.sys.feign.service;

import com.github.wxiaoqi.security.zs.sys.feign.IDeptTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门抽检服务类
 */
@Service
public class IDeptTestService {
    @Autowired
    private IDeptTest iDeptTest;

    /**
     * 通过追溯码获得id集合
     */
    public List<Integer> getIdsByTraceCode(String traceCode) {

        return iDeptTest.getIdsByTraceCode(traceCode);
    }

    /**
     * 通过追溯码集合获得id集合
     */
    public List<Integer> getIdsByTraceCodes(List<String> traceCodes) {
        if (traceCodes == null) {
            return null;
        }
        return iDeptTest.getIdsByTraceCodes(traceCodes);
    }

    /**
     * 通过id集合删除数据
     */
    public int delByIds(List<Integer> ids) {
        if (ids == null) {
            return 0;
        }
        return iDeptTest.delByIds(ids);
    }

}
