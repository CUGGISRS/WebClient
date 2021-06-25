package com.zd.earth.manage.mapper;

import com.zd.earth.manage.common.CommonMapper;
import com.zd.earth.manage.entity.FlyPath;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlyPathMapper extends CommonMapper<FlyPath> {

    public List<FlyPath> getLinkAll();

    public List<FlyPath> pageLink(@Param("page")Integer page,
                                  @Param("limit")Integer limit,
                                  @Param("orderBy")String orderBy);

    public int getCountAll();
}
