package com.github.wxiaoqi.security.zs.sys.feign;

import com.github.wxiaoqi.security.com.sys.entity.User;
import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 用户远程接口
 */
@FeignClient(value = "ace-com-sys", url = "${feign.comUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("User")
public interface IUser {
    /**
     * 通过id集合查询数据
     */
    @PostMapping("selectByIds")
    public List<User> selectByIds(@RequestBody List<Integer> ids);


    /**
     * 通过id集合删除数据
     */
    @PostMapping("delByIds")
    public Integer delByIds(@RequestBody List<Integer> ids) ;

    /**
     * 创建一个用户
     */
    @PostMapping("createUser")
    public User createUser(@RequestBody User user);

    /**
     * 修改一个用户
     */
    @PutMapping(value = "updateUser")
    public User updateUser(@RequestBody User user);
}
