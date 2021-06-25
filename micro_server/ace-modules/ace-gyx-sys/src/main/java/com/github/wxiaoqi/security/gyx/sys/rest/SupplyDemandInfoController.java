package com.github.wxiaoqi.security.gyx.sys.rest;

import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import com.github.wxiaoqi.security.gyx.sys.biz.SupplyDemandInfoBiz;
import com.github.wxiaoqi.security.gyx.sys.entity.SupplyDemandInfo;
import com.github.wxiaoqi.security.gyx.sys.feign.IUserService;
import com.github.wxiaoqi.security.info.sys.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"供求信息"})
@RestController
@RequestMapping("supplydemand")
public class SupplyDemandInfoController {

    @Autowired
    protected SupplyDemandInfoBiz supplyDemandInfoBiz;
    @Resource
    private IUserService iUserService;

    @ApiOperation(notes = "按供求类型分页查询供求信息", value = "按供求类型分页查询供求信息(只查询已审核的)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageRow", value = "每页个数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pubType", value = "供求类型", required = true, paramType = "query"),
    })
    @RequestMapping(value = "/getInfoListByType", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<JSONObject> getInfoListByType(HttpServletRequest request) {
        JSONObject jsonObject = CommonUtil.request2Json(request);
        CommonUtil.hasAllRequired(jsonObject, "pageNum,pageRow,pubType");
        return supplyDemandInfoBiz.getInfoListByType(jsonObject);
    }

//    /**
//     * 分页获取供求信息  cj
//     *
//     * @param params
//     * @return
//     */
//    @ApiOperation(notes = "分页获取供求信息", value = "分页获取供求信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "limit", value = "每页个数", required = true, paramType = "query"),
//            @ApiImplicitParam(name = "pubType", value = "供求类型", required = true, paramType = "query"),
//    })
//    @RequestMapping(value = "/listByPage", method = RequestMethod.GET)
//    public ObjectRestResponse listByPage(@RequestParam Map<String, Object> params) {
//        return baseBiz.listByPage(params);
//    }

    @ApiOperation(notes = "新增供求信息", value = "新增供求信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, paramType = "form"),
            @ApiImplicitParam(name = "contactPerson", value = "联系人", required = false, paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "联系电话", required = false, paramType = "form"),
            @ApiImplicitParam(name = "area", value = "所在地区", required = false, paramType = "form"),
            @ApiImplicitParam(name = "pubTime", value = "发布时间", required = false, paramType = "form"),
            @ApiImplicitParam(name = "pubType", value = "供求类型（0表示供应信息、1表示求购信息）", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", value = "审核状态（0表示待审核、1表示审核通过，-1表示审核未通过）", required = false, paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, paramType = "form"),
            @ApiImplicitParam(name = "files", value = "文件数组", required = false, paramType = "form"),
    })
    @RequestMapping(value = "/addNewSupplyDemand", method = RequestMethod.POST)
    public ObjectRestResponse add(@RequestParam Map<String, String> map, @RequestParam MultipartFile[] files) {
        return supplyDemandInfoBiz.addNewSupplyDemand(map, files);
    }

    @ApiOperation(value = "更新供求信息", notes = "使用postman测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "农产品id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "title", value = "农产品名称", required = false, paramType = "form"),
            @ApiImplicitParam(name = "contactPerson", value = "总类", required = false, paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "联系电话", required = false, paramType = "form"),
            @ApiImplicitParam(name = "area", value = "所在地区", required = false, paramType = "form"),
            @ApiImplicitParam(name = "pubTime", value = "发布时间", required = false, paramType = "form"),
            @ApiImplicitParam(name = "pubType", value = "供求类型（0表示供应信息、1表示求购信息）", required = false, paramType = "form"),
            @ApiImplicitParam(name = "status", value = "审核状态（0表示待审核、1表示审核通过，-1表示审核未通过）", required = false, paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, paramType = "form"),
            @ApiImplicitParam(name = "delIds", value = "删除的图片id数组", required = false, paramType = "form"),
            @ApiImplicitParam(name = "files", value = "文件数组", required = false, paramType = "form"),
    })
    @PutMapping("/updateSupplyDemand")
    public ObjectRestResponse updateSupplyDemand(@RequestParam Integer id,
                                                 String title,
                                                 String contactPerson,
                                                 String phone,
                                                 String area,
                                                 String pubTime,
                                                 String pubType,
                                                 String status,
                                                 String description,
                                                 Integer[] delIds,
                                                 MultipartFile[] files) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SupplyDemandInfo supplyDemandInfo = new SupplyDemandInfo();
        supplyDemandInfo.setId(id);
        supplyDemandInfo.setTitle(title);
        supplyDemandInfo.setContactPerson(contactPerson);
        supplyDemandInfo.setPhone(phone);
        supplyDemandInfo.setArea(area);
        supplyDemandInfo.setPubTime(simpleDateFormat.parse(pubTime));
        supplyDemandInfo.setPubType(pubType);
        supplyDemandInfo.setStatus(status);
        supplyDemandInfo.setDescription(description);
        return supplyDemandInfoBiz.updateSupplyDemand(supplyDemandInfo, delIds, files);
    }

//    @ApiOperation(notes = "获取一条供求信息详细信息", value = "获取一条供求信息详细信息")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "params"),
//    })
//    @RequestMapping(value = "/getSupplyDemandById", method = RequestMethod.GET)
//    public ObjectRestResponse getSupplyDemandById(@RequestParam int id) {
//        return baseBiz.getSupplyDemandById(id);
//    }

    @ApiOperation(value = "分页查询供求信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "页码", required = true, paramType = "query"),
            @ApiImplicitParam(name = "size", value = "每页个数", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pubType", value = "供求类型", paramType = "query"),
            @ApiImplicitParam(name = "title", value = "标题", paramType = "query"),
            @ApiImplicitParam(name = "contactPerson", value = "联系人", paramType = "query"),
    })
    @GetMapping("/allByPage")
    public ObjectRestResponse<JSONObject> all(@RequestParam Integer page, @RequestParam Integer size, @RequestParam Map<String, Object> params) {
        int[] ints = PageUtil.transToStartEnd(page - 1, size);
        ints[1] = size;
        return supplyDemandInfoBiz.all(ints, params);
    }

    @ApiOperation(value = "查询一条供求信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "query"),
    })
    @GetMapping("/getOneById")
    public ObjectRestResponse getOneById(@RequestParam Integer id) {
        return supplyDemandInfoBiz.getOneById(id);
    }

    @ApiOperation(value = "批量删除&单个删除，传List<Integer>")
    @GetMapping("/del")
    public ObjectRestResponse<T> delOne(@RequestParam List<Integer> ids) {
        return supplyDemandInfoBiz.delOne(ids);
    }

    @ApiOperation(value = "用户：新增供求信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, paramType = "form"),
            @ApiImplicitParam(name = "contactPerson", value = "联系人", required = false, paramType = "form"),
            @ApiImplicitParam(name = "phone", value = "联系电话", required = false, paramType = "form"),
            @ApiImplicitParam(name = "area", value = "所在地区", required = false, paramType = "form"),
//            @ApiImplicitParam(name = "pubTime", value = "发布时间", required = false, paramType = "form"),
            @ApiImplicitParam(name = "type", value = "供求类型（供应信息、求购信息）", required = false, paramType = "form"),
//            @ApiImplicitParam(name = "status", value = "审核状态（待审核、审核通过）", required = false, paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, paramType = "form"),
            @ApiImplicitParam(name = "token", value = "令牌", required = false, paramType = "form"),
            @ApiImplicitParam(name = "files", value = "文件数组", required = false, paramType = "form"),
    })
    @RequestMapping(value = "/addNewSupplyByUser", method = RequestMethod.POST)
    public ObjectRestResponse addByUer(@RequestParam String type,
                                       String title,
                                       String contactPerson,
                                       String phone,
                                       String area,
                                       String description,
                                       String token,
                                       MultipartFile[] files) {
        SupplyDemandInfo supplyDemandInfo = new SupplyDemandInfo();
        supplyDemandInfo.setPubType(type);
        supplyDemandInfo.setTitle(title);
        supplyDemandInfo.setContactPerson(contactPerson);
        supplyDemandInfo.setPhone(phone);
        supplyDemandInfo.setArea(area);
        supplyDemandInfo.setDescription(description);
        //未审核
        supplyDemandInfo.setStatus("0");
        supplyDemandInfo.setPubTime(new Date());
        //解析用户信息
        JwtUser userInfoByToken = iUserService.getUserInfoByToken(token);
        supplyDemandInfo.setUserid(userInfoByToken.getId());
        return supplyDemandInfoBiz.addByUer(supplyDemandInfo, files);
    }

    @ApiOperation(value = "根据用户信息：分页查询供求信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "标题", required = true, paramType = "form"),
            @ApiImplicitParam(name = "size", value = "联系人", required = false, paramType = "form"),
            @ApiImplicitParam(name = "token", value = "联系电话", required = false, paramType = "form"),
    })
    @RequestMapping(value = "/getPageByUserToken", method = RequestMethod.POST)
    public ObjectRestResponse<HashMap<String, Object>> getPageByUserToken(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String token) {
        int start = PageUtil.getStart(page - 1, size);
        //解析用户信息
        JwtUser userInfoByToken = iUserService.getUserInfoByToken(token);
        //用户ID
        Integer userId = userInfoByToken.getId();
        return supplyDemandInfoBiz.getPageByUserToken(start, size, userId);
    }
}
