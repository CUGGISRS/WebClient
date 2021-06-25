package com.github.wxiaoqi.security.jd.sys.feign;

import com.github.wxiaoqi.security.com.sys.entity.FileInfo;
import com.github.wxiaoqi.security.common.handler.MultipartSupportConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件远程接口
 */
@FeignClient(value = "ace-com-sys", url = "${feign.comUrl}", configuration = {MultipartSupportConfig.class})
@RequestMapping("FileInfo")
public interface IFileInfo {
    /**
     * 通过表名和表id获得数据
     */
    @GetMapping("getByFormIdAndFormName")
    public List<FileInfo> getByFormIdAndFormName(@RequestParam("formId") Integer formId,
                                                 @RequestParam("formName") String formName);

    /**
     * 通过表名和表id集合获得数据
     */
    @PostMapping("getByFormIdsAndFormName")
    public List<FileInfo> getByFormIdsAndFormName(@RequestBody List<Integer> formIds,
                                                  @RequestParam("formName") String formName);

    /**
     * 通过表名和表id删除数据
     */
    @DeleteMapping("delByFormIdAndFormName")
    public Integer delByFormIdAndFormName(@RequestParam("formId") Integer formId,
                                          @RequestParam("formName") String formName);

    /**
     * 通过表名和表id集合删除数据
     */
    @PostMapping("delByFormIdsAndFormName")
    public Integer delByFormIdsAndFormName(@RequestBody List<Integer> formIds,
                                           @RequestParam("formName") String formName);


    @PostMapping(value = "uploadFile", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFile(@RequestPart(value = "file", required = false) MultipartFile file
            , @RequestParam("dir") String dir,
                             @RequestParam("addFileName") String addFileName);


    /**
     * 通过访问url集合删除文件
     */
    @PostMapping("delFileByUrls")
    public void delFileByUrls(@RequestBody List<String> urls);


    /**
     * 通过id集合查询数据
     */
    @PostMapping("selectByIds")
    public List<FileInfo> selectByIds(@RequestBody List<Integer> ids);


    /**
     * 通过id集合删除数据
     */
    @PostMapping("deleteByIds")
    public Integer deleteByIds(@RequestBody List<Integer> ids);

    /**
     * 批量插入,不包括主键，会自动添加自增主键到集合中,属性值为null也会插入
     */
    @PostMapping("insertByList")
    public Integer insertByList(@RequestBody List<FileInfo> fileInfos);


}
