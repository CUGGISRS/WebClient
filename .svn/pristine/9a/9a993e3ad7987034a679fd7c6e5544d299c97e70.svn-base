package com.github.wxiaoqi.security.dzsw.sys.biz;

import com.github.wxiaoqi.security.common.biz.BaseBiz;
import com.github.wxiaoqi.security.common.msg.ObjectRestResponse;
import com.github.wxiaoqi.security.common.msg.StatusCode;
import com.github.wxiaoqi.security.dzsw.sys.constants.CommonConstants;
import com.github.wxiaoqi.security.dzsw.sys.entity.CompanyInfo;
import com.github.wxiaoqi.security.dzsw.sys.mapper.CompanyInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class CompanyInfoBiz extends BaseBiz<CompanyInfoMapper, CompanyInfo> {

    @Autowired
    private FileInfoBiz fileInfoBiz;

    public ObjectRestResponse addNewCompanyInfo(Map<String, String> map, MultipartFile[] multipartFile) {
        try {
            CompanyInfo companyInfo = new CompanyInfo();
            // 企业名称
            if (map.containsKey("companyName")) {
                companyInfo.setCompanyName(map.get("companyName"));
            }
            // 企业类型
            if (map.containsKey("companyType")) {
                companyInfo.setCompanyType(map.get("companyType"));
            }
            if (map.containsKey("chargeMan")) {
                companyInfo.setChargeMan(map.get("chargeMan"));
            }
            if (map.containsKey("phone")) {
                companyInfo.setPhone(map.get("phone"));
            }
            if (map.containsKey("registMoney")) {
                companyInfo.setRegistMoney(map.get("registMoney"));
            }
            if (map.containsKey("businessCode")) {
                companyInfo.setBusinessCode(map.get("businessCode"));
            }
            if (map.containsKey("industry")) {
                companyInfo.setIndustry(map.get("industry"));
            }
            if (map.containsKey("introduction")) {
                companyInfo.setIntroduction(map.get("introduction"));
            }
            if (map.containsKey("place")) {
                companyInfo.setPlace(map.get("place"));
            }
            companyInfo.setIsDeleted(0);
            int affect = mapper.insertSelective(companyInfo);
            if (affect == 0) {
                return new ObjectRestResponse(StatusCode.FAIL, false);
            }
            int insertId = companyInfo.getId();
            // 上传文件
            fileInfoBiz.addOneFile(multipartFile, CommonConstants.ULTABLE_ZCI, insertId);
            return new ObjectRestResponse(StatusCode.SUCCESS, insertId);
        } catch (Exception ex) {
            return new ObjectRestResponse(StatusCode.FAIL, ex.getMessage());
        }
    }

}
