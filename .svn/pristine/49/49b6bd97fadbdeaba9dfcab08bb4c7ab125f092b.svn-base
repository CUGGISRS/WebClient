package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.entity.FlyPathPoint;
import com.zd.earth.manage.mapper.FlyPathPointMapper;
import com.zd.earth.manage.util.MyObjectUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 飞行路径点
 */
@Service
public class FlyPathPointBiz extends BaseBiz<FlyPathPointMapper, FlyPathPoint> {
    /**
     * 获得某一飞行路径的飞行路径点
     */
    public List<FlyPathPoint> getByPId(Integer pId){
        if(pId!=null){
            Map<String,Object> toMap=new HashMap<>();
            toMap.put("flyPathId",pId);
            return getByToMap(toMap);
        }
        return null;
    }



}
