package com.zd.earth.manage.common;

import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

/**
 * 批量更新接口
 */
public interface UpdateBatchByPrimaryKeySelectiveMapper<T> {
    /**
     * 根据Example条件批量更新实体`record`包含的不是null的属性值
     *
     * @return
     */
    @UpdateProvider(type = BatchExampleProvider.class, method = "dynamicSQL")
    int updateBatchByPrimaryKeySelective(List<? extends T> recordList);
}
