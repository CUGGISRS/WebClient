package com.github.wxiaoqi.security.jd.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.jd.sys.entity.BetweenDeptUser;
import com.github.wxiaoqi.security.jd.sys.feign.Service.IUserService;
import com.github.wxiaoqi.security.jd.sys.mapper.BetweenDeptUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门-用户
 */
@Service
public class BetweenDeptUserBiz extends BaseBiz<BetweenDeptUserMapper, BetweenDeptUser> {
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
     * 通过部门id获得数据
     */
    public List<BetweenDeptUser> getByDId(Integer dId) {
        if (dId != null) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("deptId", dId);
            return getByToMap(andToMap);
        }
        return null;
    }

    /**
     * 通过部门id集合获得数据
     */
    public List<BetweenDeptUser> getByDIds(List<Integer> dIds) {
        String propertyName = "deptId";
        return getByInFiledMayToMap(dIds, propertyName, null);
    }


    /**
     * 通过集合删除、部门-用户、用户(带文件)
     */
    public void delBDUAndUser(List<BetweenDeptUser> obj, List<String> oldUrls) throws Exception {
        int sum = MyObjectUtil.iterableCount(obj);
        if (sum > 0) {
            List<Integer> bduIds = new ArrayList<>();
            List<Integer> userIds = new ArrayList<>();
            for (int i = 0; i < sum; i++) {
                BetweenDeptUser item = obj.get(i);
                Integer id = item.getId();
                bduIds.add(id);
                Integer userId = item.getUserId();
                userIds.add(userId);
            }

            int index = batchDeleteByIds(bduIds);
            if (sum != index) {
                throw new Exception("批量删除部门-用户失败");
            }

            //删除用户,用户文件存入集合
            userService.delData(userIds,oldUrls);
        }
    }

}
