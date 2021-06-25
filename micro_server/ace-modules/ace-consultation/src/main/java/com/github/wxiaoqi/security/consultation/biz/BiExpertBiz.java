package com.github.wxiaoqi.security.consultation.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.consultation.entity.BiExpert;
import com.github.wxiaoqi.security.consultation.entity.BiexpertUser;
import com.github.wxiaoqi.security.consultation.entity.User;
import com.github.wxiaoqi.security.consultation.mapper.BiExpertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;


@Service
@Transactional(rollbackFor = Exception.class)
public class BiExpertBiz extends BaseBiz<BiExpertMapper, BiExpert> {

    @Autowired
    private UserBiz userBiz;

    @Autowired(required=false)
    private BiExpertMapper biExpertMapper;

    public BiExpert validate(int userId){

        User user = userBiz.selectById(userId);

        BiExpert result = null;
        if (user.getAttr1() != null && !user.getAttr1().isEmpty()) {
            int expertid = 0;
            try {
                expertid = Integer.parseInt(user.getAttr1());
            } catch (NumberFormatException e) {
            }
            if (expertid != 0) {
                result = super.selectById(expertid);
            }
        }
        return result;
    }

    //查询单个专家的用户名密码
    public BiexpertUser findExpertUsername(int id){
        BiexpertUser biexpertUser = biExpertMapper.selectOneExpertUser(id);
        return biexpertUser;
    }

    //删除单个专家，并且删除用户名和密码
    public ObjectRestResponse deleteOneExpert(int id) {

        try {
            biExpertMapper.deleteOneExpert(id);
            return new ObjectRestResponse(StatusCode.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
