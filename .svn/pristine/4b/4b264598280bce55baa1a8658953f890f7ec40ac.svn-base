package com.github.wxiaoqi.security.dzsw.sys.rest;

import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.dzsw.sys.annotation.UserToken;
import com.github.wxiaoqi.security.dzsw.sys.biz.QAInfoBiz;
import com.github.wxiaoqi.security.dzsw.sys.entity.QAInfo;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;

@Api(tags = {"留言问答"})
@RestController
@RequestMapping("/qa")
@ResponseBody
public class QAInfoController extends BaseController<QAInfoBiz, QAInfo> {

    @Autowired
    private QAInfoBiz qaInfoBiz;

    @GetMapping("/test")
    public JwtUser test(@UserToken JwtUser jwtUser) {
        return jwtUser;
    }

    @PostMapping("/addQAWithToken")
    @ApiOperation(value = "新增留言问答")
    @ResponseBody
    public ObjectRestResponse addQAWithToken(@UserToken JwtUser jwtUser, @RequestParam Map<String, String> body) throws ParseException {
        return qaInfoBiz.addQA(jwtUser, body);
    }

    @PostMapping("/addQA")
    @ApiOperation(value = "游客新增留言问答")
    @ResponseBody
    public ObjectRestResponse addQAWithoutToken(@RequestParam Map<String, String> body) {
        return qaInfoBiz.addQA(body);
    }

    @PostMapping("/getQA")
    @ApiOperation(value = "查看一条留言详细信息")
    @ResponseBody
    public ObjectRestResponse getQAById(@RequestParam int qaId) {
        return qaInfoBiz.getQAById(qaId);
    }

    @ApiOperation(notes = "按照时间倒序分页查询留言信息", value = "showQAOrderByTimePage")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageRow", value = "每页个数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "status", value = "状态", paramType = "query"),
            @ApiImplicitParam(name = "targetSystem", value = "目标系统", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/listByTime", method = RequestMethod.POST)
    public ObjectRestResponse listByTime(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(jsonObject, "pageNum,pageRow,targetSystem");
        return qaInfoBiz.listByTime(jsonObject);
    }

    @RequestMapping("/listByPage")
    public ObjectRestResponse listByPage(@RequestParam Map<String, Object> params) {
        return baseBiz.listByPage(params);
    }

    @PostMapping("/delQA")
    @ApiOperation(value = "删除一条留言信息，同时删除回复信息")
    @ResponseBody
    public ObjectRestResponse delQAById(@RequestParam int qaId) {
        return qaInfoBiz.delQAById(qaId);
    }

    @ApiOperation(value = "删除一条留言信息，同时删除回复信息")
    @Override
    public ObjectRestResponse<QAInfo> remove(@PathVariable int id) throws Exception {
        return qaInfoBiz.delById(id);
    }
}
