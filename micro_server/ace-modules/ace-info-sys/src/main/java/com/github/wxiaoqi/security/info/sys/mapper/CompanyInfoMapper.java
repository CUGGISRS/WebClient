package com.github.wxiaoqi.security.info.sys.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.info.sys.entity.CompanyAndFile;
import com.github.wxiaoqi.security.info.sys.entity.CompanyInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CompanyInfoMapper extends Mapper<CompanyInfo>, CommonMapper<CompanyInfo> {
    List<CompanyAndFile> listCompany(Integer page, Integer size, String companyName);
}
