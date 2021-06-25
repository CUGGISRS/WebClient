package com.github.wxiaoqi.security.dzsw.sys.rest;

import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.dzsw.sys.biz.UserBiz;
import com.github.wxiaoqi.security.dzsw.sys.entity.User;
import com.github.wxiaoqi.security.dzsw.sys.service.SendSmsService;
import com.github.wxiaoqi.security.dzsw.sys.utils.sms.AssertUtil;
import com.github.wxiaoqi.security.dzsw.sys.utils.sms.RedisUtil;
import com.github.wxiaoqi.security.dzsw.sys.utils.sms.SmsUtil;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import com.github.wxiaoqi.security.dzsw.sys.vo.SendSmsDto;
import com.github.wxiaoqi.security.dzsw.sys.vo.UserVo;
import com.github.wxiaoqi.security.dzsw.sys.vo.VerifyCodeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 用户相关
 * @author: gsy
 * @create: 2020-09-11 11:53
 **/
@RequestMapping("user")
@RestController
@Api(tags = {"门户用户相关接口"})
public class UserController {

    @Resource
    private UserBiz userBiz;

    //验证码失效时间
    @Value("${aliyun.invalidtime}")
    private String INVALID_TIME;

    @Resource
    private SendSmsService sendSmsService;

    @Resource
    private RedisUtil redisUtil;

    @GetMapping("/checkImgCode")
    @ApiOperation(value = "校验图片验证码")
    public ObjectRestResponse<T> checkImgCode(@RequestParam String code, @RequestParam String mobile) {
        //1.取出redis验证码
        String redisCode = (String) redisUtil.getCacheObject("verifyCode");
        //2.校验用户传入的验证码
        if (redisCode.equals(code)) {
            //1.删除该验证码
            redisUtil.delete("verifyCode");
            //2.发送手机短信
            SendSmsDto sendSmsDto = new SendSmsDto();
            sendSmsDto.setMobile(mobile);
            //注册类型：2
            sendSmsDto.setType(2);
            boolean b = sendSmsService.sendSms(sendSmsDto);
            if (b) {
                return new ObjectRestResponse<>(StatusCode.SUCCESS);
            }
        }
        return new ObjectRestResponse<>(StatusCode.SMS_ERROR);
    }

    @PostMapping("send")
    @ApiOperation("发送短信验证码")
    public ObjectRestResponse<T> send(@RequestBody SendSmsDto sendSmsDto) {
        boolean b = sendSmsService.sendSms(sendSmsDto);
        if (b) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL);
    }

    @PostMapping("/reg")
    @ApiOperation("注册新用户")
    public ObjectRestResponse<T> regUser(@RequestBody @Validated UserVo user) {
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto();
        verifyCodeDto.setMobile(user.getPhone());
        verifyCodeDto.setCode(user.getCode());
        verifyCodeDto.setType(2);        //注册类型：2
        //校验验证码是否正确
        sendSmsService.verifyCode(verifyCodeDto);
        return userBiz.regUser(user);
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(value = "删除用户")
    public ObjectRestResponse deleteUserById(@PathVariable Integer id) {
        // return userBiz.deleteUserById(id);
        return userBiz.delById(id);
    }

    @PostMapping("/delete/List")
    @ApiOperation(value = "批量删除用户", notes = "传入集合类型的id，例:[1,2,3,4]")
    public ObjectRestResponse deleteUserByIdList(@RequestBody List<Integer> idList) {
        return userBiz.delByIds(idList);
    }

    @PutMapping("/update")
    @ApiOperation(value = "修改用户")
    public ObjectRestResponse<T> updateUser(@RequestParam Integer id, String sysName, String password, String phone, String photo, String status, Integer isVerify, Integer isDeleted) {
        User user = new User();
        user.setId(id);
        user.setSysName(sysName);
        user.setPassword(password);
        user.setPhone(phone);
        user.setPhoto(photo);
        user.setStatus(status);
        user.setIsVerify(isVerify);
        user.setIsDeleted(isDeleted);
        return userBiz.updateUser(user);
    }

    @PutMapping("/updatePassword")
    @ApiOperation(value = "修改密码")
    public ObjectRestResponse<T> updatePassword(@RequestParam String token, @RequestParam String oldPassword, @RequestParam String newPassword) {
        if (StringHelper.isNullOrEmpty(oldPassword) || StringHelper.isNullOrEmpty(newPassword)) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "旧密码和新密码不能为空");
        }
        if (oldPassword.equals(newPassword)) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "旧密码和新密码不能相同");
        }
        JwtUser jwtUser = userBiz.getUserInfoByToken(token);
        if (jwtUser == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "令牌解析失败");
        }
        Integer userId = jwtUser.getId();
        User user = userBiz.selectById(userId);
        String pwd = user.getPassword();
        if (oldPassword.equals(pwd)) {
            return userBiz.updatePassword(userId, newPassword);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL, "密码错误");
    }


    @PostMapping("/uploadUserPhoto")
    @ApiOperation(value = "上传或更换当前用户头像")
    public ObjectRestResponse<String> uploadUserPhoto(MultipartFile photoFile, String token) {
        JwtUser jwtUser = userBiz.getUserInfoByToken(token);
        if (jwtUser == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "令牌解析失败");
        }
        String photo = userBiz.uploadUserPhoto(photoFile, jwtUser.getId());
        if (photo != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, photo);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL, false);
    }

    @DeleteMapping("/delUserPhoto")
    @ApiOperation(value = "删除当前用户头像")
    public ObjectRestResponse delUserPhoto(String token) {
        JwtUser jwtUser = userBiz.getUserInfoByToken(token);
        if (jwtUser == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "令牌解析失败");
        }
        boolean result = userBiz.delUserPhoto(jwtUser.getId());
        if (result) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL, false);
    }

    @GetMapping("/list")
    @ApiOperation(value = "分页获取用户列表")
    public ObjectRestResponse<User> list(@RequestParam Map<String, Object> params) {
        return userBiz.list(params);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiOperation(value = "登录，验证用户名和密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "map", value = "{\"username\":\"string\",\"password\":\"string\"}", required = true, dataType = "map", paramType = "body"),
    })
    public ObjectRestResponse<String> login(@RequestBody Map<String, String> map) {
//        return userBiz.login(map);
        return userBiz.validate(map);
    }


    @GetMapping("/getCurrentUser")
    @ApiOperation(value = "获取当前用户")
    @ResponseBody
    public ObjectRestResponse<JwtUser> getCurrentUser() {
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, JwtUser.getCurUser());
    }

    @ApiOperation(value = "模拟发送短信")
    @PostMapping("sendFake")
    public ObjectRestResponse<T> sendFake(@RequestBody SendSmsDto sendSmsDto) {
        //默认五分钟
        String code = SmsUtil.getCode();
        boolean b = redisUtil.setCacheObject(SmsUtil.map.get(sendSmsDto.getType()) + sendSmsDto.getMobile(), code, 60 * Integer.parseInt(INVALID_TIME));
        if (b) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL, false);
    }

    @ApiOperation(value = "模拟验证短信")
    @PostMapping("verifyFake")
    public ObjectRestResponse<T> verifyFake(@RequestBody VerifyCodeDto verifyCodeDto) {

        String mobile = verifyCodeDto.getMobile();
        String s = SmsUtil.map.get(verifyCodeDto.getType());
        String redisCode = (String) redisUtil.getCacheObject(s + mobile);
        //断言工具
        AssertUtil.isNull(redisCode, StatusCode.FAIL);
        //验证验证码真假
        if (redisCode.equals(verifyCodeDto.getCode())) {
            //redis删除验证码
            redisUtil.delete(SmsUtil.map.get(verifyCodeDto.getType()) + verifyCodeDto.getMobile());
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
        } else {
            return new ObjectRestResponse<>(StatusCode.SMS_ERROR);
        }
    }

    @ApiOperation(value = "获取过期时间")
    @PostMapping("getKeyTime")
    public String getKeyTime(@RequestParam String key) {
        long time = redisUtil.getTime(key);
        return time + "秒";
    }

    @PostMapping("getValueByKey")
    public Object getValueByKey(@RequestParam String key) {
        //获得缓存的基本对象
        Object cacheObject = redisUtil.getCacheObject(key);
        return cacheObject;
    }

    @PostMapping("setCacheObject")
    public boolean setCacheObject(@RequestParam String key, @RequestParam String value) {
        //缓存的键值对
        Long expire = Long.valueOf(-1);
        boolean b = redisUtil.setCacheObject(key, value, expire);
        return b;
    }

    @ApiOperation(value = "解析token中的当前用户(全部信息，不查用户表)")
    @PostMapping("getUserInfoByToken")
    public JwtUser getUserInfoByToken(@RequestParam String token) {
        return userBiz.getUserInfoByToken(token);
    }

    @ApiOperation(value = "通过token获得当前用户(部分信息,不包含密码等重要信息)")
    @PostMapping("getUserInfoByTokenPart")
    public ObjectRestResponse<Map<String, Object>> getUserInfoByTokenPart(@RequestParam String token) {
        JwtUser jwtUser = userBiz.getUserInfoByToken(token);
        if (jwtUser == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "令牌解析失败");
        }
        Integer userId = jwtUser.getId();
        return userBiz.getUserInfoByTokenPart(userId);
    }

    @ApiOperation(value = "注册：校验用户名是否存在")
    @PostMapping("checkUsername")
    public HashMap<String, Boolean> checkUsername(@RequestParam String username) {
        return userBiz.checkUsername(username);
    }

    @ApiOperation(value = "重置用户初始密码")
    @PostMapping("resetPWD")
    public ObjectRestResponse resetPWD(@RequestParam int id, @RequestParam String newPwd) {
        return userBiz.updatePassword(id, newPwd);
    }

    @ApiOperation(value = "忘记密码，重置密码")
    @PostMapping("forget")
    public ObjectRestResponse forgetPWD(@RequestParam String code, @RequestParam int codeType, @RequestParam String phone, @RequestParam String newPwd) {
        VerifyCodeDto verifyCodeDto = new VerifyCodeDto();
        verifyCodeDto.setMobile(phone);
        verifyCodeDto.setCode(code);
        verifyCodeDto.setType(codeType);        //忘记密码  ： 3
        //校验忘记密码验证码是否正确
        sendSmsService.verifyCode(verifyCodeDto);
        return userBiz.forget(phone, newPwd);
    }

    @ApiOperation(value = "忘记密码输入手机号获取验证码：校验手机号是否有对应账户存在")
    @PostMapping("checkPhone")
    public HashMap<String, Boolean> checkPhone(@RequestParam String phone) {
        return userBiz.checkPhone(phone);
    }
}
