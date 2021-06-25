package com.github.wxiaoqi.security.gyx.sys.rest;

import cn.hutool.core.util.PageUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import com.github.wxiaoqi.security.gyx.sys.biz.ConsultBiz;
import com.github.wxiaoqi.security.gyx.sys.dto.ConsultDto;
import com.github.wxiaoqi.security.gyx.sys.entity.ConsultationInfo;
import com.github.wxiaoqi.security.gyx.sys.feign.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = {"专家咨询问答"})
@RestController
@RequestMapping("consult")
public class ConsultController {

    @Resource
    protected ConsultBiz consultBiz;
    @Resource
    protected IUserService iUserService;

    @ApiOperation(value = "分页条件查询所有咨询问答")
    @GetMapping("/getPageByState")
    public ObjectRestResponse<Map<String, Object>> getPageByState(@RequestParam Integer page, @RequestParam Integer limit, @RequestParam Map<String, Object> params) {
        int[] ints = PageUtil.transToStartEnd(page - 1, limit);
        ints[1] = limit;
        return consultBiz.getPageBy(ints, params);
    }

    @ApiOperation(value = "新增")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "标题", required = true, paramType = "form"),
            @ApiImplicitParam(name = "content", value = "内容", required = false, paramType = "form"),
            @ApiImplicitParam(name = "expertId", value = "专家ID", required = false, paramType = "form"),
            @ApiImplicitParam(name = "expertName", value = "专家名称", required = false, paramType = "form"),
//            @ApiImplicitParam(name = "userId", value = "用户ID", required = false, paramType = "form"),
//            @ApiImplicitParam(name = "userName", value = "用户名称", required = false, paramType = "form"),
            @ApiImplicitParam(name = "token", value = "令牌", required = false, paramType = "form")
    })
    @PostMapping("/add")
    public ObjectRestResponse<Integer> add(@RequestBody ConsultDto ConsultDto) {
        JwtUser userInfoByToken = iUserService.getUserInfoByToken(ConsultDto.getToken());
        ConsultDto.setUserId(userInfoByToken.getId());
        ConsultDto.setUserName(userInfoByToken.getAccount());
        ConsultDto.setState("0");
        return consultBiz.add(ConsultDto);
    }

    @ApiOperation(value = "通过用户id，分页获取该用户的问答列表")
    @PostMapping("/getByUserId")
    public ObjectRestResponse<HashMap<String, Object>> getByUserId(@RequestParam Integer page, @RequestParam Integer size, @RequestParam String token) {
        int start = PageUtil.getStart(page - 1, size);
        JwtUser userInfoByToken = iUserService.getUserInfoByToken(token);
        return consultBiz.getByUserId(page, size, userInfoByToken.getId());
    }

    @ApiOperation(value = "通过问答id，获取问答详情")
    @GetMapping("/getOneById")
    public ObjectRestResponse<Map<String, Object>> getOneById(@RequestParam Integer id) {
        return consultBiz.getOneById(id);
    }

    @ApiOperation(value = "更新问答详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "state", value = "回复状态", required = false, paramType = "form"),
            @ApiImplicitParam(name = "replyContent", value = "回复内容", required = false, paramType = "form"),
            @ApiImplicitParam(name = "replyTime", value = "回复时间", required = false, paramType = "form"),
            @ApiImplicitParam(name = "score", value = "得分", required = false, paramType = "form")
    })
    @PutMapping("/update")
    public ObjectRestResponse update(@RequestBody ConsultationInfo consultationInfo) {
        return consultBiz.update(consultationInfo);
    }

    @ApiOperation(value = "管理员删除问答详情(单个||批量)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", required = true, paramType = "form"),
    })
    @GetMapping("/del")
    public ObjectRestResponse delete(@RequestParam List<Integer> ids) {
        return consultBiz.delete(ids);
    }
}
