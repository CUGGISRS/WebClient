package com.github.wxiaoqi.security.com.sys.feign.service;

import com.github.wxiaoqi.security.com.sys.feign.IBetweenDeptUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门_用户服务类
 */
@Service
public class IBetweenDeptUserService {

    @Autowired
    private IBetweenDeptUser iBetweenDeptUser;

    /**
     * 通过用户id删除数据
     */
    public int delByUserId(Integer userId) {
        return iBetweenDeptUser.delByUserId(userId);
    }

    /**
     * 通过用户id集合删除数据
     */
    public int delByUserIds(List<Integer> userIds) {
        if (userIds == null) {
            return 0;
        }
        return iBetweenDeptUser.delByUserIds(userIds);
    }
}
