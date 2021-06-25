package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.entity.ViewManage;
import com.zd.earth.manage.mapper.ViewManageMapper;
import com.zd.earth.manage.util.MyObjectUtil;
import com.zd.earth.manage.util.StringHelper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 视点管理
 */
@Service
public class ViewManageBiz extends BaseBiz<ViewManageMapper, ViewManage> {

    /**
     * 判断能否添加(观察值唯一，其他参数组合唯一,且所有参数非空)
     * @return
     */
    public String isAdd(ViewManage obj) {
        BigDecimal heading=obj.getHeading();
        BigDecimal pitch=obj.getPitch();
        BigDecimal roll=obj.getRoll();
        String watchValue=obj.getWatchValue();
        BigDecimal height=obj.getHeight();
        BigDecimal longitude=obj.getLongitude();
        BigDecimal latitude=obj.getLatitude();
        if(heading==null||pitch==null||roll==null||
        StringHelper.isNullOrEmpty(watchValue)||
        height==null||longitude==null||latitude==null){
            return "参数中存在空值";
        }

        Map<String,Object> toMap=new HashMap<>();
        toMap.put("watchValue",watchValue);
        List<ViewManage> list0=getByToMap(toMap);
        if(MyObjectUtil.noEmpty(list0)>0){
            return "观察值存在重复数据";
        }
        toMap.remove("watchValue");
        height= MyObjectUtil.retainBigDecimal(height,3);
        longitude= MyObjectUtil.retainBigDecimal(longitude,7);
        latitude= MyObjectUtil.retainBigDecimal(latitude,7);
        toMap.put("height",height);
        toMap.put("longitude",longitude);
        toMap.put("latitude",latitude);
        heading= MyObjectUtil.retainBigDecimal(heading,3);
        pitch=MyObjectUtil.retainBigDecimal(pitch,3);
        roll=MyObjectUtil.retainBigDecimal(roll,3);
        toMap.put("heading",heading);
        toMap.put("pitch",pitch);
        toMap.put("roll",roll);
        List<ViewManage> list=getByToMap(toMap);
        if(list!=null&&list.size()>0){
            return "其他参数存在重复数据";
        }
        return null;
    }
}
