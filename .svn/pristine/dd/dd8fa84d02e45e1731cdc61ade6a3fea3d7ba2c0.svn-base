package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.FigureDataBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.FigureData;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.msg.TableResultResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;

@Api(tags = "图形数据")
@RestController
@RequestMapping("FigureData")
public class FigureDataRest extends BaseRest<FigureDataBiz, FigureData> {

    @ApiOperation("添加一条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "FigureData"),
            @ApiImplicitParam(name = "name", value = "名称(唯一)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "content", value = "内容（json字符串）", paramType = "query",dataType = "String")
    })
    @Override
    public ObjectRestResponse<FigureData> add(@RequestBody FigureData obj) throws Exception {
        //是否唯一
        String name=obj.getName();
        FigureData old=baseBiz.getOneByName(name);
        if(old!=null){
            return new ObjectRestResponse<>(StatusCode.FAIL,"相同名称已存在");
        }
        return super.add(obj);
    }

    @ApiOperation("修改一条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "FigureData"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称(唯一)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "content", value = "内容（json字符串）", paramType = "query",dataType = "String")
    })
    @Override
    public ObjectRestResponse<FigureData> update(@RequestBody FigureData obj) throws Exception {
        Integer id=obj.getId();
        FigureData old=baseBiz.selectById(id);
        if(old!=null){
            //是否唯一
            String name=obj.getName();
            FigureData result=baseBiz.getOneByName(name);
            if(result!=null&&!id.equals(result.getId())){
                return  new ObjectRestResponse<>(StatusCode.FAIL,"相同名称已存在");
            }

            return super.update(obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
    }


    @ApiOperation("删除一条")
    @ApiImplicitParam(name = "id", value = "编号", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<FigureData> remove(@PathVariable int id) throws Exception {
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
    public ObjectRestResponse<FigureData> get(@PathVariable int id) {
        return super.get(id);
    }


    @ApiOperation("分页展示(id倒序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "行数", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称(唯一)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "content", value = "内容（json字符串）", paramType = "query",dataType = "String")
    })
    @Override
    public TableResultResponse<FigureData> list(@RequestParam
                                                @ApiIgnore  //该注解使swagger不展示该参数
                                                        Map<String, Object> params) throws Exception {
        String orderBy="id desc";
        return baseBiz.selectByQuery(null,orderBy,params,null,null,null,
                null,null,null,null,null,null,null,null,
                null,null,null,null,null,null,
                null,null,null,null,null,null);
    }
}
