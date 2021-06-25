package com.github.wxiaoqi.security.consultation.rest;


import com.github.wxiaoqi.security.auth.client.jwt.UserAuthUtil;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.exception.auth.UserTokenException;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.consultation.biz.BiConsultationBiz;
import com.github.wxiaoqi.security.consultation.biz.BiExpertBiz;
import com.github.wxiaoqi.security.consultation.entity.BiConsultation;
import com.github.wxiaoqi.security.consultation.entity.BiExpert;
import com.github.wxiaoqi.security.consultation.entity.DtConsultation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("consultation")
public class BiConsultationController extends BaseController<BiConsultationBiz, BiConsultation> {

    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private BiExpertBiz biExpertBiz;

    @Autowired
    private BiConsultationBiz biConsultationBiz;

    protected BaseBiz baseBiz;

    @ResponseBody
    @Override
    public ObjectRestResponse<BiConsultation> add(@RequestBody BiConsultation biConsultation) throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(userAuthUtil.getInfoFromToken(biConsultation.getToken()).getId());
        } catch (NumberFormatException e) {
            throw new UserTokenException("User token is null or empty!");
        }
        biConsultation.setUserid(id);
        biConsultation.setUsername(userAuthUtil.getInfoFromToken(biConsultation.getToken()).getUniqueName());
        return super.add(biConsultation);
    }

    @ResponseBody
    @Override
    public TableResultResponse<BiConsultation> list(@RequestParam Map<String, Object> params) throws Exception {
        if (params.containsKey("token")) {
            params.put("userid", userAuthUtil.getInfoFromToken(params.get("token").toString()).getId());
            params.remove("token");
        }
        return super.list(params);
    }

    @RequestMapping(value = "/noread", method = RequestMethod.GET)
    @ResponseBody
    public TableResultResponse<?> getUserInfo(String token) throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(userAuthUtil.getInfoFromToken(token).getId());
        } catch (NumberFormatException e) {
            throw new UserTokenException("User token is null or empty!");
        }
        BiExpert biExpert = biExpertBiz.validate(id);
        if (biExpert != null) {
            id = biExpert.getId();
        }
        class NoReadInfo {
            private Integer id;

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }
        }
        ArrayList<NoReadInfo> lsNoRead = new ArrayList<NoReadInfo>();

        return new TableResultResponse<NoReadInfo>(lsNoRead.size(), lsNoRead);
    }
    //根据专家id查问答列表
    @GetMapping("/getAskList/{id}")
    @ResponseBody
    public ObjectRestResponse aksListByExpertId(@PathVariable("id") int id){
        return biConsultationBiz.aksListByExpertId(id);
    }
}
