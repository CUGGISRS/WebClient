package com.github.wxiaoqi.security.admin.biz;

import com.ace.cache.annotation.Cache;
import com.ace.cache.annotation.CacheClear;
import com.github.wxiaoqi.security.admin.entity.User;
import com.github.wxiaoqi.security.admin.mapper.UserMapper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * ${DESCRIPTION}
 *
 * @author wanghaobin
 * @create 2017-06-08 16:23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserBiz extends BaseBiz<UserMapper,User> {


    @Override
    @CacheClear(pre="user{1.username}")
    public int updateSelectiveById(User entity) {
        return super.updateSelectiveById(entity);
    }

    /**
     * 根据用户名获取用户信息
     * @param username
     * @return
     */
    @Cache(key="user{1}")
    public User getUserByUsername(String username){
        User user = new User();
        user.setUsername(username);
        return mapper.selectOne(user);
    }


    /**
     * 通过username获取该部门人员信息
     * @param username
     * @return
     */
    public List<User> getDeptPersonByUsername(String username){
        return mapper.getDeptPersonByUsername(username);
    }

    /**
     * 通过username获取该公司人员信息
     * @param username
     * @return
     */
    public List<User> getEnterprisePersonByUsername(String username){
        return mapper.getEnterprisePersonByUsername(username);
    }

    /**
     * 根据id获得连表用户信息
     * @param id
     * @return
     */
    public User getLinkOneById(Integer id){
        return mapper.getLinkOneById(id);
    }


    /**
     * 连表查询分页展示全部数据
     * @param params
     * @return
     */
    public TableResultResponse<User> getLinkListByPage(Map<String, Object> params){
        int total=mapper.getLinkCountByPage(params);
        mapPutPageLimitTotal(params,total);
        return  new TableResultResponse<>(total,mapper.getLinkListByPage(params));
    }
}
