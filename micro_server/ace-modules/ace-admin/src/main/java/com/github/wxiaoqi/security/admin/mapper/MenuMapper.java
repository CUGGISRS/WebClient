package com.github.wxiaoqi.security.admin.mapper;

import com.github.wxiaoqi.security.admin.entity.Menu;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface MenuMapper extends CommonMapper<Menu> {
    public List<Menu> selectMenuByAuthorityId(@Param("authorityId") String authorityId,@Param("authorityType") String authorityType);

    /**
     * 根据用户和组的权限关系查找用户可访问菜单
     * @param userId
     * @return
     */
    public List<Menu> selectAuthorityMenuByUserId (@Param("userId") int userId);

    /**
     * 根据用户和组的权限关系查找用户可访问的系统
     * @param userId
     * @return
     */
    public List<Menu> selectAuthoritySystemByUserId (@Param("userId") int userId);
}
