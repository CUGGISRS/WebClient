package com.github.wxiaoqi.security.dzsw.sys.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.dzsw.sys.biz.DataDictionaryInfoBiz;
import com.github.wxiaoqi.security.dzsw.sys.entity.DataDictionaryInfo;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author TsaiJun
 */
@Api(tags = {"数据字典"})
@RestController
@RequestMapping("/dic")
@ResponseBody
public class DictionaryController extends BaseController<DataDictionaryInfoBiz, DataDictionaryInfo> {

    @Autowired
    protected DataDictionaryInfoBiz dataDictionaryInfoBiz;

    @PostMapping("/test")
    public int testMethod() {
        return 12;
    }


    @ApiOperation(notes = "按type数据类型查询数据字典", value = "按type数据类型查询数据字典")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "数据字典类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "isCodeAsc", value = "是否按code正序", required = false, paramType = "query", dataType = "boolean")
    })
    @RequestMapping(value = "/getDicByType", method = RequestMethod.POST)
    public ObjectRestResponse dictionaryListByType(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        return dataDictionaryInfoBiz.dictionaryListByType(jsonObject);
    }

    /**
     * 根据数据类型和数据名称查询一条数据字典
     *
     * @return
     */
    @RequestMapping(value = "/getDicByTypeAndText", method = RequestMethod.GET)
    public ObjectRestResponse getDicByTypeAndText(@RequestParam Map<String, Object> map) {
        return dataDictionaryInfoBiz.getDicByTypeAndText(map);
    }

    // 新增一条数据字典信息
    @ApiOperation(notes = "新增一条数据字典信息", value = "新增一条数据字典信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "数据字典编号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "数据字典类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "text", value = "数据字典名称", required = true, paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "数据字典备注", required = false, paramType = "query"),
    })
    @RequestMapping(value = "/addDic", method = RequestMethod.POST)
    public ObjectRestResponse addDic(@RequestParam Map<String, Object> map) {
        return dataDictionaryInfoBiz.addDic(map);
    }

    @ApiOperation(notes = "删除一条数据字典信息", value = "删除一条数据字典信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "数据字典编号", required = true, paramType = "query")
    })
    @RequestMapping(value = "/delDic", method = RequestMethod.POST)
    public ObjectRestResponse delDic(@RequestParam String code) {
        return dataDictionaryInfoBiz.delDic(code);
    }

    @ApiOperation(notes = "更新一条数据字典信息", value = "更新一条数据字典信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "数据字典编号", required = true, paramType = "query"),
            @ApiImplicitParam(name = "type", value = "数据字典类型", required = true, paramType = "query"),
            @ApiImplicitParam(name = "text", value = "数据字典名称", required = true, paramType = "query"),
            @ApiImplicitParam(name = "remark", value = "数据字典备注", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/updateDic", method = RequestMethod.POST)
    public ObjectRestResponse updateDic(@RequestParam Map<String, Object> map) {
        return dataDictionaryInfoBiz.updateDic(map);
    }
}
