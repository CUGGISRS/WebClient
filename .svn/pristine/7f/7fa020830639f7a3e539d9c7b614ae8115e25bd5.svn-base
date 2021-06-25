package com.github.wxiaoqi.security.consultation.rest;


import com.alibaba.fastjson.JSON;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.consultation.biz.DtExpertdutyBiz;
import com.github.wxiaoqi.security.consultation.entity.DtExpertduty;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("expertduty")
public class DtExpertdutyController extends BaseController<DtExpertdutyBiz, DtExpertduty> {

    @RequestMapping(value = "/month", method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse<List<DtExpertdutyBiz.ExpertdutyInDay>> selectByYearMonth(@RequestParam int year, @RequestParam int month) {
        return baseBiz.selectByYearMonth(year, month, -1);
    }


    @PostMapping("/day")
    //cj  保存排班信息
    public ObjectRestResponse<List<DtExpertduty>> saveByYearMonth(@RequestParam int year, @RequestParam int month, @RequestParam int day, @RequestBody List<DtExpertduty> dutyList) {
        return baseBiz.saveByYearMonth(year, month, day, dutyList);
    }

}
