package com.github.wxiaoqi.security.jd.sys.feign.Service;

import com.github.wxiaoqi.security.com.sys.entity.User;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.jd.sys.feign.IUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务类
 */
@Service
public class IUserService {
    @Autowired
    private IUser iUser;


    /**
     * 通过id集合查询数据
     */
    public List<User> selectByIds(List<Integer> ids) {
        //@RequestBody注解参数不能为null
        if (ids == null) {
            return null;
        }
        return iUser.selectByIds(ids);
    }


    /**
     * 通过id集合删除用户数据,原url存入集合后返回
     */
    public Integer delData(List<Integer> ids, List<String> oldUrls) throws Exception {

        int sum = MyObjectUtil.iterableCount(ids);
        List<User> users = iUser.selectByIds(ids);
        if (users != null && users.size() == sum) {
            int index = iUser.delByIds(ids);
            if (sum != index) {
                throw new Exception("批量删除用户失败");
            }
            //原头像文件存入集合
            List<String> photos = users.stream().map(User::getPhoto).collect(Collectors.toList());
            oldUrls.addAll(photos);
            return sum;
        }
        return null;
    }
}
