package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.constants.Constants;
import com.zd.earth.manage.entity.FlyPath;
import com.zd.earth.manage.mapper.FlyPathMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 飞行路径
 */
@Service
public class FlyPathBiz extends BaseBiz<FlyPathMapper, FlyPath> {
    /**
     * 连表查询全部
     */
    public List<FlyPath> getLinkAll(){
        return mapper.getLinkAll();
    }
    /**
     * 查询总数
     */
    public int getCountAll(){
        return mapper.getCountAll();
    }

    /**
     *（sql注入排序语句后）连表分页
     */
    public List<FlyPath> pageLink(Integer page,Integer limit, String orderBy){
        if(page!=null&&limit!=null){
            if(page>0){
                page=(page-1)*limit;
            }else {
                page=null;
            }
            if(limit<0){
                limit=0;
            }
        }
        List<FlyPath> list=mapper.pageLink(page,limit,orderBy);
        return list;
    }

}
