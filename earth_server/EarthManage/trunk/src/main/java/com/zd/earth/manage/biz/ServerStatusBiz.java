package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.entity.ServerStatus;
import com.zd.earth.manage.mapper.ServerStatusMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务状态
 */
@Service
public class ServerStatusBiz extends BaseBiz<ServerStatusMapper, ServerStatus> {


    /**
     * 查询某一状态的数据
     */
    public List<ServerStatus> getByStatus(Integer status){
        if(status==null){
            return null;
        }
        Map<String,Object>  toMap=new HashMap<>();
        toMap.put("status",status);
        return getByToMap(toMap);
    }
}
