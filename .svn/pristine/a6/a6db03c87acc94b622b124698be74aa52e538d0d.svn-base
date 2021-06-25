package com.github.wxiaoqi.security.zs.sys.biz;

import com.github.wxiaoqi.security.com.sys.constatns.CommonConstants;
import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.util.MyObjectUtil;
import com.github.wxiaoqi.security.zs.sys.entity.BaseInfo;
import com.github.wxiaoqi.security.zs.sys.mapper.BaseInfoMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 基地
 */
@Service
public class BaseInfoBiz extends BaseBiz<BaseInfoMapper, BaseInfo> {
    /**
     * 修改某一企业的数据
     */
    public int updateByEId(BaseInfo obj, Integer eId) {
        if (eId == null) {
            return 0;
        }
        String fieldName = "enterpriseId";
        return updateByFiled(obj, eId, fieldName);
    }

    /**
     * 修改某些id的数据
     */
    public int updateByIds(BaseInfo obj, List<Integer> ids) {
        String fieldName = "id";
        return updateByInFiledMayToMap(obj, ids, fieldName, null);
    }


    /**
     * 通过id删除基地(假)
     */
    public BaseInfo delFalse(Integer id) throws Exception {
        BaseInfo old = selectById(id);
        if (old != null) {
            int index = updateSelectiveById(new BaseInfo(id, 1));
            if (index == 0) {
                throw new Exception("删除基地失败");
            }
        }
        return old;
    }

    /**
     * 通过id集合删除基地(假)
     */
    public Integer delFalse(List<Integer> ids) throws Exception {
        int sum = MyObjectUtil.iterableCount(ids);
        List<BaseInfo> list = batchSelectByIds(ids);
        if (list != null && list.size() == sum) {
            int index = updateByIds(new BaseInfo(null, 1), ids);
            if (index != sum) {
                throw new Exception("基地批量删除失败");
            }
            return sum;
        }
        return null;
    }


    /**
     * 获得某一企业的(某一类型的)基地
     *
     * @param enterpriseId
     * @return
     */
    public List<BaseInfo> getByEIdAndBaseType(Object enterpriseId, Object baseType) {
        if (MyObjectUtil.noNullOrEmpty(enterpriseId)) {
            Map<String, Object> andToMap = new HashMap<>();
            andToMap.put("enterpriseId", enterpriseId);
            andToMap.put("baseType", baseType);
            return getByToMap(andToMap);
        }
        return null;
    }

    /**
     * 设置文件属性值
     */
    public void setFiles(BaseInfo obj, List<FileInfo> fileInfos) {
        if (obj != null && fileInfos != null && fileInfos.size() > 0) {
            obj.setReportPictureList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_1.equals(v.getType())
            ).collect(Collectors.toList()));
            obj.setBasePictureList(fileInfos.stream().filter(v -> CommonConstants.FILE_TYPE_2.equals(v.getType())
            ).collect(Collectors.toList()));
        }
    }

    /**
     * 通过集合获得id集合
     */
    public List<Integer> getIdsByList(List<BaseInfo> list) {
        if (list != null) {
            List<Integer> ids = list.stream().map(BaseInfo::getId).collect(Collectors.toList());
            if (MyObjectUtil.iterableCount(ids) > 0) {
                return ids;
            }
        }
        return null;
    }

}
