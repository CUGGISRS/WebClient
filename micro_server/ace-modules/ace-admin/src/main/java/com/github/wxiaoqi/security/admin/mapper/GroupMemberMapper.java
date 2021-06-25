package com.github.wxiaoqi.security.admin.mapper;

import com.github.wxiaoqi.security.admin.entity.GroupMember;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface GroupMemberMapper extends CommonMapper<GroupMember> {
    public int deleteByGroupId (int groupId);
    public int insertByGroupIdAndUserId (@Param("groupId") int groupId, @Param("userId") int userId);
}
