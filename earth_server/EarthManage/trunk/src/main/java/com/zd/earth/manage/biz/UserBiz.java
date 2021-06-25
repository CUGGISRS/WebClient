package com.zd.earth.manage.biz;

import com.zd.earth.manage.common.BaseBiz;
import com.zd.earth.manage.entity.User;
import com.zd.earth.manage.mapper.UserMapper;
import com.zd.earth.manage.util.StringHelper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@Service
public class UserBiz extends BaseBiz<UserMapper, User> {

    /**
     * 通过用户名获得一条数据
     */
    public User getOneByUsername(String username){
        if(StringHelper.noNullOrEmpty(username)){
            return selectOne(new User(username,null));
        }
        return null;
    }

    /**
     * 通过用户名修数据
     */
    public int updByUsername(User obj,String username){
        if(StringHelper.noNullOrEmpty(username)){
            Map<String,Object> toMap=new HashMap<>();
            toMap.put("username",username);
            return updateByFiledMap(obj,toMap);
        }
        return 0;
    }

    /**
     * 判断能否添加(用户名唯一，且用户名、密码非空)
     */
    public String isAdd(User obj) {
        String username=obj.getUsername();
        String pwd=obj.getPassword();
        if(StringHelper.isNullOrEmpty(username)||StringHelper.isNullOrEmpty(pwd)){
            return "用户名、密码必须非空";
        }

       User user=getOneByUsername(username);
        if(user!=null){
            return "用户名已存在";
        }
        return null;
    }
}
