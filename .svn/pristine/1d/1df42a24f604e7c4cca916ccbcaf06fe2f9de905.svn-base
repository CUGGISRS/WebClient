package com.zd.earth.manage.rest;

import com.zd.earth.manage.biz.FlyPathBiz;
import com.zd.earth.manage.biz.FlyPathPointBiz;
import com.zd.earth.manage.biz.FlyPathPointSingleBiz;
import com.zd.earth.manage.common.BaseRest;
import com.zd.earth.manage.entity.FlyPath;
import com.zd.earth.manage.entity.FlyPathPoint;
import com.zd.earth.manage.entity.FlyPathPointSingle;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.msg.TableResultResponse;
import com.zd.earth.manage.util.MyObjectUtil;
import com.zd.earth.manage.util.StringHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Api(tags = "飞行路径")
@RestController
@RequestMapping("FlyPath")
public class FlyPathRest extends BaseRest<FlyPathBiz, FlyPath> {
    @Autowired
    private FlyPathPointBiz flyPathPointBiz;
    @Autowired
    private FlyPathPointSingleBiz flyPathPointSingleBiz;


    @ApiOperation("添加一条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flyPath", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "FlyPath"),
            @ApiImplicitParam(name = "name", value = "名称(非空)", paramType = "query",dataType = "String")
    })
    @Override
    public ObjectRestResponse<FlyPath> add(@RequestBody FlyPath flyPath) throws Exception {
        String name = flyPath.getName();
        if (StringHelper.isNullOrEmpty(name)) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        return super.add(flyPath);
    }


    @ApiOperation("修改一条")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "flyPath", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "FlyPath"),
            @ApiImplicitParam(name = "id", value = "编号", paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "name", value = "名称(非空)", paramType = "query",dataType = "String")
    })
    @Override
    public ObjectRestResponse<FlyPath> update(@RequestBody FlyPath flyPath) throws Exception {
        String name = flyPath.getName();
        if (StringHelper.isNullOrEmpty(name)) {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
        }
        return super.update(flyPath);
    }

    @ApiOperation("删除一条飞行路径、飞行路径点、飞行路径点单点")
    @ApiImplicitParam(name = "id", value = "主键", paramType = "path",dataType = "int")
    @Override
    public ObjectRestResponse<FlyPath> remove(@PathVariable int id) throws Exception {
        FlyPath old = baseBiz.selectById(id);
        if (old != null) {
            int index = baseBiz.deleteById(id);
            if (index > 0) {
                //删除相关飞行路径点、飞行路径点单点
                List<FlyPathPoint> flyPathPoints = flyPathPointBiz.getByPId(id);
                List<Integer> pIds = MyObjectUtil.getIdsByList(flyPathPoints);
                flyPathPointBiz.batchDeleteByIds(pIds);
                List<FlyPathPointSingle> flyPathPointSingles = flyPathPointSingleBiz.getByPIds(pIds);
                List<Integer> psIds = MyObjectUtil.getIdsByList(flyPathPointSingles);
                flyPathPointSingleBiz.batchDeleteByIds(psIds);

                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
            }
            return new ObjectRestResponse<>(StatusCode.FAIL, "删除失败");
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    @ApiOperation("查询所有飞行路径、飞行路径点、飞行路径点单点")
    @GetMapping("getLinkALl")
    public ObjectRestResponse<List<FlyPath>> getLinkALl() {
        List<FlyPath> list = baseBiz.getLinkAll();
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, list);
    }


    @ApiOperation("分页展示飞行路径、飞行路径点、飞行路径点单点(id倒序)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码",  paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "limit", value = "行数", paramType = "query",dataType = "int")
    })
    @GetMapping("pageLink")
    public TableResultResponse<FlyPath> list(Integer page, Integer limit) throws Exception {
        String orderBy = "id desc";
        int total = baseBiz.getCountAll();
        List<FlyPath> list = baseBiz.pageLink(page, limit, orderBy);
        return new TableResultResponse<>(total, list);
    }
}
