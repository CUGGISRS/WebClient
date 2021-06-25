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
import com.github.wxiaoqi.security.zs.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.zs.sys.biz.ProductDetailBiz;
import com.github.wxiaoqi.security.zs.sys.entity.ProductDetail;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品详情
 */
@RestController
@RequestMapping("ProductDetail")
public class ProductDetailController extends BaseController<ProductDetailBiz, ProductDetail> {
    @Autowired
    private IFileInfoService fileInfoService;


    /**
     * @api {post} /ProductDetail/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条产品详情(带文件)信息
     * @apiParam {MultipartFile[]} pictureFiles 图片
     * @apiParam {MultipartFile[]} certificateFiles 认证证书
     * @apiParam {String} name 名称
     * @apiParam {String} code 商品条码（唯一）
     * @apiParam {String} type 类型
     * @apiParam {String} specs 规格
     * @apiParam {String} purpose 用途
     * @apiParam {String} introduce 介绍
     * @apiParam {Integer} status 状态（1在产，0停产）
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {String} shelfLife 保质期
     * @apiParam {Integer} sysModule 系统模块(1水产，2种植)
     * @apiParam {Integer} isDel 是否删除（0否，1是）(默认0)
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 产品详情
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<ProductDetail> addDataAndFile(ProductDetail obj,
                                                            MultipartFile[] pictureFiles,
                                                            MultipartFile[] certificateFiles) throws Exception {
        //是否唯一
        String code = obj.getCode();
        ProductDetail old = baseBiz.getOneByCode(code);
        if (old != null) {
            throw new Exception("该商品条码已存在");
        }

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("产品详情添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_22,
                CommonConstants.FILE_TYPE_1, pictureFiles, delUrls);
        obj.setPictureList(list1);
        List<FileInfo> list2 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_22,
                CommonConstants.FILE_TYPE_2, certificateFiles, delUrls);
        obj.setCertificateList(list2);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /ProductDetail/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条产品详情(带文件)信息
     * @apiParam {MultipartFile[]} pictureFiles 图片
     * @apiParam {MultipartFile[]} certificateFiles 认证证书
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {String} code 商品条码（唯一）
     * @apiParam {String} type 类型
     * @apiParam {String} specs 规格
     * @apiParam {String} purpose 用途
     * @apiParam {String} introduce 介绍
     * @apiParam {Integer} status 状态（1在产，0停产）
     * @apiParam {Integer} enterpriseId 企业id
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {String} shelfLife 保质期
     * @apiParam {Integer} sysModule 系统模块(1水产，2种植)
     * @apiParam {Integer} isDel 是否删除（0否，1是）
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 产品详情
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<ProductDetail> update(ProductDetail obj,
                                                    MultipartFile[] pictureFiles,
                                                    MultipartFile[] certificateFiles,
                                                    Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            ProductDetail oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //是否唯一
                String code = obj.getCode();
                ProductDetail old = baseBiz.getOneByCode(code);
                if (old != null && !id.equals(old.getId())) {
                    throw new Exception("该商品条码已存在");
                }

                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("产品详情修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_22
                        , CommonConstants.FILE_TYPE_1, pictureFiles, delUrls);
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_22
                        , CommonConstants.FILE_TYPE_2, certificateFiles, delUrls);

                //回填文件数据
                List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_22);
                baseBiz.setFiles(obj, fileInfos);

                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {delete} /ProductDetail/{id}  删除一条(假)
     * @apiDescription 无
     * @apiGroup 产品详情
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<ProductDetail> remove(@PathVariable int id) throws Exception {
        ProductDetail old = baseBiz.delFalse(id);
        if (old != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
       /* ProductDetail old = baseBiz.selectById(id);
        if (old != null) {
            int index = baseBiz.deleteById(id);
            if (index == 0) {
                throw new Exception("产品详情删除失败");
            }
            //删除多文件
            List<String> delUrls = new ArrayList<>();
            List<FileInfo> fileInfos = fileInfoService.commonDelByFIdAndFName(id, CommonConstants.FORM_NAME_22, delUrls);
            baseBiz.setFiles(old, fileInfos);
            fileInfoService.delFileByUrls(delUrls);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);*/
    }

    /**
     * @api {post} /ProductDetail/batchDelete 批量删除(假)
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 产品详情
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Integer> batchDeleteByIds(@RequestBody List<Integer> ids) throws Exception {
        Integer sum = baseBiz.delFalse(ids);
        if (sum != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, sum);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);

      /*  List<ProductDetail> oldList = baseBiz.batchSelectByIds(ids);
        if (oldList != null) {
            int num = ids.size();
            if (num == oldList.size()) {
                int index = baseBiz.batchDeleteByIds(ids);
                if (index != num) {
                    throw new Exception("产品详情批量删除失败");
                }
                //删除多文件
                List<String> delUrls = new ArrayList<>();
                fileInfoService.commonDelByFIdsAndFName(ids, CommonConstants.FORM_NAME_22, delUrls);
                fileInfoService.delFileByUrls(delUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, num);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);*/
    }


    /**
     * @api {get} /ProductDetail/{id} 查询一条（带文件）
     * @apiDescription 查询一条产品详情（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 产品详情
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<ProductDetail> get(@PathVariable int id) {
        ProductDetail obj = baseBiz.selectById(id);
        if (obj != null) {
            List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_22);
            baseBiz.setFiles(obj, fileInfos);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /ProductDetail/pageByEId 分页展示某一企业的产品详情（带文件）（id倒序）
     * @apiDescription 分页展示某一企业的产品详情（带文件）（id倒序）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {String} code 商品条码（唯一）
     * @apiParam {String} type 类型（=）
     * @apiParam {String} specs 规格
     * @apiParam {String} purpose 用途
     * @apiParam {String} introduce 介绍
     * @apiParam {Integer} status 状态（1在产，0停产）
     * @apiParam {Integer} enterpriseId 企业id（必填，=）
     * @apiParam {String} enterpriseName 企业名称
     * @apiParam {String} shelfLife 保质期
     * @apiParam {Integer} sysModule 系统模块(1水产，2种植)(=)
     * @apiParam {Integer} isDel 是否删除（0否，1是）(默认0,=)
     * @apiGroup 产品详情
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<ProductDetail> pageByEId(@RequestParam Map<String, Object> params) throws Exception {
        String orderBy = "id desc";
        Object eId = params.get("enterpriseId");
        if (MyObjectUtil.noNullOrEmpty(eId)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("type", params.get("type"));
            andToMap.put("sysModule", params.get("sysModule"));
            andToMap.put("enterpriseId", eId);

            Object isDel = params.get("isDel");
            andToMap.put("isDel", isDel == null ? 0 : isDel);
            TableResultResponse<ProductDetail> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                    andToMap, null, null, null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null, null, null, null, null);
            List<ProductDetail> list = data.getData().getRows();
            if (list != null) {
                List<Integer> ids = list.stream().map(ProductDetail::getId).collect(Collectors.toList());
                //设置多文件
                List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                        CommonConstants.FORM_NAME_22);
                FileInfoUtil.setListField("setPictureList", fileInfos, list,
                        CommonConstants.FILE_TYPE_1);
                FileInfoUtil.setListField("setCertificateList", fileInfos, list,
                        CommonConstants.FILE_TYPE_2);
                return data;
            }
        }

        return new TableResultResponse<>(0, new ArrayList<>());
    }
}
