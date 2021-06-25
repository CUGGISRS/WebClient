package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.com.sys.entity.User;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenEnterpriseUser;
import com.github.wxiaoqi.security.zs.sys.feign.service.IUserService;
import com.github.wxiaoqi.security.zs.sys.mapper.BetweenEnterpriseUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 企业-用户
 */
@Service
public class BetweenEnterpriseUserBiz extends BaseBiz<BetweenEnterpriseUserMapper, BetweenEnterpriseUser> {
    @Autowired
    private IUserService userService;

    /**
     * 通过用户id删除数据
     */
    public int delByUserId(Integer userId) {
        if (userId != null) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("userId", userId);
            return delByToMap(toMap);
        }
        return 0;
    }

    /**
     * 通过用户id集合删除数据
     */
    public int delByUserIds(List<Integer> userIds) {
        String inField = "userId";
        return delByInFiledMayToMap(userIds, inField, null);
    }


    /**
     * 通过企业id获得数据
     */
    public List<BetweenEnterpriseUser> getByEId(Integer eId) {
        if (eId != null) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("enterpriseId", eId);
            return getByToMap(andToMap);
        }
        return null;
    }

    /**
     * 通过用户id获得一条数据
     */
    public BetweenEnterpriseUser getOneByUserId(Integer userId) throws Exception {
        if (userId != null) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("userId", userId);
            List<BetweenEnterpriseUser> list = getByToMap(andToMap);
            int sum = MyObjectUtil.iterableCount(list);
            if (sum == 1) {
                return list.get(0);
            } else if (sum > 1) {
                throw new Exception("一个用户不能属于多个企业");
            }
        }
        return null;
    }


    /**
     * 通过企业id集合获得数据
     */
    public List<BetweenEnterpriseUser> getByEIds(List<Integer> eIds) {
        String propertyName = "enterpriseId";
        return getByInFiledMayToMap(eIds, propertyName, null);
    }

    /**
     * 通过企业id获得(某一类型的)用户集合
     */
    public List<User> getUsersByEId(Integer eId, Integer userType) {

        List<BetweenEnterpriseUser> betweenEnterpriseUsers = getByEId(eId);
        if (betweenEnterpriseUsers != null) {
            List<Integer> userIds = betweenEnterpriseUsers.stream()
                    .map(BetweenEnterpriseUser::getUserId).distinct().collect(Collectors.toList());
            List<User> users = userService.selectByIds(userIds);
            if (users != null) {
                if (MyObjectUtil.noNullOrEmpty(userType)) {
                    users = users.stream().filter(user -> userType.equals(user.getType())).collect(Collectors.toList());
                }
                if (MyObjectUtil.iterableCount(users) > 0) {
                    return users;
                }
            }
        }
        return null;
    }


    /**
     * 通过集合删除、企业-用户、用户(用户文件存入集合)
     */
    public void delBEUAndUser(List<BetweenEnterpriseUser> obj, List<String> oldUrls) throws Exception {
        int sum = MyObjectUtil.iterableCount(obj);
        if (sum > 0) {
            List<Integer> beuIds = obj.stream().map(BetweenEnterpriseUser::getId)
                    .collect(Collectors.toList());
            int index = batchDeleteByIds(beuIds);
            if (sum != index) {
                throw new Exception("批量删除企业-用户失败");
            }
            List<Integer> userIds = obj.stream().map(BetweenEnterpriseUser::getUserId)
                    .collect(Collectors.toList());
            //删除用户,用户文件存入集合
            userService.delData(userIds,oldUrls);
        }
    }


}
