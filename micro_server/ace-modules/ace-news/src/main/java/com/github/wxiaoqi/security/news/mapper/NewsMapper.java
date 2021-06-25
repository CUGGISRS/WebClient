package com.github.wxiaoqi.security.news.mapper;

import com.github.wxiaoqi.security.common.mapper.CommonMapper;
import com.github.wxiaoqi.security.news.entity.News;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface NewsMapper extends CommonMapper<News> {
    public List<News> selectSimple();
    public List<News> selectSimpleByType(@Param("type") String type);
}
