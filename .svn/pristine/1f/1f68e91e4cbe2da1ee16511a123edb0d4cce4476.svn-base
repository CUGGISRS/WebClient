package com.github.wxiaoqi.security.zs.sys.rest;

import com.alibaba.fastjson.JSONArray;
import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.com.sys.util.FileInfoUtil;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.common.msg.TableResultResponse;
import com.github.wxiaoqi.security.common.rest.BaseController;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.biz.*;
import com.github.wxiaoqi.security.zs.sys.entity.*;
import com.github.wxiaoqi.security.zs.sys.feign.service.IFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 产品
 */
@RestController
@RequestMapping("Product")
public class ProductController extends BaseController<ProductBiz, Product> {
    @Autowired
    private IFileInfoService fileInfoService;
    @Autowired
    private ProductDetailBiz productDetailBiz;


    @GetMapping("getPBNByEnterpriseId")
    public List<String> getPBNByEnterpriseId(Integer enterpriseId, String baseType) {
        return baseBiz.getPBNByEnterpriseId(enterpriseId, baseType);
    }

    @PostMapping("enterpriseArea")
    public Map<String, List<Integer>> enterpriseArea(@RequestBody List<Integer> eIds) {
        return baseBiz.enterpriseArea(eIds);
    }

    @PostMapping("productDetailModule")
    public Map<String, List<Integer>> productDetailModule(@RequestBody List<Integer> pdIds) {
        return baseBiz.productDetailModule(pdIds);
    }

    @GetMapping("getSysModuleByPDId")
    public Integer getSysModuleByPDId(Integer pdId) {
        ProductDetail pd = productDetailBiz.selectById(pdId);
        return pd == null ? null : pd.getSysModule();
    }


    @PostMapping("pbnPartMap")
    public JSONArray pbnPartMap(@RequestBody List<String> pbnList) {
        return baseBiz.pbnPartMap(pbnList);
    }


    /**
     * @api {post} /Product/addDataAndFile  添加一条(带文件)
     * @apiDescription 添加一条产品(带文件)信息
     * @apiParam {MultipartFile[]} pictureFiles 图片
     * @apiParam {String} name 名称
     * @apiParam {Integer} productDetailId 产品详情表id
     * @apiParam {Integer} baseId 基地id
     * @apiParam {String} baseName 基地名称
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 产品
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PostMapping("/addDataAndFile")
    @ResponseBody
    public ObjectRestResponse<Product> addDataAndFile(Product obj,
                                                      MultipartFile[] pictureFiles) throws Exception {

        List<String> delUrls = new ArrayList<>();

        int index = baseBiz.insertSelective(obj);
        if (index == 0) {
            fileInfoService.delFileByUrls(delUrls);
            throw new Exception("产品添加失败");
        }
        Integer id = obj.getId();
        //添加多文件
        List<FileInfo> list1 = fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_15,
                CommonConstants.FILE_TYPE_1, pictureFiles, delUrls);
        obj.setPictureList(list1);

        return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
    }

    /**
     * @api {put} /Product/updateDataAndFile  修改一条(带文件)
     * @apiDescription 修改一条产品(带文件)信息
     * @apiParam {MultipartFile[]} pictureFiles 图片
     * @apiParam {Integer[]} delFileIds 待删除文件表id数组
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {Integer} productDetailId 产品详情表id
     * @apiParam {Integer} baseId 基地id
     * @apiParam {String} baseName 基地名称
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiParamExample {form-data} Request-Example:
     * {
     * 参数类型：body/form-data 存在文件参数时请使用postman测试
     * }
     * @apiGroup 产品
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @PutMapping("/updateDataAndFile")
    @LcnTransaction//分布式事务
    @Transactional //本地事务
    public ObjectRestResponse<Product> update(Product obj,
                                              MultipartFile[] pictureFiles,
                                              Integer[] delFileIds
    ) throws Exception {
        Integer id = obj.getId();
        if (id != null) {
            Product oldData = baseBiz.selectById(id);
            if (oldData != null) {
                //回退时删除的文件url
                List<String> delUrls = new ArrayList<>();
                //所有待删除的文件url
                List<String> oldUrls = new ArrayList<>();

                int index = baseBiz.updateSelectiveById(obj);
                if (index == 0) {
                    fileInfoService.delFileByUrls(delUrls);
                    throw new Exception("产品修改失败");
                }

                //删除多文件数据
                fileInfoService.batchDelData(delFileIds, oldUrls, delUrls);
                //添加多文件
                fileInfoService.batchAddDataAndFile(id, CommonConstants.FORM_NAME_15
                        , CommonConstants.FILE_TYPE_1, pictureFiles, delUrls);

                //修改基本作业、收购、生长环境、收获、定期检验中关联字段
              /*  String name=obj.getName();
                if(MyObjectUtil.noNullOrEmpty(name)){
                    baseWorkBiz.updateByPId(new BaseWork(name),id);
                    List<String> tcs=harvestBiz.getTraceCodesByList(harvestBiz.getByPId(id));
                    buyInfoBiz.updateByTraceCodes(new BuyInfo(name,null),tcs);
                    growEnvironmentBiz.updateByPId(new GrowEnvironment(name),id);
                    harvestBiz.updateByPId(new Harvest(name),id);
                    regularTestBiz.updateByPId(new RegularTest(name),id);
                }
*/
                //回填文件数据
                List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_15);
                baseBiz.setFiles(obj, fileInfos);
                //删除被替换了的文件
                fileInfoService.delFileByUrls(oldUrls);
                return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {delete} /Product/{id}  删除一条产品（带文件）、基本作业(带文件)、生长环境、收获(带文件)、收购（带文件）、加工(收获、收购)（带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     * @apiDescription 无
     * @apiGroup 产品
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Product> remove(@PathVariable int id) throws Exception {
        Product old = baseBiz.delLinkAndFile(id);
        if (old != null) {
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, old);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }

    /**
     * @api {post} /Product/batchDelete 批量删除产品（带文件）、基本作业(带文件)、生长环境、收获(带文件)、收购（带文件）、加工(收获、收购)（带文件）、成品检验（带文件）、部门抽检（带文件）、检验-项目(成品检验、部门抽检)、销售
     * @apiDescription 无
     * @apiParam {List:Integer} ids 编号集合
     * @apiParamExample {json} Request-Example(参数类型：body/json):
     * [
     * 1,2
     * ]
     * @apiGroup 产品
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
     * @api {get} /Product/{id} 查询一条（带文件）
     * @apiDescription 查询一条产品（带文件）
     * @apiParam {Integer} id 编号
     * @apiGroup 产品
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @Override
    public ObjectRestResponse<Product> get(@PathVariable int id) {
        Product obj = baseBiz.selectById(id);
        if (obj != null) {
            List<FileInfo> fileInfos = fileInfoService.getByFormIdAndFormName(id, CommonConstants.FORM_NAME_15);
            baseBiz.setFiles(obj, fileInfos);
            return new ObjectRestResponse<>(StatusCode.SUCCESS, true, obj);
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR, false);
    }


    /**
     * @api {get} /Product/pageByEId 分页展示某一企业(某一基地类型)的产品（带文件）（结束时间倒序）
     * @apiDescription 分页展示某一企业(某一基地类型)的产品（带文件）（结束时间倒序）
     * @apiParam {String} baseType 基地类型（=）
     * @apiParam {Date} startDateS 开始时间起点(>=)
     * @apiParam {Date} endDateE 结束时间终点(<=)
     * @apiParam {Integer} enterpriseId 企业id（必填，=）
     * @apiParam {String} type 类型（=）
     * @apiParam {String} code 商品条码（=）
     * @apiParam {Integer} page 页码
     * @apiParam {Integer} limit 行数
     * @apiParam {Integer} id 编号
     * @apiParam {String} name 名称
     * @apiParam {Integer} productDetailId 产品详情表id
     * @apiParam {Integer} baseId 基地id
     * @apiParam {String} baseName 基地名称
     * @apiParam {Date} startDate 开始时间
     * @apiParam {Date} endDate 结束时间
     * @apiGroup 产品
     * @apiHeader {String} Authorization 用户令牌
     * @apiVersion 1.0.0
     */
    @GetMapping("pageByEId")
    public TableResultResponse<Product> pageByEId(@RequestParam Map<String, Object> params,
                                                  Date startDateS, Date endDateE) throws Exception {
        String orderBy = "end_date desc";
        Object type = params.get("type");
        Object code = params.get("code");
        Object baseType = params.get("baseType");
        Object enterpriseId = params.get("enterpriseId");

        Map<String, Iterable> inMap = new HashMap<>();
        if (MyObjectUtil.noNullOrEmpty(code) || MyObjectUtil.noNullOrEmpty(type)) {
            Integer sysModule = baseBiz.getSysModuleByBaseType(baseType);
            List<ProductDetail> productDetails = productDetailBiz.getByEIdMayCodeAndType(sysModule, enterpriseId, code, type);
            List<Integer> pdIds = productDetailBiz.getIdsByList(productDetails);
            if (pdIds != null) {
                inMap.put("productDetailId", pdIds);
            } else {
                return new TableResultResponse<>(0, new ArrayList<>());
            }
        }
        List<Integer> baseIds = baseBiz.getBaseIdsByEIdAndBaseType(enterpriseId, baseType);

        if (baseIds != null) {
            inMap.put("baseId", baseIds);
        } else {
            return new TableResultResponse<>(0, new ArrayList<>());
        }

        Map<String, Object> gtMap = new HashMap<>();
        gtMap.put("startDate", startDateS);
        Map<String, Object> ltMap = new HashMap<>();
        ltMap.put("endDate", endDateE);
        TableResultResponse<Product> data = baseBiz.selectByQuery(null, orderBy, params, null, null, null,
                null, null, null, gtMap, null, ltMap, inMap, null,
                null, null, null, null, null, null,
                null, null, null, null, null, null);
        List<Product> list = data.getData().getRows();
        if (list != null) {
            List<Integer> ids = list.stream().map(Product::getId).collect(Collectors.toList());
            //设置多文件
            List<FileInfo> fileInfos = fileInfoService.getByFormIdsAndFormName(ids,
                    CommonConstants.FORM_NAME_15);
            FileInfoUtil.setListField("setPictureList", fileInfos, list,
                    CommonConstants.FILE_TYPE_1);
            return data;
        }
        return new TableResultResponse<>(0, new ArrayList<>());
    }


}
