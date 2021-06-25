package com.github.wxiaoqi.security.info.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.info.sys.entity.Suggestion;
import com.github.wxiaoqi.security.info.sys.mapper.SuggestionMapper;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: gsy
 * @create: 2020-09-12 11:08
 **/

@Service
@Transactional(rollbackFor = Exception.class)
public class SuggestBiz extends BaseBiz<SuggestionMapper, Suggestion> {
    @Resource
    SuggestionMapper suggestionMapper;

    public ObjectRestResponse<T> addSuggest(Suggestion suggestion) {
        int i = insertSelective(suggestion);
        if (i <= 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, true);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }

    public TableResultResponse<Suggestion> listByType(Query query, String type, String theme, Date before, Date after) {
        Example example = new Example(Suggestion.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("idDeleted", 0);

        if (type != null && !"".equals(type)) {
            //类型查询
            criteria.andEqualTo("type", type);
        }
        if (theme != null && !"".equals(theme)) {
            //模糊查询主题
            String sql = " instr(theme,'" + theme + "' )>0";
            criteria.andCondition(sql);
        }
        if(before!=null){
            criteria.andGreaterThanOrEqualTo("updTime",before);
        }
        if(after!=null){
            criteria.andLessThanOrEqualTo("updTime",after);
        }

        try {
            TableResultResponse<Suggestion> result = getListByPageAndCondition(query, example);
            return result;
        } catch (Exception ex) {
            return new TableResultResponse<Suggestion>();
        }
    }

    public ObjectRestResponse<T> deal(Integer id, String updName) {
        Suggestion suggestion = new Suggestion();
        suggestion.setId(id);
        suggestion.setStatus(1);
        suggestion.setUpdName(updName);
        int i = suggestionMapper.updateByPrimaryKeySelective(suggestion);
        if (i == 0) {
            return new ObjectRestResponse<>(StatusCode.FAIL, false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true);
    }
}
