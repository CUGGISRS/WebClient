package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BetweenEnterpriseUser;
import com.github.wxiaoqi.security.zs.sys.entity.Enterprise;
import com.github.wxiaoqi.security.zs.sys.mapper.EnterpriseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业
 */
@Service
public class EnterpriseBiz extends BaseBiz<EnterpriseMapper, Enterprise> {
    @Autowired
    private BetweenEnterpriseUserBiz betweenEnterpriseUserBiz;

    /**
     * 通过用户id获得企业信息
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public Enterprise getOneByUserId(Integer userId) throws Exception {
        BetweenEnterpriseUser beu = betweenEnterpriseUserBiz.getOneByUserId(userId);
        if (beu != null) {
            return selectById(beu.getEnterpriseId());
        }
        return null;
    }


}
