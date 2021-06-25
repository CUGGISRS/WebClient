package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.ServerConfigBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.ServerConfig;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Api(tags = "服务配置")
@RequestMapping("ServerConfig")
public class ServerConfigRest extends BaseRest<ServerConfigBiz, ServerConfig> {


    @ApiOperation("添加一条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "ServerConfig"),
            @ApiImplicitParam(name = "configName", value = "配置名称(唯一)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "configContent", value = "配置内容", paramType = "query",dataType = "String")
    })
    @Override
    public ObjectRestResponse<ServerConfig> add(@RequestBody ServerConfig obj) throws Exception {

        String configName=obj.getConfigName();
        //是否唯一
        ServerConfig result=baseBiz.getOneByConfigName(configName);
        if(result!=null){
            return  new ObjectRestResponse<>(StatusCode.FAIL,"配置名称已存在");
        }
        return super.add(obj);
    }

    @ApiOperation("修改一条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "ServerConfig"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "configName", value = "配置名称(唯一)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "configContent", value = "配置内容", paramType = "query",dataType = "String")
    })
    @Override
    public ObjectRestResponse<ServerConfig> update(@RequestBody ServerConfig obj) throws Exception {
        Integer id=obj.getId();
        ServerConfig old=baseBiz.selectById(id);
        if(old!=null){
            //是否唯一
            String configName=obj.getConfigName();
            ServerConfig result=baseBiz.getOneByConfigName(configName);
            if(result!=null&&!id.equals(result.getId())){
                return  new ObjectRestResponse<>(StatusCode.FAIL,"配置名称已存在");
            }
            return super.update(obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
    }

    @ApiOperation("删除一条")
    @ApiImplicitParam(name = "id", value = "编号", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<ServerConfig> remove(@PathVariable int id) throws Exception {
       return super.remove(id);
    }

    @ApiOperation("批量删除")
    @ApiImplicitParam(name = "ids", value = "编号集合", paramType = "body",dataType = "int",allowMultiple = true)
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        return super.batchDeleteByIds(ids);
    }


    @ApiOperation("查询一条")
    @ApiImplicitParam(name = "id", value = "编号", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<ServerConfig> get(@PathVariable int id) {
       return super.get(id);
    }


    @ApiOperation("通过配置名称获得一条数据")
    @ApiImplicitParam(name = "configName", value = "配置名称", paramType = "query",dataType = "String")
    @GetMapping("getOneByConfigName")
    public ObjectRestResponse<ServerConfig> getOneByConfigName(String configName)  {
        ServerConfig obj = baseBiz.getOneByConfigName(configName);
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true,obj);
    }

    @ApiOperation("通过配置名称集合获得数据")
    @ApiImplicitParam(name = "configNames", value = "配置名称集合", paramType = "body",dataType = "String",allowMultiple = true)
    @PostMapping("getByConfigNames")
    public ObjectRestResponse<List<ServerConfig>> getByConfigNames(@RequestBody List<String> configNames) {
        List<ServerConfig> list = baseBiz.getByConfigNames(configNames);
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true,list);
    }

}
