package com.github.wxiaoqi.security.com.sys.feign.service;

import com.github.wxiaoqi.security.com.sys.feign.IBetweenEnterpriseUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业_用户服务类
 */
@Service
public class IBetweenEnterpriseUserService {
    @Autowired
    private IBetweenEnterpriseUser iBetweenEnterpriseUser;

    /**
     * 通过用户id删除数据
     */
    public int delByUserId(Integer userId) {
        return iBetweenEnterpriseUser.delByUserId(userId);
    }

    /**
     * 通过用户id集合删除数据
     */
    public int delByUserIds(List<Integer> userIds) {
        if (userIds == null) {
            return 0;
        }
        return iBetweenEnterpriseUser.delByUserIds(userIds);
    }
}
