package com.github.wxiaoqi.security.consultation.rest;


import com.github.wxiaoqi.security.auth.client.jwt.UserAuthUtil;
import com.github.wxiaoqi.security.common.exception.auth.UserTokenException;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.consultation.biz.BiExpertBiz;
import com.github.wxiaoqi.security.consultation.biz.DtConsultationBiz;
import com.github.wxiaoqi.security.consultation.entity.BiExpert;
import com.github.wxiaoqi.security.consultation.entity.DtConsultation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("consultation/dt")
public class DtConsultationController extends BaseController<DtConsultationBiz, DtConsultation> {

    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private BiExpertBiz biExpertBiz;

    @ResponseBody
    @Override
    public ObjectRestResponse<DtConsultation> add(@RequestBody DtConsultation dtConsultation) throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(userAuthUtil.getInfoFromToken(dtConsultation.getToken()).getId());
        } catch (NumberFormatException e) {
            throw new UserTokenException("User token is null or empty!");
        }
        dtConsultation.setPublishid(id);
        dtConsultation.setPublishname(userAuthUtil.getInfoFromToken(dtConsultation.getToken()).getUniqueName());
        BiExpert biExpert = biExpertBiz.validate(id);
        if (biExpert != null) {
            dtConsultation.setPublishid(biExpert.getId());
            dtConsultation.setPublishname(biExpert.getName());
        }
        return super.add(dtConsultation);
    }

    @ResponseBody
    @Override
    public TableResultResponse<DtConsultation> list(@RequestParam Map<String, Object> params) throws Exception {
        if (params.containsKey("token")) {
            int id = 0;
            try {
                id = Integer.parseInt(userAuthUtil.getInfoFromToken(params.get("token").toString()).getId());
            } catch (NumberFormatException e) {
                throw new UserTokenException("User token is null or empty!");
            }
            BiExpert biExpert = biExpertBiz.validate(id);
            if (biExpert != null) {
                id = biExpert.getId();
            }
            if(id != 0){
                params.put("publishid", String.valueOf(id));
            }
            params.remove("token");
        }
        return super.list(params);
    }

    //根据consulationId查询详细问答
    @RequestMapping(value = "/getDetailAsk",method = RequestMethod.GET)
    @ResponseBody
    public ObjectRestResponse getDetailAskById(@RequestParam Map<Object,String> params){
if (params.containsKey("consultationid")){
    String s = params.get("consultationid");
    List listByFiled = baseBiz.getListByFiled(s,"consultationid");
    if (listByFiled.size()==0){
        return new ObjectRestResponse(StatusCode.FAIL,"不存在");
    }
    return new ObjectRestResponse(StatusCode.SUCCESS,listByFiled,true);
}
    return new ObjectRestResponse(StatusCode.REQUEST_PARAM_ERROR);
    }
}
