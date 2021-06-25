package com.github.wxiaoqi.security.zs.sys.rest;

import com.alibaba.fastjson.JSONObject;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.zs.sys.biz.HarvestBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProcessBatchBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.entity.Harvest;
import com.github.wxiaoqi.security.zs.sys.entity.ProcessBatch;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import com.github.wxiaoqi.security.zs.sys.util.UniqueUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 加工批次
 */
@RestController
@RequestMapping("ProcessBatch")
public class ProcessBatchController extends BaseController<ProcessBatchBiz, ProcessBatch> {
    @Autowired
    private IFileInfoService fileInfoService;

    @Autowired
    private ProductBiz productBiz;

    @Autowired
    private HarvestBiz harvestBiz;

    /**
     * @api {post} /ProcessBatch/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条加工批次(带文件)信息
     * @apiParam {MultipartFile[]} productBatchNumberPictureFiles 产品批次号图片
     * @apiParam {Integer} baseId 加工基地id
     * @apiParam {String} baseName 加工基地名称
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiParam {String} productBatchNumber 产品批次号（唯一,后端生成）
     * @apiParam {BigDecimal} processAmount 加工数量
     * @apiParam {String} processAmountUnit 加工数量单位
     * @apiParam {Integer} harvestId 收获表id
     * @apiParam {Integer} buyId 收购表id
     * @apiParam {String} processWay 加工方式
     * @apiParam {Integer} productDetailId 产品详情表id
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 加工批次
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @ResponseBody
    public ObjectRestResponse<ProcessBatch> addDataAndFile(ProcessBatch obj,
                                                           MultipartFile[] productBatchNumberPictureFiles) throws Exception {
        //生成20位唯一码
        String productBatchNumber = UniqueUtil.getUniqueCode();
        //是否唯一
        ProcessBatch old = baseBiz.getOneByPBN(productBatchNumber);
        Harvest hOld = harvestBiz.getOneByTraceCode(productBatchNumber);
        if (old != null || hOld != null) {
            throw new Exception("该产品批次号已存在");
        }
        obj.setProductBatchNumber(productBatchNumber);

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("加工批次添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_14,
                CommonConstants.FILE_TYPE_1, productBatchNumberPictureFiles, delUrls);
        obj.setProductBatchNumberPictureList(list1);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /ProcessBatch/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条加工批次(带文件)信息
     * @apiParam {MultipartFile[]} productBatchNumberPictureFiles 产品批次号图片
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} baseId 加工基地id
     * @apiParam {String} baseName 加工基地名称
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiParam {String} productBatchNumber 产品批次号（唯一）
     * @apiParam {BigDecimal} processAmount 加工数量
     * @apiParam {String} processAmountUnit 加工数量单位
     * @apiParam {Integer} harvestId 收获表id
     * @apiParam {Integer} buyId 收购表id
     * @apiParam {String} processWay 加工方式
     * @apiParam {Integer} productDetailId 产品详情表id
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 加工批次
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<ProcessBatch> update(ProcessBatch obj,
                                                   MultipartFile[] productBatchNumberPictureFiles,
                                                   Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            ProcessBatch oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //是否唯一
                String productBatchNumber = obj.getProductBatchNumber();
                Harvest hOld = harvestBiz.getOneByTraceCode(productBatchNumber);
                ProcessBatch old = baseBiz.getOneByPBN(productBatchNumber);
                if (hOld != null || (old != null && !id.equals(old.getId()))) {
                    throw new Exception("该产品批次号已存在");
                }

                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("加工批次修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_14
                        , CommonConstants.FILE_TYPE_1, productBatchNumberPictureFiles, delUrls);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> list1 = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_14);
                obj.setProductBatchNumberPictureList(list1);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {delete} /ProcessBatch/{id}  删除一条加工(带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     * @apiDescription 无
     * @apiGroup 加工批次
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<ProcessBatch> remove(@PathVariable int id) throws Exception {
        ProcessBatch old = baseBiz.delLinkAndFile(id);
        if (old != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /ProcessBatch/batchDelete 批量删除加工(带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 加工批次
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        Integer num = baseBiz.delLinkAndFile(ids);
        if (num != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {get} /ProcessBatch/{id} 查询一条（带文件）
     * @apiDescription 查询一条加工批次（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 加工批次
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<ProcessBatch> get(@PathVariable int id) {
        ProcessBatch obj = baseBiz.selectById(id);
        if (obj != null) {
            obj.setProductBatchNumberPictureList(fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_14));
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /ProcessBatch/pageByEId 分页展示某一企业(某一基地类型)的加工批次（带文件）（结束时间倒序）(收获、收购)
     * @apiDescription 分页展示某一企业(某一基地类型)的加工批次（带文件）（结束时间倒序）(收获、收购)
     * @apiParam {Integer} enterpriseId 企业id（必填，=）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {Date} startDateS 开始时间起点(>=)
     * @apiParam {Date} endDateE 结束时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {Integer} baseId 加工基地id(=)
     * @apiParam {String} baseName 加工基地名称
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiParam {String} productBatchNumber 产品批次号（唯一）
     * @apiParam {BigDecimal} processAmount 加工数量
     * @apiParam {String} processAmountUnit 加工数量单位
     * @apiParam {Integer} harvestId 收获表id
     * @apiParam {Integer} buyId 收购表id
     * @apiParam {String} processWay 加工方式
     * @apiParam {Integer} productDetailId 产品详情表id(=)
     * @apiGroup 加工批次
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<ProcessBatch> pageByEId(@RequestParam Map<String, Object> params,
                                                       Date startDateS, Date endDateE) throws Exception {
        String orderBy = "end_date desc";
        Map<String, Iterable> orInMap = new HashMap<>();
        Map<String, Object> toMap = new HashMap<>();
        Object enterpriseId = params.get("enterpriseId");
        Object baseType = params.get("baseType");
        //收获
        List<Integer> hIds = productBiz.getHarvestIdsByEIdAndBaseType(enterpriseId,
                baseType);
        //收购
        Integer sysModule = productBiz.getSysModuleByBaseType(baseType);
        List<Integer> bIds = productBiz.getBuyIdsByEId(enterpriseId, sysModule);
        if (hIds != null || bIds != null) {
            //or查询
            orInMap.put("harvestId", hIds);
            orInMap.put("buyId", bIds);
        } else {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        toMap.put("baseId", params.get("baseId"));
        toMap.put("productDetailId", params.get("productDetailId"));
        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("startDate", startDateS);
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("endDate", endDateE);
        TableResultResponse<ProcessBatch> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                toMap, null, null, gtMap, null, ltMap, null, null,
                null, null, null, null, null, null,
                null, null, null, null, orInMap, null);
        List<ProcessBatch> list = data.getData().getRows();
        if (list != null) {
            List<Integer> ids = list.stream().map(ProcessBatch::getId).collect(Collectors.toList());
            //设置多文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_14);
            FileInfoUtil.setListField("setProductBatchNumberPictureList", fileInfos, list,
                    CommonConstants.FILE_TYPE_1);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }


    /**
     * @api {get} /ProcessBatch/traceByPBN 通过产品批次号（或追溯码）获得追溯信息（带文件）
     * @apiDescription 通过产品批次号（或追溯码）获得追溯信息（产品、基地、基本作业、收获、加工批次、成品检验、销售、产品详情、收购、传感器、传感器数据）（带文件）
     * @apiParam {String} productBatchNumber 产品批次号（或追溯码）
     * @apiGroup 加工批次
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("traceByPBN")
    public ObjectRestResponse<JSONObject> traceByPBN(String productBatchNumber) throws Exception {
        JSONObject obj = baseBiz.traceByPBN(productBatchNumber);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }


    /**
     * @api {get} /ProcessBatch/statisticsByEId 获得某一企业(某一基地类型)某一年的统计信息
     * @apiDescription 获得某一企业(某一基地类型)某一年的统计信息
     * @apiParam {Date} before 一年的时间起点(非空，如2020-1，2020-1-1)
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} baseType 基地类型
     * @apiGroup 加工批次
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("statisticsByEId")
    public ObjectRestResponse<JSONObject> statisticsByEId(Integer enterpriseId, String baseType, Date before) throws Exception {
        JSONObject obj = baseBiz.statisticsByEId(enterpriseId, baseType, before);
        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

}
