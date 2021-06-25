package com.github.wxiaoqi.security.consultation.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.consultation.biz.UserBiz;
import com.github.wxiaoqi.security.consultation.entity.BiExpert;
import com.github.wxiaoqi.security.consultation.entity.BiexpertUser;
import com.github.wxiaoqi.security.consultation.entity.User;
import tk.mybatis.mapper.common.Mapper;


public interface BiExpertMapper extends CommonMapper<BiExpert> {
    //通过专家id 查询单一专家的信息，附有专家账号
    public BiexpertUser selectOneExpertUser(int id);

    //通过id删除专家，并且删除账号
    public Integer deleteOneExpert(int id);

}
