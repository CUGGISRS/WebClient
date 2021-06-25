package com.github.wxiaoqi.security.consultation.entity;

import lombok.Data;

import javax.persistence.Transient;

@Data
public class BiConsulation_DtConsulation extends BiConsultation{

    @Transient
    DtConsultation dtConsultation;
}
