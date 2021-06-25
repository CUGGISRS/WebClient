package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.constants.Constants;
import com.zd.earth.manage.entity.ServerConfig;
import com.zd.earth.manage.mapper.ServerConfigMapper;
import com.zd.earth.manage.util.MyObjectUtil;
import com.zd.earth.manage.util.StringHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务配置
 */
@Service
public class ServerConfigBiz extends BaseBiz<ServerConfigMapper, ServerConfig> {

    /**
     * 修改某一配置名称的数据
     */
    public int updateByConfigName(ServerConfig obj,String configName){
        if(StringHelper.noNullOrEmpty(configName)){
            Map<String,Object> toMap=new HashMap<>();
            toMap.put("configName",configName);
            return  updateByFiledMap(obj,toMap);
        }
        return 0;
    }

    /**
     * 通过配置名称获得一条数据
     */
    public ServerConfig getOneByConfigName(String configName){
        if(StringHelper.noNullOrEmpty(configName)){
            return selectOne(new ServerConfig(configName));
        }
        return null;
    }

    /**
     * 通过配置名称集合获得数据
     */
    public List<ServerConfig> getByConfigNames(List<String> configNames){
        if(MyObjectUtil.noEmpty(configNames)>0){
            String inField="configName";
            return getByInFiledMayToMap(configNames,inField,null);
        }
        return null;
    }

    /**
     * 获得json文件存储目录路径
     */
    public String getJsonFilePath(){
        ServerConfig serverConfig=getOneByConfigName(Constants.JSON_FILE_PATH);
        if(serverConfig!=null){
           return  serverConfig.getConfigContent();
        }
        return null;
    }



}
