package com.zd.earth.manage.common;

import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;
import tk.mybatis.mapper.common.ids.SelectByIdsMapper;

/**
 * 继承多个通用mapper,获得它们操作数据库的方法
 * @param <T>
 */
public interface CommonMapper<T> extends Mapper<T>, IdsMapper<T>, MySqlMapper<T>, SelectByIdsMapper<T>,UpdateBatchByPrimaryKeySelectiveMapper<T>,InsertBatchAllColsMapper<T> {
}
