package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.LocalAgentBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.LocalAgent;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.msg.TableResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "本地代理")
@RestController
@RequestMapping("LocalAgent")
public class LocalAgentRest extends BaseRest<LocalAgentBiz, LocalAgent> {

    @ApiOperation("添加一条(ip和端口组合唯一，且所有参数非空)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "LocalAgent"),
            @ApiImplicitParam(name = "ip", value = "ip", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "port", value = "端口", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "path", value = "本地路径", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "query",dataType = "String")
    })
    @Override
    public ObjectRestResponse<LocalAgent> add(@RequestBody LocalAgent obj) throws Exception {
        //能否添加
        String errorInfo= baseBiz.isAdd(obj);
        if(errorInfo!=null){
            return new ObjectRestResponse<>(StatusCode.FAIL,errorInfo);
        }
        return super.add(obj);
    }



    @ApiOperation("删除一条")
    @ApiImplicitParam(name = "id", value = "编号", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<LocalAgent> remove(@PathVariable int id) throws Exception {
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
    public ObjectRestResponse<LocalAgent> get(@PathVariable int id) {
        return super.get(id);
    }


    @ApiOperation("分页展示(标题正序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "行数", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "ip", value = "ip(=)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "port", value = "端口(=)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "path", value = "本地路径", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "query",dataType = "String")
    })
    @Override
    public TableResultResponse<LocalAgent> list(@RequestParam
                                                    @ApiIgnore  //该注解使swagger不展示该参数
                                                            Map<String, Object> params) throws Exception {
        String orderBy="title";
        Map<String,Object> toMap=new HashMap<>();
        toMap.put("ip",params.get("ip"));
        toMap.put("port",params.get("port"));
        return baseBiz.selectByQuery(null,orderBy,params,null,null,null,
                toMap,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null);
    }
}
