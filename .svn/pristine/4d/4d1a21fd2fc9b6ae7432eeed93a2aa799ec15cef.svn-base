package com.github.wxiaoqi.security.dzsw.sys.biz;

import cn.hutool.core.util.ObjectUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.FileUtils;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.dzsw.sys.config.WebAppConfigurer;
import com.github.wxiaoqi.security.dzsw.sys.entity.User;
import com.github.wxiaoqi.security.dzsw.sys.mapper.UserMapper;
import com.github.wxiaoqi.security.dzsw.sys.service.PermissionService;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import com.github.wxiaoqi.security.dzsw.sys.utils.sms.RedisUtil;
import com.github.wxiaoqi.security.dzsw.sys.vo.JwtUser;
import com.github.wxiaoqi.security.dzsw.sys.vo.UserVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: gsy
 * @create: 2020-09-11 11:57
 **/
@Service
public class UserBiz extends BaseBiz<UserMapper, User> {

    @Resource
    UserMapper userMapper;

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private WebAppConfigurer webAppConfigurer;
    //用户头像在图片保存路径下归属文件夹
    private String dir = "user";

    /**
     * 上传文件到图片保存路径的某个文件夹下，返回访问url(生成文件名前加内容)
     */
    public String uploadFile(MultipartFile file, String dir, String addFileName) {
        try {
            String imgUrl = webAppConfigurer.getSON_PATH();
            String path = webAppConfigurer.getROOT_PATH() + imgUrl + dir;
            String fileName = FileUtils.uploadFile(file, path, addFileName);
            if (fileName != null) {
                String url = imgUrl + dir + "/" + fileName;
                return url;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过访问url删除图片保存路径下文件
     */
    public void delFileByUrl(String url) {
        if (url != null) {
            url = webAppConfigurer.getROOT_PATH() + url;
            FileUtils.deleteFile(url);
        }
    }

    /**
     * 给某一用户上传或更换用户头像，并修改用户信息,返回头像url
     */
    public String uploadUserPhoto(MultipartFile file, Integer id) {

        User user = selectById(id);
        if (user != null) {
            String photo = uploadFile(file, dir, null);
            if (photo != null) {
                int index = updateSelectiveById(new User(id, photo));
                if (index > 0) {
                    //删除原头像文件
                    delFileByUrl(user.getPhoto());
                    return photo;
                }
                //删除新头像
                delFileByUrl(photo);
            }
        }
        return null;
    }

    /**
     * 删除用户头像
     *
     * @param id
     */
    public boolean delUserPhoto(Integer id) {
        User user = selectById(id);
        if (user != null) {
            String photo = user.getPhoto();
            //修改头像字段
            int index = updateSelectiveById(new User(id, ""));
            if (index > 0) {
                delFileByUrl(photo);
                return true;
            }
        }
        return false;
    }

    //   private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ObjectRestResponse<T> updateUser(User user) {
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i <= 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse<T> regUser(UserVo user) {

        //查询用户名是否存在
        int exist = userMapper.queryExistAccount(user.getAccount());
        if (exist > 0) {
            return new ObjectRestResponse<>(StatusCode.E_10009, false);
        }
        //删除状态&&认证状态
        user.setIsDeleted(0);
        user.setIsVerify(0);
        //是否启用
        user.setStatus("1");
        //注册时间
        user.setRegTime(new Date());
        //设置密码
        //  user.setPassword(encoder.encode(user.getPassword()));
        int insert = userMapper.insert(user);
        if (insert <= 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, false);
    }

    //验证码校验
    /*public boolean verifyCode(UserVo verifyCodeDto){
        String mobile = verifyCodeDto.getPhone();
        String s = SmsUtil.map.get(verifyCodeDto.getSmsType());
        String redisCode = (String) redisUtil.getCacheObject(s+mobile);
        //断言工具
        AssertUtil.isNull(redisCode, StatusCode.FAIL);
        //验证验证码真假
        if (redisCode.equals(verifyCodeDto.getCode())) {
            //redis删除验证码
            redisUtil.delete(SmsUtil.map.get(verifyCodeDto.getSmsType()) + verifyCodeDto.getPhone());
            return true;
        } else {
            return false;
        }
    }*/

    public ObjectRestResponse  delById(Integer id) {
        int i = deleteById(id);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse delByIds(List<Integer> idList) {

        int change = batchDeleteByIds(idList);
        if (change == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse<T> deleteUserById(Integer id) {
        User user = new User();
        user.setId(id);
        user.setIsDeleted(1);
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i <= 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse list(Map<String, Object> params) {
        try {
            Query query = new Query(params);
            Example example = getExampleByQuery(query);
            example.createCriteria().andEqualTo("isDeleted", 0);
            Page<Object> result = PageHelper.startPage(query.getPage(), query.getLimit());
            List<User> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, (int) result.getTotal()));
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex);
        }
    }



    public ObjectRestResponse<T> deleteUserByIdList(Integer[] idList) {

        int change = 0;
        User user = new User();
        user.setIsDeleted(1);
        for (Integer id : idList) {
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("id", id);
            change = userMapper.updateByExampleSelective(user, example);
            change += change;
        }
        if (change == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public ObjectRestResponse login(Map<String, String> map) {
        if (map.containsKey("account")) {
            String username = map.get("account");
            if (map.containsKey("password")) {
                String password = map.get("password");
                User userInfo = new User();
                User user = getUserByUsername(username);
                //  if (encoder.matches(password, user.getPassword())) {
                if (password != null && password.equals(user.getPassword())) {
                    BeanUtils.copyProperties(user, userInfo);
                    userInfo.setId(user.getId());
                    return new ObjectRestResponse(StatusCode.SUCCESS, userInfo);
                } else {
                    return new ObjectRestResponse(StatusCode.FAIL, userInfo);
                }

            }
        }
        return new ObjectRestResponse(StatusCode.FAIL, null);
    }


    @Autowired
    private PermissionService permissionService;

    public ObjectRestResponse validate(Map<String, String> map) {
        String username = map.get("username");
        String password = map.get("password");
        if (username != null && password != null) {
            JwtUser jwtUser = permissionService.validate(username, password);
            if (jwtUser == null) {
                return new ObjectRestResponse<>(StatusCode.FAIL, "该用户不存在或密码错误", false);
            }
            //生成token
            String token = permissionService.createToken(jwtUser);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, token);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


//    public User validate(String username, String password) {
//        User userInfo = new User();
//        User user = getUserByUsername(username);
//        if (encoder.matches(password, user.getPassword())) {
//            BeanUtils.copyProperties(user, userInfo);
//            userInfo.setId(user.getId());
//        }
//        return userInfo;
//    }

    public User getUserByUsername(String username) {
        User user = new User();
        user.setAccount(username);
        return mapper.selectOne(user);
    }

    /**
     * 通过用户名查询系统名下的用户信息
     *
     * @param username
     * @return
     */
    public User selectOneByUsername(String username) {
        if (username != null) {
            Example example = new Example(User.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("account", username);
            List<User> baseUsers = selectByExample(example);
            if (baseUsers != null && baseUsers.size() == 1) {
                return baseUsers.get(0);
            }
        }
        return null;
    }


    public JwtUser getUserInfoByToken(String token) {
        JwtUser userInfo = permissionService.getUserInfo(token);
        return userInfo;
    }

    public ObjectRestResponse<Map<String, Object>> getUserInfoByTokenPart(Integer id) {
        //   JwtUser userInfo = permissionService.getUserInfo(token);
        User userInfo = selectById(id);
        if (userInfo == null) {
            return new ObjectRestResponse<>(StatusCode.FAIL, "当前用户不存在");
        }
        Map<String, Object> map = new HashMap<>();
        map.put("account", userInfo.getAccount());
        map.put("name", userInfo.getName());
        map.put("telephone", userInfo.getPhone());
        map.put("isVerify", userInfo.getIsVerify());
        //头像
        map.put("photo", userInfo.getPhoto());
        return new ObjectRestResponse<>(StatusCode.SUCCESS, map);
    }

    public HashMap<String, Boolean> checkUsername(String username) {
        User user = new User();
        user.setAccount(username);
        User resultUser = userMapper.selectOne(user);
        HashMap<String, Boolean> hashMap = new HashMap();
        if (ObjectUtil.isEmpty(resultUser)) {
            hashMap.put("valid", true);
        } else {
            hashMap.put("valid", false);
        }
        return hashMap;
    }

    public HashMap<String, Boolean> checkPhone(String phone) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("phone", phone);
        List<User> userList = selectByExample(example);
        HashMap<String, Boolean> hashMap = new HashMap();
        if (userList.size() == 0) {
            hashMap.put("exist", false);
        } else {
            hashMap.put("exist", true);
        }
        return hashMap;
    }

    // 修改用户密码
    public ObjectRestResponse<T> updatePassword(Integer id, String newPassword) {
        User user = new User();
        user.setId(id);
        user.setPassword(newPassword);
        // user.setPassword(encoder.encode(newPassword));
        int i = userMapper.updateByPrimaryKeySelective(user);
        if (i > 0) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL);
    }

    // 用户忘记密码，根据手机号更新用户密码
    public ObjectRestResponse forget(String phone, String newPWD) {
        try {
            Example example = new Example(User.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("phone", phone);
            List<User> userList = selectByExample(example);
            if (userList.size() == 0) {
                return new ObjectRestResponse(StatusCode.FAIL, "用户手机号不存在");
            }
            for (User user : userList) {
                //   user.setPassword(encoder.encode(newPWD));
                user.setPassword(newPWD);
                userMapper.updateByPrimaryKeySelective(user);
            }
            return new ObjectRestResponse(StatusCode.SUCCESS);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL);
        }
    }

}
