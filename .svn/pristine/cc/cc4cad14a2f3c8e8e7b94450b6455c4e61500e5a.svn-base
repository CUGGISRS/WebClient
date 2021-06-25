package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.entity.FlyPathPointSingle;
import com.zd.earth.manage.mapper.FlyPathPointSingleMapper;
import com.zd.earth.manage.util.MyObjectUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 飞行路径点单点
 */
@Service
public class FlyPathPointSingleBiz extends BaseBiz<FlyPathPointSingleMapper, FlyPathPointSingle> {

    /**
     * 获得某一飞行路径点的单点
     */
    public List<FlyPathPointSingle> getByPId(Integer pId){
        if(pId!=null){
            Map<String,Object> toMap=new HashMap<>();
            toMap.put("pathPointId",pId);
            return getByToMap(toMap);
        }
        return null;
    }

    /**
     * 获得某些飞行路径点的单点
     */
    public List<FlyPathPointSingle> getByPIds(List<Integer> pIds){
        String inField="pathPointId";
        return getByInFiledMayToMap(pIds,inField,null);
    }

    /**
     * 修改某一飞行路径点的单点
     */
    public int updByPId(FlyPathPointSingle t,Integer pId){
        if(pId!=null){
            Map<String,Object> toMap=new HashMap<>();
            toMap.put("pathPointId",pId);
            return updateByFiledMap(t,toMap);
        }
        return 0;
    }


    /**
     * 获得某一飞行路径点的最大单点序号
     */
    public Integer getMaxIndexByPId(Integer pId){
        Integer maxIndex=null;
        List<FlyPathPointSingle> list=getByPId(pId);
        int size=MyObjectUtil.noEmpty(list);
        if(size>0){
            for (int i=0;i<size;i++){
                FlyPathPointSingle item=list.get(i);
                Integer index=item.getIndexNum();
                if(index!=null){
                    if(maxIndex==null){
                        maxIndex=index;
                    }else {
                     if(index.intValue()>maxIndex.intValue()){
                         maxIndex=index;
                     }
                    }
                }
            }
        }
       return maxIndex;
    }
}
