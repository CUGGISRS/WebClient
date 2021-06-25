package com.github.wxiaoqi.security.admin.mapper;

import com.github.wxiaoqi.security.admin.entity.User;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserMapper extends CommonMapper<User> {
    public User getLinkOneById(Integer id);
    public List<User> getLinkListByPage(Map<String,Object> params);
    public  Integer getLinkCountByPage(Map<String,Object> params);

    public List<User> selectMemberByGroupId(@Param("groupId") int groupId);
    public List<User> selectLeaderByGroupId(@Param("groupId") int groupId);

    /**
     * 通过username获取该部门人员信息
     * @param username
     * @return
     */
    public List<User> getDeptPersonByUsername(String username);
    /**
     * 通过username获取该公司人员信息
     * @param username
     * @return
     */
    public List<User> getEnterprisePersonByUsername(String username);


}
