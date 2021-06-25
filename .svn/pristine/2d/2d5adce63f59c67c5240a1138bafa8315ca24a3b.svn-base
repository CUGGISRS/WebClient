package com.github.wxiaoqi.security.com.sys.rest;

import com.github.wxiaoqi.security.auth.client.config.UserAuthConfig;
import com.github.wxiaoqi.security.com.sys.biz.FileInfoBiz;
import com.github.wxiaoqi.security.com.sys.biz.UserBiz;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.User;
import com.github.wxiaoqi.security.com.sys.rest.service.PermissionService;
import com.github.wxiaoqi.security.common.context.BaseContextHandler;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.util.MD5Util;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.vo.UserInfo;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登陆注册或其它任意接口
 */
@RestController
public class LoginRegController {

    @Autowired
    private PermissionService permissionService;
    @Autowired
    private UserBiz userBiz;
    @Autowired
    private FileInfoBiz fileInfoBiz;

    @Autowired
    private UserAuthConfig userAuthConfig;

    @GetMapping("getUserByToken")
    public UserInfo getUserByToken(String token) {
        return permissionService.getUserInfo(token);
    }

    @GetMapping("isTokenExpired")
    public boolean isTokenExpired(String token) {
        return permissionService.isTokenExpired(token);
    }

    /**
     * @api {post} /login/user  登陆用户
     * @apiDescription 进行用户登陆
     * @apiParam {String} username 用户名（必填）
     * @apiParam {String} password 密码（必填）
     * @apiParam {String} sysName 系统名（必填）
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * {
     * "username":"admin","password":"123456","sysName":"X2"
     * }
     * @apiGroup 任意
     * @apiVersion 1.0.0
     */
    @PostMapping("/login/user")
    public ObjectRestResponse<String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String sysName = body.get("sysName");
        UserInfo jwtUser = permissionService.validate(username, password, sysName);
        if (jwtUser == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "该用户不存在或用户异常或密码错误", false);
        }
        //生成token
        String token = permissionService.createToken(jwtUser);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, token);
    }


    /**
     * @api {get} /getCurrentUser 获得当前用户信息
     * @apiDescription 获得当前用户信息
     * @apiHeader {String} Authorization 用户令牌
     * @apiGroup 任意
     * @apiVersion 1.0.0
     */
    @GetMapping("/getCurrentUser")
    public ObjectRestResponse<UserInfo> getCurrentUser(HttpServletRequest request) throws Exception {
        UserInfo jwtUser = getCurrentUserInfo(request);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, jwtUser);
    }

    /**
     * @api {get} /refreshToken 刷新当前用户token
     * @apiDescription 刷新当前用户token
     * @apiHeader {String} Authorization 用户令牌
     * @apiGroup 任意
     * @apiVersion 1.0.0
     */
    @GetMapping("/refreshToken")
    public ObjectRestResponse<String> refreshToken(HttpServletRequest request) throws Exception {
        UserInfo jwtUser = getCurrentUserInfo(request);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, permissionService.createToken(jwtUser), true);
    }

    /**
     * @api {post} /updatePassword 修改当前用户密码
     * @apiDescription 修改当前用户密码
     * @apiParam {String} oldPassword 原密码（必填）
     * @apiParam {String} newPassword 新密码（必填）
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * {
     * "oldPassword":"123456","newPassword":"xx"
     * }
     * @apiHeader {String} Authorization 用户令牌
     * @apiGroup 任意
     * @apiVersion 1.0.0
     */
    @PostMapping("/updatePassword")
    public ObjectRestResponse<User> updatePassword(@RequestBody Map<String, String> map, HttpServletRequest request) throws Exception {
        UserInfo jwtUser = getCurrentUserInfo(request);
        String username = jwtUser.getUsername();
        String sysName = jwtUser.getSysName();
        String oldPassword = map.get("oldPassword");
        String newPassword = map.get("newPassword");
        if (!MyObjectUtil.noNullOrEmpty(oldPassword) || !MyObjectUtil.noNullOrEmpty(newPassword)) {
            return new ObjectRestResponse(StatusCode.FAIL, "原密码和新密码不能为空", false);
        }
        if (newPassword.equals(oldPassword)) {
            return new ObjectRestResponse(StatusCode.FAIL, "新密码不能与原密码相同", false);
        }
        if (permissionService.validate(username, oldPassword, sysName) == null) {
            return new ObjectRestResponse(StatusCode.FAIL, "原密码错误", false);
        }
        User user = new User();
        Integer id = jwtUser.getId();
        user.setId(id);
        user.setPassword(newPassword);
        // user.setPassword(MD5Util.convertMD5(MD5Util.string2MD5(newPassword)));
        int index = userBiz.updateSelectiveById(user);
        if (index > 0) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, userBiz.selectById(id));
        }
        return new ObjectRestResponse(StatusCode.FAIL, "修改密码失败", false);
    }

    /**
     * @api {post} /uploadUserPhoto 上传或更换当前用户头像
     * @apiDescription 上传或更换当前用户头像
     * @apiParam {MultipartFile} photoFile 用户头像文件（必填）
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 任意
     * @apiVersion 1.0.0
     */
    @PostMapping("/uploadUserPhoto")
    public ObjectRestResponse<String> uploadUserPhoto(MultipartFile photoFile, HttpServletRequest request) throws Exception {
        UserInfo jwtUser = getCurrentUserInfo(request);
        String photo = userBiz.uploadUserPhoto(photoFile, jwtUser.getId());
        if (photo != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, photo);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {post} /uploadAnything  上传任意文件到服务器文件存入地文件表表名文件夹下,返回url(文件名前加上传人姓名)
     * @apiDescription 上传任意文件到服务器文件存入地文件表表名文件夹下, 返回url(文件名前加上传人姓名)
     * @apiParam {MultipartFile} file 文件（必填）
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 任意
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/uploadAnything")
    public ObjectRestResponse<String> uploadAnything(MultipartFile file, HttpServletRequest request) throws Exception {
        UserInfo jwtUser = getCurrentUserInfo(request);
        String url = fileInfoBiz.uploadFile(file, CommonConstants.FORM_NAME_20, jwtUser.getName());
        if (url != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, url);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * 获得当前用户信息
     */
    private UserInfo getCurrentUserInfo(HttpServletRequest request) throws Exception {
        String token = request.getHeader(userAuthConfig.getTokenHeader());
        UserInfo jwtUser = permissionService.getUserInfo(token);
        if (jwtUser == null) {
            throw new Exception("当前用户不存在");
        }
        return jwtUser;
    }
}
