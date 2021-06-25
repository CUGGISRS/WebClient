package com.github.wxiaoqi.security.auth.mapper;

import com.github.wxiaoqi.security.auth.entity.ClientService;
import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import tk.mybatis.mapper.common.Mapper;

public interface ClientServiceMapper extends CommonMapper<ClientService> {
    void deleteByServiceId(int id);
}
