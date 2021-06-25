package com.github.wxiaoqi.security.common.mapper;

import com.github.wxiaoqi.security.common.mapper.provider.BatchExampleProvider;
import org.apache.ibatis.annotations.InsertProvider;

import java.util.List;

/**
 * 自定义mapper方法，主要是解决批量插入问题(主键字段也插入)
 */
public interface InsertBatchAllColsMapper<T> {

    /**
     * 批量插入数据库，所有字段都插入，包括主键，不会自动添加自增主键到集合中
     */
  //  @Options(useGeneratedKeys = true, keyProperty = "id") //此处注释掉，禁止自动添加自增主键到集合中
    @InsertProvider(type = BatchExampleProvider.class, method = "dynamicSQL")
    int insertListUseAllCols(List<T> recordList);
}
