package com.zd.earth.manage;

import com.alibaba.fastjson.JSONObject;
import com.zd.earth.manage.constants.Constants;
import com.zd.earth.manage.util.HardWareUtils;
import com.zd.earth.manage.util.FileUtil;
import com.zd.earth.manage.util.URLUtil;
import com.zd.earth.manage.util.YmlUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
//注意MapperScan要导tk的包，不然会报NoSuchMethodException
@EnableSwagger2
@MapperScan({"com.zd.earth.manage.mapper"})
public class EarthManageApplication {


    public static void main(String[] args) {
        //系统名称
        String sysName=YmlUtil.getValue("spring.application.name","spring","application","name");

        //授权服务前缀
        String licensePrefix=null;
        //授权码文件父路径
        String licenseFileParentPath=null;
        //授权码文件名
        String licenseFileName=null;
        Map<String,String> map=YmlUtil.getValueMap("license");
        if(map!=null){
            licensePrefix=map.get("license.prefix");
            licenseFileParentPath=map.get("license.fileParentPath");
            licenseFileName=map.get("license.fileName");
        }

        //物理地址
        String localMac= HardWareUtils.getLocalMac();
        //主板序列号
        String motherboardSN=HardWareUtils.getMotherboardSN();
        //cpu序列号
        String cpuSerial=HardWareUtils.getCPUSerial();
        //硬盘序列号
        String hdSerialInfo=HardWareUtils.getHdSerialInfo();

        //获得授权码
        String licenseCode= FileUtil.getJson(licenseFileParentPath,licenseFileName);
        //参数
        Map<String,String> paramsMap=new HashMap<>();

        paramsMap.put("sysName",sysName);
        paramsMap.put("localMac",localMac);
        paramsMap.put("motherboardSN",motherboardSN);
        paramsMap.put("cpuSerial",cpuSerial);
        paramsMap.put("hdSerialInfo",hdSerialInfo);


        if(licenseCode!=null){
            paramsMap.put("licenseCode",licenseCode);
            //验证授权码
            String resultStr = URLUtil.postOrPut(
                    licensePrefix+"/LicenseInfo/verifyLicenseCode",
                    "POST", paramsMap, null, null);
            JSONObject result = URLUtil.string2JSONObject(resultStr, false);
            if (result != null) {
                int code =result.getIntValue(Constants.RESP_STATUS);
                if(code==200){
                    //启动服务
                    SpringApplication.run(EarthManageApplication.class, args);
                    return;
                }else {
                    System.out.println("授权码无效或过期了！");
                }
            }

        }
        //生成申请码
        System.out.println("开始获取申请码");
        String resultStr = URLUtil.postOrPut(
                licensePrefix+"/LicenseInfo/generateApplyCode",
                "POST", paramsMap, null, null);
        JSONObject result = URLUtil.string2JSONObject(resultStr, false);
        if (result != null) {
            int code =result.getIntValue(Constants.RESP_STATUS);
            if(code==200){
                System.out.println("获取申请码成功："+result.getString(Constants.RESP_DATA));
                System.out.println("请用该申请码申请授权码！");
                return;
            }
        }
        System.out.println("获取申请码失败！");

    }

}
