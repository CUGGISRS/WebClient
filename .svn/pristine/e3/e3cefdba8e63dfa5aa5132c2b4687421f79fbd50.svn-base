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
import com.github.wxiaoqi.security.zs.sys.biz.BuyInfoBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.entity.BuyInfo;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 收购
 */
@RestController
@RequestMapping("BuyInfo")
public class BuyInfoController extends BaseController<BuyInfoBiz, BuyInfo> {
    @Autowired
    private IFileInfoService fileInfoService;

    /**
     * @api {post} /BuyInfo/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条收购(带文件)信息
     * @apiParam {MultipartFile[]} buyPictureFiles 收购图片
     * @apiParam {String} traceCode 追溯码
     * @apiParam {String} name 收购产品名称
     * @apiParam {BigDecimal} buyTotal 收购总价(元)
     * @apiParam {BigDecimal} buyAmount 收购数量
     * @apiParam {String} buyAmountUnit 收购数量单位
     * @apiParam {Date} buyDate 收购时间
     * @apiParam {String} description 描述
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {String} sysName 系统名称
     * @apiParam {Integer} sysModule 系统模块(1水产，2种植)
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 收购
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    public ObjectRestResponse<BuyInfo> addDataAndFile(BuyInfo obj,
                                                      MultipartFile[] buyPictureFiles) throws Exception {


        List<String> delUrls = new ArrayList<>();
        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("收购添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> fileInfos = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_23,
                CommonConstants.FILE_TYPE_1, buyPictureFiles, delUrls);
        baseBiz.setFiles(obj, fileInfos);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /BuyInfo/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条收购(带文件)信息
     * @apiParam {MultipartFile[]} buyPictureFiles 收购图片
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {String} traceCode 追溯码
     * @apiParam {String} name 收购产品名称
     * @apiParam {BigDecimal} buyTotal 收购总价(元)
     * @apiParam {BigDecimal} buyAmount 收购数量
     * @apiParam {String} buyAmountUnit 收购数量单位
     * @apiParam {Date} buyDate 收购时间
     * @apiParam {String} description 描述
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {Integer} sysModule 系统模块(1水产，2种植)
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 收购
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<BuyInfo> update(BuyInfo obj,
                                              MultipartFile[] buyPictureFiles,
                                              Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            BuyInfo oldData = baseBiz.selectById(id);
            if (oldData != null) {

                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("收购修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_23
                        , CommonConstants.FILE_TYPE_1, buyPictureFiles, delUrls);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_23);
                baseBiz.setFiles(obj, fileInfos);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {delete} /BuyInfo/{id}  删除收购(带文件)、加工（带文件）
     * @apiDescription 无
     * @apiGroup 收购
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<BuyInfo> remove(@PathVariable int id) throws Exception {
        BuyInfo old = baseBiz.delLinkAndFile(id);
        if (old != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /BuyInfo/batchDelete 批量删除收购(带文件)、加工（带文件）
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 收购
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
     * @api {get} /BuyInfo/{id} 查询一条（带文件）
     * @apiDescription 查询一条收购（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 收购
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<BuyInfo> get(@PathVariable int id) {
        BuyInfo obj = baseBiz.selectById(id);
        if (obj != null) {
            List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_23);
            baseBiz.setFiles(obj, fileInfos);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /BuyInfo/pageByEId 分页展示某一企业的收购（带文件）(收购时间倒序)
     * @apiDescription 分页展示某一企业的收购（带文件）(收购时间倒序)
     * @apiParam {Date} buyDateS 收购时间起点(>=)
     * @apiParam {Date} buyDateE 收购时间终点(<=)
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} traceCode 追溯码
     * @apiParam {String} name 收购产品名称
     * @apiParam {BigDecimal} buyTotal 收购总价(元)
     * @apiParam {BigDecimal} buyAmount 收购数量
     * @apiParam {String} buyAmountUnit 收购数量单位
     * @apiParam {Date} buyDate 收购时间
     * @apiParam {String} description 描述
     * @apiParam {Integer} enterpriseId 企业id(必填，=)
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {Integer} sysModule 系统模块(1水产，2种植)(=)
     * @apiGroup 收购
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<BuyInfo> pageByEId(@RequestParam Map<String, Object> params,
                                                  Date buyDateS, Date buyDateE) throws Exception {

        Object eId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(eId)) {
            String orderBy = "buy_date desc";
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("enterpriseId", eId);
            toMap.put("sysModule", params.get("sysModule"));
            Map<String, Object> gtMap = new HashMap<>();
            gtMap.put("buyDate", buyDateS);
            Map<String, Object> ltMap = new HashMap<>();
            ltMap.put("buyDate", buyDateE);
            TableResultResponse<BuyInfo> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    toMap, null, null, gtMap, null, ltMap, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<BuyInfo> list = data.getData().getRows();
            if (list != null) {
                List<Integer> ids = list.stream().map(BuyInfo::getId).collect(Collectors.toList());
                //设置多文件
                List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                        CommonConstants.FORM_NAME_23);
                FileInfoUtil.setListField("setBuyPictureList", fileInfos, list,
                        CommonConstants.FILE_TYPE_1);
                return data;
            }
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }

}
