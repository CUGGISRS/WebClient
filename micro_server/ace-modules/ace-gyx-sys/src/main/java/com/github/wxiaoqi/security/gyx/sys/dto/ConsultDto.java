package com.github.wxiaoqi.security.gyx.sys.dto;

import com.github.wxiaoqi.security.gyx.sys.entity.ConsultationInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultDto extends ConsultationInfo {
    private String token;
}
