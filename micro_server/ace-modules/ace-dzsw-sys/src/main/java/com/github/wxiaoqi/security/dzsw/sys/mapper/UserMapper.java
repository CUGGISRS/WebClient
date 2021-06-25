package com.github.wxiaoqi.security.dzsw.sys.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.dzsw.sys.entity.User;

public interface UserMapper extends CommonMapper<User> {
    int queryExistAccount(String account);
}
