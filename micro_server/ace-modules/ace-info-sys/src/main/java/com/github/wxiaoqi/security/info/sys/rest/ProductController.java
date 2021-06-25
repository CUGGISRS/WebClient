package com.github.wxiaoqi.security.info.sys.rest;

import cn.hutool.core.util.PageUtil;
import com.alibaba.fastjson.JSONObject;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.info.sys.biz.ProductBiz;
import com.github.wxiaoqi.security.info.sys.entity.ProductsAgriculturalInfo;
import com.github.wxiaoqi.security.info.sys.entity.ProductsAndFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @description: 特色农产品
 * @author: gsy
 * @create: 2020-09-14 09:39
 **/
@Api(tags = {"特色农产品"})
@RestController
@RequestMapping("product")
public class ProductController {

    @Resource
    ProductBiz productBiz;

    @ApiOperation(value = "新增特色农产品", notes = "使用postman测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "名称", required = true, paramType = "form"),
            @ApiImplicitParam(name = "allType", value = "总类", required = false, paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类型", required = false, paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, paramType = "form"),
            @ApiImplicitParam(name = "files", value = "文件数组", required = false, paramType = "form"),
    })
    @PostMapping("/add")
    public ObjectRestResponse<T> addProduct(@RequestParam Map<String, String> map, @RequestParam MultipartFile[] files, @RequestParam MultipartFile[] AuthInfoFiles) {
        return productBiz.addProduct(map, files);
    }

    @ApiOperation(value = "删除:数据库移除")
    @DeleteMapping("/del")
    public ObjectRestResponse<T> delProduct(@RequestParam Integer[] ids) {
        return productBiz.delProduct(ids);
    }

    /* @ApiOperation(value = "多条删除:使状态为1")
    @DeleteMapping("/del")
    public ObjectRestResponse<T> delProduct(@RequestParam Integer[] ids){
        return productBiz.delProductList(ids);
    }*/

    @ApiOperation(value = "修改农产品", notes = "使用postman测试接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "农产品id", required = true, paramType = "form"),
            @ApiImplicitParam(name = "name", value = "农产品名称", required = false, paramType = "form"),
            @ApiImplicitParam(name = "allType", value = "总类", required = false, paramType = "form"),
            @ApiImplicitParam(name = "type", value = "类别", required = false, paramType = "form"),
            @ApiImplicitParam(name = "description", value = "描述", required = false, paramType = "form"),
            @ApiImplicitParam(name = "delIds", value = "待删除的农产品图片id【数组】", required = false, paramType = "form"),
    })
    @PutMapping("/update")
    public ObjectRestResponse<T> updateProduct(@RequestParam Integer id,
                                               String name,
                                               String allType,
                                               String type,
                                               String description,
                                               Integer[] delIds,
                                               MultipartFile[] files) {
        ProductsAgriculturalInfo products = new ProductsAgriculturalInfo();
        products.setId(id);
        products.setName(name);
        products.setAllType(allType);
        products.setType(type);
        products.setDescription(description);
        //防止只存在id的情况，通用mapper报错
        products.setImg(0);
        //删除的图片id数组
        //新图片文件数组
        return productBiz.updateProduct(products, delIds, files);
    }

    @ApiOperation(value = "分页查询所有农产品")
    @GetMapping("/all")
    public ObjectRestResponse<JSONObject> all(@RequestParam Integer page, @RequestParam Integer size, String authType, String allType) {
        int[] ints = PageUtil.transToStartEnd(page - 1, size);
        ints[1] = size;
        return productBiz.all(ints, authType, allType);
    }

    @ApiOperation(value = "根据id查询单个农产品详情")
    @GetMapping("/{id}")
    //url:?/1
    public ObjectRestResponse<ProductsAndFile> queryById(@PathVariable Integer id) {
        return productBiz.queryById(id);
    }

//    @ApiOperation(value = "测试@RequestParam")
//    @GetMapping("/")
//    url ?id=1
//    public ObjectRestResponse<JSONObject> queryByIdTest(@RequestParam String id){
//        return null;
//    }

}
