package com.github.wxiaoqi.security.jd.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.jd.sys.entity.BetweenDeptUser;
import com.github.wxiaoqi.security.jd.sys.entity.Dept;
import com.github.wxiaoqi.security.jd.sys.feign.Service.IFileInfoService;
import com.github.wxiaoqi.security.jd.sys.mapper.DeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * 监督部门
 */
@Service
public class DeptBiz extends BaseBiz<DeptMapper, Dept> {
    @Autowired
    private BetweenDeptUserBiz deptUserBiz;
    @Autowired
    private IFileInfoService fileInfoService;

    /**
     * 通过id删除部门，部门用户，用户（带文件）
     */
    public Dept delLinkOne(Integer id) throws Exception {
        Dept old = selectById(id);
        if (old == null) {
            return null;
        }
        int index = deleteById(id);
        if (index == 0) {
            throw new Exception("删除部门失败");
        }
        //删除部门用户、用户(带文件)
        List<BetweenDeptUser> bduList = deptUserBiz.getByDId(id);
        List<String> oldUrls=new ArrayList<>();
        deptUserBiz.delBDUAndUser(bduList,oldUrls);
        fileInfoService.delFileByUrls(oldUrls);
        return old;
    }

    /**
     * 通过id集合删除部门，部门用户，用户（带文件）
     */
    public Integer delLinkData(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<Dept> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = batchDeleteByIds(ids);
            if (index != sum) {
                throw new Exception("部门批量删除失败");
            }

            //删除部门用户、用户(带文件)
            List<BetweenDeptUser> bduList = deptUserBiz.getByDIds(ids);
            List<String> oldUrls=new ArrayList<>();
            deptUserBiz.delBDUAndUser(bduList,oldUrls);
            fileInfoService.delFileByUrls(oldUrls);
            return sum;
        }
        return null;
    }
}
