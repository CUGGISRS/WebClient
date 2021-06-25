package com.github.wxiaoqi.security.gyx.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.gyx.sys.entity.ExpertScheduleInfo;
import com.github.wxiaoqi.security.gyx.sys.mapper.ExpertScheduleInfoMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ExpertScheduleInfoBiz extends BaseBiz<ExpertScheduleInfoMapper, ExpertScheduleInfo> {
}
