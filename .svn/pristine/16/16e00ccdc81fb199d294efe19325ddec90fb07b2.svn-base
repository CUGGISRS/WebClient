package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.ServerStatusBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.ServerStatus;
import com.zd.earth.manage.msg.TableResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;

@Api(tags = "服务状态")
@RestController
@RequestMapping("ServerStatus")
public class ServerStatusRest extends BaseRest<ServerStatusBiz, ServerStatus> {

    @ApiOperation(value = "分页展示(名称正序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页个数",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "uri", value = "路径", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "status", value = "状态(1启动，0停止)", paramType = "query",dataType = "int")
    })
    @Override
    public TableResultResponse<ServerStatus> list(@RequestParam
                                          @ApiIgnore  //该注解使swagger不展示该参数
                                                  Map<String, Object> params)   {
        String orderBy="name";
        return baseBiz.selectByQuery(null,orderBy,params,null,null,null,
                null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null);
    }

}
