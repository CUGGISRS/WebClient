package com.zd.earth.manage.rest;


import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.service.PermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Api(tags = "用户登陆")
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private PermissionService permissionService;


    @ApiOperation("登陆用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "body", value = "接收参数的对象，其他query参数为其属性",  paramType = "body",required = true,
                    dataType = "Map"),
            @ApiImplicitParam(name = "username", value = "用户名（必填）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码（必填）", paramType = "query",dataType = "String")
    })
    @PostMapping("")
    public ObjectRestResponse<String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        Map<String,Object> jwtUser = permissionService.validate(username, password);
        if (jwtUser == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "该用户不存在或用户异常或密码错误", false);
        }
        //生成token
        String token = permissionService.createToken(jwtUser);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, token);
    }



    @ApiOperation("获得请求token中的用户信息")
    @GetMapping("/getCurrentUser")
    public ObjectRestResponse<Map<String,Object>> getCurrentUser(HttpServletRequest request) {
        Map<String,Object> jwtUser = permissionService.getUserInfo(permissionService.getCurrentToken(request));
        if(jwtUser==null){
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, jwtUser);
    }


    @ApiOperation("刷新请求token")
    @GetMapping("/refreshToken")
    public ObjectRestResponse<String> refreshToken(HttpServletRequest request) {
        Map<String,Object> jwtUser =  permissionService.getUserInfo(permissionService.getCurrentToken(request));
        if(jwtUser==null){
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, permissionService.createToken(jwtUser), true);
    }


    @ApiOperation("验证请求token是否过期(200代表没过期)")
    @GetMapping("/isTokenExpired")
    public ObjectRestResponse isTokenExpired(HttpServletRequest request)  {
        String token =permissionService.getCurrentToken(request);
        if(permissionService.isTokenExpired(token)){
            return new ObjectRestResponse(StatusCode.SUCCESS,true);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL, false);
    }

}
