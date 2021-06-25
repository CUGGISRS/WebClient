package com.github.wxiaoqi.security.com.sys.rest;

import com.github.wxiaoqi.security.com.sys.biz.DataDictionaryBiz;
import com.github.wxiaoqi.security.com.sys.entity.DataDictionary;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.rest.BaseController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 数据字典
 */
@RestController
@RequestMapping("DataDictionary")
public class DataDictionaryController extends BaseController<DataDictionaryBiz, DataDictionary> {


    /**
     * @api {get} /DataDictionary/getAllMayToCondition 通过条件查询全部数据(code排序)
     * @apiDescription name和type组合唯一，code唯一
     * @apiParam {String} type 类型（=）
     * @apiParam {String} name 名称（=）
     * @apiParam {String} code 代码（=）
     * @apiParam {String} remark 备注（=）
     * @apiGroup 数据字典
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("getAllMayToCondition")
    public ObjectRestResponse<List<DataDictionary>> getAllMayToCondition(String type,
                                                                         String remark, String name, String code) {
        List<DataDictionary> list = baseBiz.getAllMayToCondition(type, remark, name, code);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, list);
    }
}
