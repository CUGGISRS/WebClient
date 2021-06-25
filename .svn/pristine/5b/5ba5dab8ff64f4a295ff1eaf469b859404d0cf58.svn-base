package com.zd.earth.manage.rest;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zd.earth.manage.biz.ServerConfigBiz;
import com.zd.earth.manage.config.MyWebMvcConfigurer;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import com.zd.earth.manage.util.FileUtil;
import com.zd.earth.manage.util.StringHelper;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "文件或文件夹操作")
@RestController
@RequestMapping("FileDirOpt")
public class FileDirOptRest {

    @Autowired
    private ServerConfigBiz serverConfigBiz;
    @Autowired
    private MyWebMvcConfigurer myWebMvcConfigurer;

    @ApiOperation("上传文件到服务器")
    @PostMapping("/uploadAnything")
    public ObjectRestResponse<String> uploadAnything(@ApiParam(name="file",value="文件")MultipartFile file)  {
        if(file!=null){
            String folder="anything/";
            String url=FileUtil.uploadFile(file, myWebMvcConfigurer.getSavePath()+folder,null);
            if(url!=null){
                url=myWebMvcConfigurer.getVisitPrefix()+folder+url;
                return new ObjectRestResponse<>(StatusCode.SUCCESS,true,url);
            }
        }
        return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
    }

    @ApiOperation("获得某一目录下所有文件夹和指定的文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "path", value = "目录路径(非空，\"\"为根路径)", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "noExistDir", value = "是否不保留文件夹(默认false)", paramType = "query",dataType = "boolean"),
            @ApiImplicitParam(name = "existAllFile", value = "是否保留所有类型的文件(默认false)", paramType = "query",dataType = "boolean"),
            @ApiImplicitParam(name = "fileTypes", value = "特定保留文件类型(后缀)数组（忽略大小写匹配，如txt(TXT),rar）", paramType = "query",
                    dataType = "String",allowMultiple = true),
            @ApiImplicitParam(name = "existNoSuffixFIle", value = "是否保留无后缀的文件(默认false)", paramType = "query",dataType = "boolean")
    })
    @GetMapping("getDirsMayFilesJSON")
    public ObjectRestResponse<JSONArray> getDirsMayFilesJSON( String path,String [] fileTypes
            , boolean existAllFile,boolean noExistDir,boolean existNoSuffixFIle) throws Exception {
        JSONArray json = FileUtil.getDirsMayFilesJSON(path,noExistDir,existAllFile,fileTypes,existNoSuffixFIle);
        if(json==null){
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true,json);
    }

    @ApiOperation("获得json存储目录下所有json文件")
    @GetMapping("getAllJsonFileJSON")
    public ObjectRestResponse<JSONArray> getAllJsonFileJSON() throws Exception {
        //获得数据库中json存储目录路径
        String path=serverConfigBiz.getJsonFilePath();
        JSONArray json = FileUtil.getDirsMayFilesJSON(path,true,
                false,null,true);
        if(json==null){
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true,json);
    }


    @ApiOperation("在json存储目录下创建内容为空的json文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名（非空）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "isReplace", value = "能否直接替换同名文件(默认false)", paramType = "query",dataType = "boolean")
    })
    @PostMapping("writeJsonFile")
    public ObjectRestResponse<Boolean> writeJsonFile(String fileName,boolean isReplace)  {
        //获得数据库中json存储目录路径
        String path=serverConfigBiz.getJsonFilePath();
        String errorInfo=FileUtil.writeJsonFile(path,fileName,null,isReplace);
        if(errorInfo==null){
            return new ObjectRestResponse<>(StatusCode.SUCCESS,true);
        }
        return new ObjectRestResponse<>(StatusCode.FAIL,errorInfo);
    }


    @ApiOperation("在json存储目录下删除某一文件名的json文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名（非空）", paramType = "query",dataType = "String")
    })
    @PostMapping("delJsonFile")
    public ObjectRestResponse<Boolean> delJsonFile(String fileName) throws Exception {
        //获得数据库中json存储目录路径
        String path=serverConfigBiz.getJsonFilePath();
        boolean result= FileUtil.deleteFile(path,fileName);
        if(result){
            return new ObjectRestResponse<>(StatusCode.SUCCESS,true);
        }
       return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
    }


    @ApiOperation("获得json存储目录下json文件的内容")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "fileName", value = "文件名（非空）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "nodeKey", value = "节点属性名（模糊查询条件）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "nodeValue", value = "节点属性值（模糊查询条件）", paramType = "query",dataType = "String")
    })
    @PostMapping("getJsonByPath")
    public ObjectRestResponse<JSONArray> getJsonByPath(String fileName,String nodeKey,String nodeValue) throws Exception {
        //获得数据库中json存储目录路径
        String path=serverConfigBiz.getJsonFilePath();
        String jsonStr=FileUtil.getJson(path,fileName);
        JSONArray arr=JSONArray.parseArray(jsonStr);
        //通过某一节点属性（模糊查询）过滤
        arr=FileUtil.filterByProperty(nodeKey,nodeValue,arr);
        if (arr==null){
            return new ObjectRestResponse<>(StatusCode.REQUEST_PARAM_ERROR,false);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true,arr);
    }


    @ApiOperation("给json存储目录下json文件添加json节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的json对象，其他query参数为其属性",  paramType = "body",required = true,dataType = "JSONObject"),
            @ApiImplicitParam(name = "pId", value = "父节点唯一编号（null代表根节点）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName", value = "文件名（非空）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "title", value = "标题（从title参数以下都为节点属性，可不选，可新增，请根据实际节点属性要求传递！！）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "expand", value = "能否展开", paramType = "query",dataType = "boolean"),
            @ApiImplicitParam(name = "root", value = "是否为根节点", paramType = "query",dataType = "boolean"),
            @ApiImplicitParam(name = "data", value = "数据", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "view", value = "能否查看", paramType = "query",dataType = "boolean"),
            @ApiImplicitParam(name = "checked", value = "是否选中", paramType = "query",dataType = "boolean")
    })
    @PostMapping("addJsonNode")
    public ObjectRestResponse<Boolean> addJsonNode(@RequestBody JSONObject obj) throws Exception {
        //获得数据库中json存储目录路径
        String path=serverConfigBiz.getJsonFilePath();
        String pId=obj.getString("pId");
        String fileName=obj.getString("fileName");
        obj.remove("pId");
        obj.remove("fileName");
        String errorInfo=FileUtil.addJsonNode(path,fileName,pId,obj);
        if(errorInfo!=null){
            return new ObjectRestResponse<>(StatusCode.FAIL,errorInfo);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true);
    }


    @ApiOperation("给json存储目录下json文件修改json节点属性(不包括子节点)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的json对象，其他query参数为其属性",  paramType = "body",required = true,dataType = "JSONObject"),
            @ApiImplicitParam(name = "id", value = "节点唯一编号（非空）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName", value = "文件名（非空）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "title", value = "标题（从title参数以下都为节点属性，可不选，可新增，请根据实际节点属性要求传递！！）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "expand", value = "能否展开",  paramType = "query",dataType = "boolean"),
            @ApiImplicitParam(name = "root", value = "是否为根节点",  paramType = "query",dataType = "boolean"),
            @ApiImplicitParam(name = "data", value = "数据", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "view", value = "能否查看", paramType = "query", dataType = "boolean"),
            @ApiImplicitParam(name = "checked", value = "是否选中", paramType = "query",dataType = "boolean")
    })
    @PutMapping("updJsonNode")
    public ObjectRestResponse<Boolean> updJsonNode(@RequestBody JSONObject obj) throws Exception {
        //获得数据库中json存储目录路径
        String path=serverConfigBiz.getJsonFilePath();
        String id=obj.getString("id");
        String fileName=obj.getString("fileName");
        obj.remove("fileName");
        obj.remove("id");
        String errorInfo=FileUtil.updJsonNode(path,fileName,id,obj);
        if(errorInfo!=null){
            return new ObjectRestResponse<>(StatusCode.FAIL,errorInfo);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true);
    }


    @ApiOperation("给json存储目录下json文件删除json节点(包括子节点)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "节点唯一编号（非空）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName", value = "文件名（非空）", paramType = "query",dataType = "String")
    })
    @DeleteMapping("delJsonNode")
    public ObjectRestResponse<Boolean> delJsonNode(String fileName,String id) throws Exception {
        //获得数据库中json存储目录路径
        String path=serverConfigBiz.getJsonFilePath();
        String errorInfo=FileUtil.delJsonNode(path,fileName,id);
        if(errorInfo!=null){
            return new ObjectRestResponse<>(StatusCode.FAIL,errorInfo);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true);
    }

    @ApiOperation("给json存储目录下json文件的父节点中上下移动选中的子节点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "obj", value = "接收参数的json对象，其他query参数为其属性",  paramType = "body",required = true,dataType = "JSONObject"),
            @ApiImplicitParam(name = "id", value = "节点唯一编号（非空）", paramType = "query",dataType = "String"),
            @ApiImplicitParam(name = "fileName", value = "文件名（非空）", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "moveNum", value = "移动行数（正数下移，负数上移）", paramType = "query", dataType = "int")
    })
    @PutMapping("moveJsonNode")
    public ObjectRestResponse<Boolean> moveJsonNode(@RequestBody JSONObject obj)  {
        //获得数据库中json存储目录路径
        String path=serverConfigBiz.getJsonFilePath();
        String id=obj.getString("id");
        String fileName=obj.getString("fileName");

        Integer moveNum=obj.getInteger("moveNum");

        String errorInfo=FileUtil.moveJsonNode(path,fileName,id,moveNum);
        if(errorInfo!=null){
            return new ObjectRestResponse<>(StatusCode.FAIL,errorInfo);
        }
        return new ObjectRestResponse<>(StatusCode.SUCCESS,true);
    }



}
