package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.ViewManageBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.ViewManage;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.msg.TableResultResponse;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;


@Api(tags = "视点管理")
@RestController
@RequestMapping("ViewManage")
public class ViewManageRest extends BaseRest<ViewManageBiz, ViewManage> {


    @ApiOperation("添加一条(观察值唯一，其他参数组合唯一,且所有参数非空)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "ViewManage"),
            @ApiImplicitParam(name = "heading", value = "起点(20,3)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "pitch", value = "最高点(20,3)",  paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "roll", value = "翻转点(20,3)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "watchValue", value = "观察值",  paramType = "query"),
            @ApiImplicitParam(name = "height", value = "高度(20,3)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "longitude", value = "经度(10,7)",  paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "latitude", value = "纬度(10,7)", paramType = "query",dataType = "BigDecimal")
    })
    @Override
    public ObjectRestResponse<ViewManage> add(@RequestBody ViewManage obj) throws Exception {

        //能否添加
       String errorInfo= baseBiz.isAdd(obj);
       if(errorInfo!=null){
           return new ObjectRestResponse<>(StatusCode.FAIL,errorInfo);
       }
        return super.add(obj);
    }


    @ApiOperation("删除一条")
    @ApiImplicitParam(name = "id", value = "主键", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<ViewManage> remove(@PathVariable int id) throws Exception {
        return super.remove(id);
    }


    @ApiOperation(value = "分页展示(id倒序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "每页个数",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "heading", value = "起点(20,3)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "pitch", value = "最高点(20,3)",  paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "roll", value = "翻转点(20,3)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "watchValue", value = "观察值",  paramType = "query"),
            @ApiImplicitParam(name = "height", value = "高度(20,3)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "longitude", value = "经度(10,7)",  paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "latitude", value = "纬度(10,7)", paramType = "query",dataType = "BigDecimal")
    })
    @GetMapping("pageByCondition")
    public TableResultResponse<ViewManage> list(@RequestParam
                                                 @ApiIgnore  //该注解使swagger不展示该参数
                                                 Map<String, Object> params) throws Exception {
        String orderBy="id desc";
        return baseBiz.selectByQuery(null,orderBy,params,null,null,null,
                null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null);
    }

}
