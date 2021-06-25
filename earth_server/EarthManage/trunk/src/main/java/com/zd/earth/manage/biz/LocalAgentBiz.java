package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.entity.LocalAgent;
import com.zd.earth.manage.mapper.LocalAgentMapper;
import com.zd.earth.manage.util.StringHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 本地代理
 */
@Service
public class LocalAgentBiz extends BaseBiz<LocalAgentMapper, LocalAgent> {

    /**
     * 判断能否添加(ip和端口组合唯一，且所有参数非空)
     * @return
     */
    public String isAdd(LocalAgent obj) {
       String ip=obj.getIp();
       String port=obj.getPort();
       String title=obj.getTitle();
       String path=obj.getPath();
        if(StringHelper.isNullOrEmpty(ip)||StringHelper.isNullOrEmpty(port)||StringHelper.isNullOrEmpty(title)||StringHelper.isNullOrEmpty(path)){
            return "参数中存在空值";
        }

        Map<String,Object> toMap=new HashMap<>();
        toMap.put("ip",ip);
        toMap.put("port",port);
        List<LocalAgent> list=getByToMap(toMap);
        if(list!=null&&list.size()>0){
            return "相同ip和端口已存在";
        }
        return null;
    }
}
