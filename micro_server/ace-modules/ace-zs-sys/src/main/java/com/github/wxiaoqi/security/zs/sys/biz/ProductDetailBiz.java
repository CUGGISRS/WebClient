package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.ProductDetail;
import com.github.wxiaoqi.security.zs.sys.mapper.ProductDetailMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 产品详情
 */
@Service
public class ProductDetailBiz extends BaseBiz<ProductDetailMapper, ProductDetail> {
    /**
     * 修改某一企业的数据
     */
    public int updateByEId(ProductDetail obj, Integer eId) {
        if (eId == null) {
            return 0;
        }
        String fieldName = "enterpriseId";
        return updateByFiled(obj, eId, fieldName);
    }

    /**
     * 修改某些id的数据
     */
    public int updateByIds(ProductDetail obj, List<Integer> ids) {
        String fieldName = "id";
        return updateByInFiledMayToMap(obj, ids, fieldName, null);
    }


    /**
     * 通过id删除产品详情(假)
     */
    public ProductDetail delFalse(Integer id) throws Exception {
        ProductDetail old = selectById(id);
        if (old != null) {
            int index = updateSelectiveById(new ProductDetail(id, 1));
            if (index == 0) {
                throw new Exception("删除产品详情失败");
            }
        }
        return old;
    }

    /**
     * 通过id集合删除产品详情(假)
     */
    public Integer delFalse(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<ProductDetail> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = updateByIds(new ProductDetail(null, 1), ids);
            if (index != sum) {
                throw new Exception("产品详情批量删除失败");
            }
            return sum;
        }
        return null;
    }


    /**
     * 获得商品条码获得一条数据
     */
    public ProductDetail getOneByCode(String code) throws Exception {
        if (MyObjectUtil.noNullOrEmpty(code)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("code", code);
            List<ProductDetail> list = getByToMap(andToMap);
            int sum = MyObjectUtil.iterableCount(list);
            if (sum == 1) {
                return list.get(0);
            } else if (sum > 1) {
                throw new Exception("产品详情中商品条码必须唯一");
            }
        }
        return null;
    }

    /**
     * 查询某一企业(某一系统模块)（商品条码like)(类型=）所有数据
     */
    public List<ProductDetail> getByEIdMayCodeAndType(Object sysModule, Object enterpriseId, Object code, Object type) {
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            Map<String, Object> toMap = new HashMap<>();
            toMap.put("enterpriseId", enterpriseId);
            toMap.put("sysModule", sysModule);
            toMap.put("type", type);
            Map<String, Object> likeMap = new HashMap<>();
            likeMap.put("code", code);
            return getMayCondition(null, null, likeMap, null, null, null,
                    toMap, null, null, null, null, null, null,
                    null, null, null, null, null,
                    null, null, null, null, null, null,
                    null, null);
        }
        return null;
    }

    /**
     * 通过对象集合获得id集合
     *
     * @param list
     * @return
     */
    public List<Integer> getIdsByList(List<ProductDetail> list) {
        if (list != null) {
            List<Integer> pdIds = list.stream().map(ProductDetail::getId).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(pdIds) > 0) {
                return pdIds;
            }
        }
        return null;
    }


    /**
     * 设置文件属性值
     */
    public void setFiles(ProductDetail obj, List<FileInfo> fileInfos) {
        if (obj != null && fileInfos != null && fileInfos.size() > 0) {
            obj.setPictureList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_1.equals(v.getType())
            ).collect(Collectors.toList()));
            obj.setCertificateList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_2.equals(v.getType())
            ).collect(Collectors.toList()));
        }
    }
}
