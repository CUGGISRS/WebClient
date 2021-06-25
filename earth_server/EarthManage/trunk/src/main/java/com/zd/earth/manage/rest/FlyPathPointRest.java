package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.FlyPathPointBiz;
import com.zd.earth.manage.biz.FlyPathPointSingleBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.FlyPathPoint;
import com.zd.earth.manage.entity.FlyPathPointSingle;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.util.MyObjectUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;


@Api(tags = "飞行路径点")
@RestController
@RequestMapping("FlyPathPoint")
public class FlyPathPointRest extends BaseRest<FlyPathPointBiz, FlyPathPoint> {
    @Autowired
    private FlyPathPointSingleBiz flyPathPointSingleBiz;

    @ApiOperation("添加一条飞行路径点，可能同时批量添加飞行路径点单点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flyPathPoint", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,dataType = "FlyPathPoint"),
            @ApiImplicitParam(name = "flyPathId", value = "飞行路径id", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型（1飞行点，2观察点，3环绕点）", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "longitude", value = "经度", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "latitude", value = "纬度", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "altitude", value = "高程(米)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "height", value = "高度(米)(添加时统一添加所有关联单点高度)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "orientationAngle", value = "朝向角度（度）", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "pitchAngle", value = "俯仰角度（度）", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "rotateAngle", value = "旋转角度（度）", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "deadTime", value = "停滞时间(秒)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "speed", value = "速度(米/秒)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "radius", value = "半径(米)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "circleNum", value = "圈数(圈)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "flyPathPointSingles", value = "飞行路径点单点集合(此处集合中对象index、pathPointId、height属性值为后端生成)",
                    paramType = "query",dataType = "FlyPathPointSingle",allowMultiple = true)
    })
    @PostMapping("addMaySingles")
    public ObjectRestResponse<FlyPathPoint> addMaySingles(@RequestBody FlyPathPoint flyPathPoint) throws Exception {

        int changed=baseBiz.insertSelective(flyPathPoint);
        if(changed==0){
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        Integer id=flyPathPoint.getId();
        BigDecimal h=flyPathPoint.getHeight();
        List<FlyPathPointSingle> list=flyPathPoint.getFlyPathPointSingles();
        int size=MyObjectUtil.noEmpty(list);
        if(size>0){
            //后端设置单点序号、飞行路径点id、高度
            for (int i=0;i<size;i++){
                FlyPathPointSingle item=list.get(i);
                item.setIndexNum(i+1);
                item.setPathPointId(id);
                item.setHeight(h);
            }
            int index=flyPathPointSingleBiz.batchInsertByList(list);
            if(index!=size){
                return new ObjectRestResponse<>(StatusCode.FAIL,"批量添加飞行路径点单点失败");
            }
        }
        return  new ObjectRestResponse<>(StatusCode.SUCCESS,flyPathPoint,true);
    }


    @ApiOperation("修改一条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flyPathPoint", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,dataType = "FlyPathPoint"),
            @ApiImplicitParam(name = "id", value = "主键", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "flyPathId", value = "飞行路径id", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "type", value = "类型（1飞行点，2观察点，3环绕点）", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "longitude", value = "经度", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "latitude", value = "纬度", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "altitude", value = "高程(米)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "height", value = "高度(米)(修改时统一修改所有关联单点高度)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "orientationAngle", value = "朝向角度（度）", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "pitchAngle", value = "俯仰角度（度）", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "rotateAngle", value = "旋转角度（度）", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "deadTime", value = "停滞时间(秒)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "speed", value = "速度(米/秒)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "radius", value = "半径(米)", paramType = "query",dataType = "BigDecimal"),
            @ApiImplicitParam(name = "circleNum", value = "圈数(圈)", paramType = "query",dataType = "BigDecimal")
    })
    @Override
    public ObjectRestResponse<FlyPathPoint> update(@RequestBody FlyPathPoint flyPathPoint) throws Exception {

        BigDecimal h=flyPathPoint.getHeight();
        //统一修改单点高度
        if(h!=null){
            Integer id=flyPathPoint.getId();
            flyPathPointSingleBiz.updByPId(new FlyPathPointSingle(h),id);
        }
        return super.update(flyPathPoint);
    }

    @ApiOperation("删除一条飞行路径点、飞行路径点单点")
    @ApiImplicitParam(name = "id", value = "主键", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<FlyPathPoint> remove(@PathVariable int id) throws Exception {
        FlyPathPoint old=baseBiz.selectById(id);
        if(old==null){
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        int index=baseBiz.deleteById(id);
        if(index==0){
            return new ObjectRestResponse<>(StatusCode.FAIL,"删除失败");
        }
        //删除相关飞行路径点单点
        List<FlyPathPointSingle> flyPathPointSingles=flyPathPointSingleBiz.getByPId(id);
        List<Integer> psIds= MyObjectUtil.getIdsByList(flyPathPointSingles);
        flyPathPointSingleBiz.batchDeleteByIds(psIds);
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true,old);
    }
}
