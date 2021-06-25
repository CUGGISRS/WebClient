package com.github.wxiaoqi.security.com.sys.rest;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.biz.FileInfoBiz;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件
 */
@RestController
@RequestMapping("FileInfo")
public class FileInfoController extends BaseController<FileInfoBiz, FileInfo> {

    @GetMapping("getByFormIdAndFormName")
    public List<FileInfo> getByFormIdAndFormName(Integer formId, String formName) {
        return baseBiz.getByFormIdAndFormName(formId, formName);
    }

    @PostMapping("getByFormIdsAndFormName")
    public List<FileInfo> getByFormIdsAndFormName(@RequestBody List<Integer> formIds,
                                                  @RequestParam("formName") String formName) {
        return baseBiz.getByFormIdsAndFormName(formIds, formName);
    }

    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @DeleteMapping("delByFormIdAndFormName")
    public Integer delByFormIdAndFormName(Integer formId, String formName) {
        return baseBiz.delByFormIdAndFormName(formId, formName);
    }

    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @PostMapping("delByFormIdsAndFormName")
    public Integer delByFormIdsAndFormName(@RequestBody List<Integer> formIds,
                                           @RequestParam("formName") String formName) {
        return baseBiz.delByFormIdsAndFormName(formIds, formName);
    }


    @PostMapping(value = "uploadFile", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart(value = "file", required = false) MultipartFile file
            , String dir, String addFileName) throws Exception {

      /*  Collection<Part> parts = request.getParts();
        System.out.println(JSONObject.toJSONString(parts, true));*/
        return baseBiz.uploadFile(file, dir, addFileName);
    }


    @PostMapping("delFileByUrls")
    public void delFileByUrls(@RequestBody List<String> urls) {
        baseBiz.delFileByUrls(urls);
    }


    @PostMapping("selectByIds")
    public List<FileInfo> selectByIds(@RequestBody List<Integer> ids) {
        return baseBiz.batchSelectByIds(ids);
    }

    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @PostMapping("deleteByIds")
    public Integer deleteByIds(@RequestBody List<Integer> ids) {
        return baseBiz.batchDeleteByIds(ids);
    }


    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @PostMapping(value = "insertByList")
    public Integer insertByList(@RequestBody List<FileInfo> fileInfos) {
        return baseBiz.batchInsertByList(fileInfos);
    }


    /**
     * @api {get} /FileInfo/pageByCondition 分页展示文件（id倒序）
     * @apiDescription 分页展示文件（id倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} formId 表id（=）
     * @apiParam {String} formName 表名（=）
     * @apiParam {Integer} type 表文件类型(=)
     * @apiParam {String} url 路径
     * @apiGroup 文件
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByCondition")
    public TableResultResponse<FileInfo> pageByCondition(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Map<String, Object> toMap = new HashMap<>();
        toMap.put("formId", params.get("formId"));
        toMap.put("formName", params.get("formName"));
        toMap.put("type", params.get("type"));
        TableResultResponse<FileInfo> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, null, null, null, null, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<FileInfo> list = data.getData().getRows();
        if (list != null) {
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
