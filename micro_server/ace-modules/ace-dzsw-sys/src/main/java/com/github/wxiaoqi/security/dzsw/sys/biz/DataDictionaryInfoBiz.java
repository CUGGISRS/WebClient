package com.github.wxiaoqi.security.dzsw.sys.biz;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.common.util.Query;
import com.github.wxiaoqi.security.common.util.StringHelper;
import com.github.wxiaoqi.security.dzsw.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.dzsw.sys.entity.DataDictionaryInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.DataDictionaryInfoMapper;
import com.github.wxiaoqi.security.dzsw.sys.utils.CommonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author TsaiJun
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataDictionaryInfoBiz extends BaseBiz<DataDictionaryInfoMapper, DataDictionaryInfo> {

    /**
     * 根据type 或 code 获取数据字典
     *
     * @param jsonObject
     * @return
     */
    public ObjectRestResponse dictionaryListByType(JSONObject jsonObject) {


        if (jsonObject.containsKey("type")) {
            String type = jsonObject.getString("type");
            Example dicExample = new Example(DataDictionaryInfo.class);
            if (jsonObject.containsKey("isCodeAsc")) {
                boolean isCodeDesc = jsonObject.getBoolean("isCodeAsc");
                if (isCodeDesc) {
                    dicExample.setOrderByClause("code asc");
                }
            }

            Example.Criteria criteria = dicExample.createCriteria();
            if (jsonObject.containsKey("code")) {
                String code = jsonObject.getString("code");
                criteria.andEqualTo("code", code);
            }
            criteria.andEqualTo("type", type);
            List<DataDictionaryInfo> list = selectByExample(dicExample);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, list.size()));
        } else {
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR);
        }
    }

    //根据数据类型和数据名称查询一条数据字典
    public ObjectRestResponse getDicByTypeAndText(Map<String, Object> map) {
        try {
            Query query = new Query(map);
            Example example = getExampleByQuery(query);
            List<DataDictionaryInfo> list = selectByExample(example);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, CommonUtil.successPage(list, list.size()));
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL);
        }
    }

    /**
     * 根据数据代码查询数据
     */
    public List<DataDictionaryInfo> getByCode(String code) throws Exception {
        if (StringHelper.isNullOrEmpty(code)) {
            return null;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("code", code);
        List<DataDictionaryInfo> list = getByToMap(toMap);
        return list;
    }

    /**
     * 根据数据代码查询一条
     */
    public DataDictionaryInfo getOneByCode(String code) throws Exception {
        List<DataDictionaryInfo> list = getByCode(code);
        int size = MyObjectUtil.iterableCount(list);
        if (size == 1) {
            return list.get(0);
        } else if (size > 1) {
            throw new Exception("字典中编号必须唯一");
        }
        return null;
    }

    /**
     * 根据数据类型和数据名称查询数据
     */
    public List<DataDictionaryInfo> getByTypeAndText(String type, String text) throws Exception {
        if (StringHelper.isNullOrEmpty(type) || StringHelper.isNullOrEmpty(text)) {
            return null;
        }
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("text", text);
        toMap.put("type", type);
        List<DataDictionaryInfo> list = getByToMap(toMap);
        return list;
    }


    // 插入一条数据类型
    public ObjectRestResponse addDic(Map<String, Object> map) {
        try {

            String type = (String) map.get("type");
            String code = (String) map.get("code");
            String text = (String) map.get("text");
            String remark = (String) map.get("remark");
            if (StringHelper.isNullOrEmpty(type) || StringHelper.isNullOrEmpty(text) || StringHelper.isNullOrEmpty(code)) {
                return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
            }

            List<DataDictionaryInfo> list1 = getByCode(code);
            List<DataDictionaryInfo> list2 = getByTypeAndText(type, text);
            int num1 = MyObjectUtil.iterableCount(list1);
            int num2 = MyObjectUtil.iterableCount(list2);
            if (num1 > 0) {
                //字典中编号唯一
                return new ObjectRestResponse<>(StatusCode.FAIL, "已存在相同的编号");
            }
            if (num2 > 0) {
                //字典中字典类型和数据值组合唯一
                return new ObjectRestResponse<>(StatusCode.FAIL, "已存在相同的字典类型和数据值");
            }
            DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo();
            dataDictionaryInfo.setCode(code);
            dataDictionaryInfo.setType(type);
            dataDictionaryInfo.setText(text);
            dataDictionaryInfo.setRemark(remark);
            int affect = insertSelective(dataDictionaryInfo);
            if (affect > 0) {
                return new ObjectRestResponse<>(StatusCode.SUCCESS, dataDictionaryInfo);
            } else {
                return new ObjectRestResponse<>(StatusCode.FAIL);
            }
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex.getMessage());
        }
    }

    // 删除一条数据字典
    public ObjectRestResponse delDic(String code) {
        try {
            //获得原数据
            DataDictionaryInfo old = getOneByCode(code);
            if (old == null) {
                return new ObjectRestResponse<>(StatusCode.FAIL, "字典中不存在该编号");
            }
            int affect = deleteById(code);
            if (affect > 0) {
                return new ObjectRestResponse<>(StatusCode.SUCCESS, old);
            } else {
                return new ObjectRestResponse<>(StatusCode.FAIL);
            }
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex.getMessage());
        }
    }

    // 更新一条数据字典
    public ObjectRestResponse updateDic(Map<String, Object> map) {
        try {
            String type = (String) map.get("type");
            String code = (String) map.get("code");
            String text = (String) map.get("text");
            String remark = (String) map.get("remark");
            //获得原数据
            DataDictionaryInfo old = getOneByCode(code);
            if (old == null) {
                return new ObjectRestResponse<>(StatusCode.FAIL, "字典中不存在该编号");
            }
            String type1 = old.getType();
            String text1 = old.getText();

            String type0 = StringHelper.isNullOrEmpty(type) ? type1 : type;
            String text0 = StringHelper.isNullOrEmpty(text) ? text1 : text;
            //字典类型和数据值都相同时无需查询
            if (!type1.equals(type0) || !text1.equals(text0)) {
                List<DataDictionaryInfo> list = getByTypeAndText(type0, text0);
                if (MyObjectUtil.iterableCount(list) > 0) {
                    //字典中字典类型和数据值组合唯一
                    return new ObjectRestResponse<>(StatusCode.FAIL, "已存在相同的字典类型和数据值");
                }
            }

            DataDictionaryInfo dataDictionaryInfo = new DataDictionaryInfo(code, type, text, remark);
            int affect = updateSelectiveById(dataDictionaryInfo);
            if (affect > 0) {
                return new ObjectRestResponse<>(StatusCode.SUCCESS, dataDictionaryInfo);
            } else {
                return new ObjectRestResponse<>(StatusCode.FAIL);
            }
        } catch (Exception ex) {
            return new ObjectRestResponse<>(StatusCode.FAIL, ex.getMessage());
        }
    }

}
