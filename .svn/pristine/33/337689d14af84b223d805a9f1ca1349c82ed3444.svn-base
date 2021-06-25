package com.github.wxiaoqi.security.zs.sys.rest;

import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.BaseWorkBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BaseWork;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基本作业
 */
@RestController
@RequestMapping("BaseWork")
public class BaseWorkController extends BaseController<BaseWorkBiz, BaseWork> {

    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private ProductBiz productBiz;

    /**
     * @api {post} /BaseWork/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条基本作业(带文件)信息
     * @apiParam {MultipartFile[]} workPictureFiles 作业图片
     * @apiParam {Integer} productId 产品id
     * @apiParam {String} productName 产品名称
     * @apiParam {String} workType 作业类型
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiParam {String} workObjectName 作业物名称
     * @apiParam {String} workObjectType 作业物类型
     * @apiParam {String} workObjectSource 作业物来源
     * @apiParam {BigDecimal} workObjectAmount 作业物数量
     * @apiParam {String} workObjectAmountUnit 作业物数量单位
     * @apiParam {Integer} operatorId 操作人id（关联用户表id）
     * @apiParam {String} operator 操作人
     * @apiParam {String} weather 天气状况
     * @apiParam {Integer} supervisorId 监督人id（关联用户表id）
     * @apiParam {String} supervisor 监督人
     * @apiParam {String} supervisorResult 监督结果
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 基本作业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    public ObjectRestResponse<BaseWork> addDataAndFile(BaseWork obj,
                                                       MultipartFile[] workPictureFiles) throws Exception {

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("基本作业添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_5,
                CommonConstants.FILE_TYPE_1, workPictureFiles, delUrls);
        obj.setWorkPictureList(list1);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }


    /**
     * @api {put} /BaseWork/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条基本作业(带文件)信息
     * @apiParam {MultipartFile[]} workPictureFiles 作业图片
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} productId 产品id
     * @apiParam {String} productName 产品名称
     * @apiParam {String} workType 作业类型
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiParam {String} workObjectName 作业物名称
     * @apiParam {String} workObjectType 作业物类型
     * @apiParam {String} workObjectSource 作业物来源
     * @apiParam {BigDecimal} workObjectAmount 作业物数量
     * @apiParam {String} workObjectAmountUnit 作业物数量单位
     * @apiParam {Integer} operatorId 操作人id（关联用户表id）
     * @apiParam {String} operator 操作人
     * @apiParam {String} weather 天气状况
     * @apiParam {Integer} supervisorId 监督人id（关联用户表id）
     * @apiParam {String} supervisor 监督人
     * @apiParam {String} supervisorResult 监督结果
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 基本作业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    @PutMapping("/updateDataAndFile")
    public ObjectRestResponse<BaseWork> update(BaseWork obj,
                                               MultipartFile[] workPictureFiles, Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            BaseWork oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("基本作业修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_5
                        , CommonConstants.FILE_TYPE_1, workPictureFiles, delUrls);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> list1 = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_5);
                obj.setWorkPictureList(list1);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {delete} /BaseWork/{id}  删除一条(带文件)
     * @apiDescription 删除一条基本作业(带文件)信息
     * @apiGroup 基本作业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<BaseWork> remove(@PathVariable int id) throws Exception {
        BaseWork old = baseBiz.selectById(id);
        if (old != null) {
            int index = baseBiz.deleteById(id);
            if (index == 0) {
                throw new Exception("基本作业删除失败");
            }
            //删除多文件
            List<String> delUrls = new ArrayList<>();
            old.setWorkPictureList(fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_5, delUrls));
            fileInfoService.delFileByUrls(delUrls);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /BaseWork/batchDelete 批量删除(带文件)
     * @apiDescription 通过id集合批量删除基本作业(带文件)
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 基本作业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        List<BaseWork> oldList = baseBiz.batchSelectByIds(ids);
        if (oldList != null) {
            int num = ids.size();
            if (num == oldList.size()) {
                int index = baseBiz.batchDeleteByIds(ids);
                if (index != num) {
                    throw new Exception("基本作业批量删除失败");
                }
                //删除多文件
                List<String> delUrls = new ArrayList<>();
                fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_5, delUrls);
                fileInfoService.delFileByUrls(delUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /BaseWork/{id} 查询一条（带文件）
     * @apiDescription 查询一条基本作业（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 基本作业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<BaseWork> get(@PathVariable int id) {
        BaseWork obj = baseBiz.selectById(id);
        if (obj != null) {
            obj.setWorkPictureList(fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_5));
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /BaseWork/pageByEId 分页展示某一企业(某一基地类型)下产品或某一产品的基本作业（带文件）（结束时间倒序）
     * @apiDescription 分页展示某一企业(某一基地类型)下产品或某一产品的基本作业（带文件）（结束时间倒序）
     * @apiParam {Integer} enterpriseId 企业id（=）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {Date} startDateS 开始时间起点(>=)
     * @apiParam {Date} endDateE 结束时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} productId 产品id（=）
     * @apiParam {String} productName 产品名称
     * @apiParam {String} workType 作业类型（=）
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiParam {String} workObjectName 作业物名称
     * @apiParam {String} workObjectType 作业物类型
     * @apiParam {String} workObjectSource 作业物来源
     * @apiParam {BigDecimal} workObjectAmount 作业物数量
     * @apiParam {String} workObjectAmountUnit 作业物数量单位
     * @apiParam {Integer} operatorId 操作人id（关联用户表id）
     * @apiParam {String} operator 操作人
     * @apiParam {String} weather 天气状况
     * @apiParam {Integer} supervisorId 监督人id（关联用户表id）
     * @apiParam {String} supervisor 监督人
     * @apiParam {String} supervisorResult 监督结果
     * @apiGroup 基本作业
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<BaseWork> pageByEId(@RequestParam Map<String, Object> params,
                                                   Date startDateS, Date endDateE) throws Exception {
        String orderBy = "end_date desc";
        Object pId = params.get("productId");
        Object workType = params.get("workType");
        Map<String, Iterable> inMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();
        if (MyObjectUtil.noNullOrEmpty(pId)) {
            toMap.put("productId", pId);
        } else {
            List<Integer> pIds = productBiz.getByEIdAndBaseType(params.get("enterpriseId"),
                    params.get("baseType"));
            if (pIds != null) {
                inMap.put("productId", pIds);
            } else {
                return new TableResultResponse<>(0, new ArrayList<>());
            }
        }
        toMap.put("workType", workType);
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("startDate", startDateS);
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("endDate", endDateE);
        TableResultResponse<BaseWork> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, gtMap, null, ltMap, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<BaseWork> list = data.getData().getRows();
        if (list != null) {
            List<Integer> ids = list.stream().map(BaseWork::getId).collect(Collectors.toList());
            //设置多文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_5);
            FileInfoUtil.setListField("setWorkPictureList", fileInfos, list,
                    CommonConstants.FILE_TYPE_1);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
