package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.zs.sys.entity.EnterpriseQualification;
import com.github.wxiaoqi.security.zs.sys.mapper.EnterpriseQualificationMapper;
import org.springframework.stereotype.Service;

/**
 * 企业资质
 */
@Service
public class EnterpriseQualificationBiz extends BaseBiz<EnterpriseQualificationMapper, EnterpriseQualification> {
    /**
     * 修改某一企业的数据
     */
    public int updateByEId(EnterpriseQualification obj, Integer eId) {
        if (eId == null) {
            return 0;
        }
        String fieldName = "enterpriseId";
        return updateByFiled(obj, eId, fieldName);
    }
}
