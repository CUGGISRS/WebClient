package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.entity.FigureData;
import com.zd.earth.manage.mapper.FigureDataMapper;
import com.zd.earth.manage.util.StringHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图形数据
 */
@Service
public class FigureDataBiz extends BaseBiz<FigureDataMapper, FigureData> {
    /**
     * 通过名称获得一条信息
     */
    public FigureData getOneByName(String name){
        if(StringHelper.isNullOrEmpty(name)){
            return null;
        }
        return selectOne(new FigureData(name));
    }

}
