package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.FlyPathPointSingleBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.FlyPathPointSingle;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.util.MyObjectUtil;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "飞行路径点单点")
@RestController
@RequestMapping("FlyPathPointSingle")
public class FlyPathPointSingleRest extends BaseRest<FlyPathPointSingleBiz, FlyPathPointSingle> {


    @ApiOperation("批量添加")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "list", value = "接收参数的对象集合，其他query参数为其对象属性",  paramType = "body",required = true,
                    dataType = "FlyPathPointSingle",allowMultiple = true),
            @ApiImplicitParam(name = "pathPointId", value = "飞行路径点id(此处集合中对象该属性值必须非空且相同)", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "indexNum", value = "序号(后端生成)", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "longitude", value = "经度", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "latitude", value = "纬度", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "altitude", value = "高程(米)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "height", value = "高度(米)", paramType = "query",dataType = "BigDecimal")
    })
    @PostMapping("batchAdd")
    public ObjectRestResponse<Integer> batchAdd( @RequestBody List<FlyPathPointSingle> list) throws Exception {
        int size= MyObjectUtil.noEmpty(list);
        if(size==0){
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        Integer lastIndex=null;
        Integer pathPointId=null;
        //后端设置单点序号
        for (int i=0;i<size;i++){
            FlyPathPointSingle item=list.get(i);
            Integer pId=item.getPathPointId();
           if(pathPointId==null){
               pathPointId=pId;
           }else {
              if(!pathPointId.equals(pId)){
                  return new ObjectRestResponse<>(StatusCode.FAIL,"飞行路径点id必须为同一值");
              }
           }
           if(pathPointId==null){
               return new ObjectRestResponse<>(StatusCode.FAIL,"飞行路径点id不能为空");
           }
            //获得单点最大序号
            if(lastIndex==null){
               lastIndex=baseBiz.getMaxIndexByPId(pathPointId);
            }
            item.setIndexNum(lastIndex==null?1:lastIndex+1);
            lastIndex++;
        }
        int index=baseBiz.batchInsertByList(list);
        if(index!=size){
            return new ObjectRestResponse<>(StatusCode.FAIL,"批量添加飞行路径点单点失败");
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true,size);
    }

    @ApiOperation("修改一条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flyPathPointSingle", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "FlyPathPointSingle"),
            @ApiImplicitParam(name = "id", value = "主键", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "pathPointId", value = "飞行路径点id", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "indexNum", value = "序号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "longitude", value = "经度", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "latitude", value = "纬度", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "altitude", value = "高程(米)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "height", value = "高度(米)", paramType = "query",dataType = "BigDecimal")
    })
    @Override
    public ObjectRestResponse<FlyPathPointSingle> update(@RequestBody FlyPathPointSingle flyPathPointSingle) throws Exception {
        return super.update(flyPathPointSingle);
    }


    @ApiOperation("删除一条")
    @ApiImplicitParam(name = "id", value = "主键", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<FlyPathPointSingle> remove( @PathVariable int id) throws Exception {
        return super.remove(id);
    }
}
