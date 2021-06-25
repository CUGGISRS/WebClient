package com.github.wxiaoqi.security.consultation.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.consultation.entity.BiConsultation;
import com.github.wxiaoqi.security.consultation.entity.BiExpert;
import com.github.wxiaoqi.security.consultation.mapper.BiConsultationMapper;
import com.github.wxiaoqi.security.consultation.mapper.BiExpertMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class BiConsultationBiz extends BaseBiz<BiConsultationMapper, BiConsultation> {
    @Autowired(required=false)
    private BiConsultationMapper biConsultationMapper;

    public ObjectRestResponse aksListByExpertId(int id){
        List<BiConsultation> biConsultations = null;
        try {
            biConsultations = biConsultationMapper.aksListByExpertId(id);
            return new ObjectRestResponse(StatusCode.SUCCESS,biConsultations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
