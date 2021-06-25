package com.github.wxiaoqi.security.consultation.rest;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.auth.client.jwt.UserAuthUtil;
import com.github.wxiaoqi.security.common.exception.auth.UserTokenException;
import com.github.wxiaoqi.security.common.msg.BaseResponse;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.consultation.biz.BiConsultationBiz;
import com.github.wxiaoqi.security.consultation.biz.BiExpertBiz;
import com.github.wxiaoqi.security.consultation.biz.UserBiz;
import com.github.wxiaoqi.security.consultation.entity.BiConsultation;
import com.github.wxiaoqi.security.consultation.entity.BiExpert;
import com.github.wxiaoqi.security.consultation.entity.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import java.util.*;

@RestController
@RequestMapping("expert")
@CrossOrigin
public class BiExpertController extends BaseController<BiExpertBiz, BiExpert> {

    @Autowired
    private UserAuthUtil userAuthUtil;

    @Autowired
    private UserBiz userBiz;

    @Autowired
    private BiConsultationBiz biConsultationBiz;

    @Autowired
    private BiExpertBiz biExpertBiz;

    public class ExpertScore {
        @Setter
        @Getter
        double score;
        @Setter
        @Getter
        BiExpert expert;
    }
    //通过id查询专家信息 附有账号
    @GetMapping("/oneUsername/{id}")
    @ResponseBody
    public ObjectRestResponse oneUsername(@PathVariable("id") int id){
         return new ObjectRestResponse<>(StatusCode.SUCCESS,biExpertBiz.findExpertUsername(id));
    }
    //id删除专家信息，附带账户信息
    @RequestMapping(value = "/delete/{id}",method = RequestMethod.DELETE)
public ObjectRestResponse deleteExpertAndUser(@PathVariable("id") int id ){

        return  biExpertBiz.deleteOneExpert(id);
    }

    @CrossOrigin
    @ResponseBody
    @Override
    public ObjectRestResponse<BiExpert> add(@RequestBody BiExpert biExpert) throws Exception {
        biExpert.setId(null);
        baseBiz.insertSelective(biExpert);

        User user = new User();
        user.setUsername(biExpert.getUsername());
        user.setPassword(biExpert.getPassword());
        user.setName(biExpert.getName());
        user.setMobilePhone(biExpert.getTelephone());
        user.setAttr1(String.valueOf(biExpert.getId()));

        userBiz.insertSelective(user);
        return new ObjectRestResponse<BiExpert>();
    }

    @RequestMapping("validate")
    @ResponseBody
    public ObjectRestResponse<BiExpert> getByToken(@RequestParam String token) throws Exception {
        int id = 0;
        try {
            id = Integer.parseInt(userAuthUtil.getInfoFromToken(token).getId());
        } catch (NumberFormatException e) {
            throw new UserTokenException("User token is null or empty!");
        }

        ObjectRestResponse<BiExpert> result = new ObjectRestResponse<BiExpert>();
        BiExpert biExpert = baseBiz.validate(id);
        result.setRel(biExpert != null);
        result.setData(biExpert);
        return result;
    }

    @GetMapping("/ExpertDetail")
    public ObjectRestResponse<ExpertScore> getExpertDetail(@RequestParam int id) {
        ExpertScore expertScore = new ExpertScore();
        BiExpert biExpert = baseBiz.selectById(id);
        expertScore.setExpert(biExpert);
        expertScore.setScore(getAverScoreByExpertId(id));

        ObjectRestResponse<ExpertScore> result = new ObjectRestResponse<>(StatusCode.SUCCESS, expertScore);
        return result;
    }

    /**
     * 查询所有专家信息（带评分的那种，分数已取整）  cj
     *
     * @return
     */
    @GetMapping("/allExpert")
    public TableResultResponse<ExpertScore> getAllExpert(@RequestParam Map<String, Object> params) {
        //分页查询所有专家信息
        Query query = new Query(params);
        Example example = baseBiz.getExampleByQuery(query);
        Page<Object> res = PageHelper.startPage(query.getPage(), query.getLimit());
        List<BiExpert> biExpertsList = baseBiz.selectByExample(example);
        ArrayList<ExpertScore> expertScoreArrayList = new ArrayList<>();
        for (BiExpert biExpert : biExpertsList) {
            int expertId = biExpert.getId();
            ExpertScore expertScore = new ExpertScore();
            expertScore.setScore(getAverScoreByExpertId(expertId));
            expertScore.setExpert(biExpert);
            expertScoreArrayList.add(expertScore);
        }
     return new TableResultResponse<>(res.getTotal(), expertScoreArrayList);
    }

    /**
     * 根据专家id获取专家的总分，计算平均得分   cj
     *
     * @param expertId
     * @return
     */
    public double getAverScoreByExpertId(int expertId) {
        double sumScore = 0;
        Example example = new Example(BiConsultation.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("expertid", expertId);
        List<BiConsultation> biConsultationList = biConsultationBiz.selectByExample(example);
        for (int i = 0; i < biConsultationList.size(); i++) {
            sumScore += biConsultationList.get(i).getScore();
        }
        return (double) Math.round((sumScore / biConsultationList.size()));
    }

    /**
     * 按照评分降序排序  cj
     *
     * @return
     */
    @GetMapping("/orderByScore")
    public TableResultResponse<ExpertScore> orderByScore(@RequestParam Map<String, Object> params) {
        TableResultResponse<ExpertScore> listObjectRestResponse = getAllExpert(params);
        List<ExpertScore> expertScoreList = listObjectRestResponse.getData().getRows();
        ObjectRestResponse<List<ExpertScore>> result = new ObjectRestResponse<>();
        Collections.sort(expertScoreList, (Comparator<ExpertScore>) (o1, o2) -> (int) (o2.score - o1.score));
        return new TableResultResponse<>(listObjectRestResponse.getData().getTotal(),expertScoreList);
    }

}
