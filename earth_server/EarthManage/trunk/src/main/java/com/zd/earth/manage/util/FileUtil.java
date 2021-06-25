package com.zd.earth.manage.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zd.earth.manage.constants.Constants;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     * 给某一路径下json文件删除json节点
     */
    public static String delJsonNode(String path, String fileName, String id) {
        if (StringHelper.isNullOrEmpty(id)) {
            return "节点唯一编号不能为空";
        }
        JSONArray arr = JSONArray.parseArray(FileUtil.getJson(path, fileName));
        if (arr == null) {
            return "原json数据不能为空";
        }
        //获得递进修改子节点
        recursionDelNode(id, arr);
        // System.out.println(arr.toJSONString());
        return writeJsonFile(path, fileName, arr.toJSONString(), true);
    }

    /**
     * 递归删除节点
     */
    public static void recursionDelNode(String id, JSONArray treeNodes) {
        for (int i = 0, size = treeNodes.size(); i < size; i++) {
            JSONObject it = treeNodes.getJSONObject(i);
            JSONArray children = it.getJSONArray(Constants.JSON_NODE_CHILD);
            if (id.equals(it.getString(Constants.JSON_NODE_UNIQUE))) {
                treeNodes.remove(i);//删除整个原节点
                return;
            } else {
                if (children != null) {
                    recursionDelNode(id, children);
                }
            }
        }
    }

    /**
     * 在某一路径下json文件的父节点中上下移动选中的子节点
     */
    public static String moveJsonNode(String path, String fileName, String id, Integer moveNum) {
        if (StringHelper.isNullOrEmpty(id)) {
            return "节点唯一编号不能为空";
        }

        if (moveNum == null) {
            return "移动行数不能为空";
        }
        JSONArray arr = JSONArray.parseArray(FileUtil.getJson(path, fileName));
        if (arr == null) {
            return "原json数据不能为空";
        }
        //递进移动节点
        recursionUpdNode(id, arr, moveNum.intValue());
        // System.out.println(arr.toJSONString());
        return writeJsonFile(path, fileName, arr.toJSONString(), true);
    }


    /**
     * 递进移动节点
     */
    public static void recursionUpdNode(String id, JSONArray treeNodes, int moveNum) {
        for (int i = 0, size = treeNodes.size(); i < size; i++) {
            JSONObject it = treeNodes.getJSONObject(i);
            JSONArray children = it.getJSONArray(Constants.JSON_NODE_CHILD);
            if (id.equals(it.getString(Constants.JSON_NODE_UNIQUE))) {
                int moveAfterIndex= i + moveNum;
                if (moveNum < 0) { //上移
                    if (i == 0) {
                        return;
                    }

                    if (moveAfterIndex < 0) {
                        moveAfterIndex = 0;
                    }

                    for (int j = i; j > moveAfterIndex; j--) {
                        JSONObject item = treeNodes.getJSONObject(j - 1);
                        treeNodes.set(j, item);
                    }
                    treeNodes.set(moveAfterIndex, it);


                } else if (moveNum > 0) {//下移
                    int lastIndex=size-1;
                    if(i==lastIndex){
                        return;
                    }

                    if(moveAfterIndex>lastIndex){
                        moveAfterIndex=lastIndex;
                    }

                    for (int j = i; j < moveAfterIndex; j++) {
                        JSONObject item = treeNodes.getJSONObject(j + 1);
                        treeNodes.set(j, item);
                    }
                    treeNodes.set(moveAfterIndex, it);

                }
                return;
            } else {
                if (children != null) {
                    recursionUpdNode(id, children, moveNum);
                }
            }
        }
    }


    /**
     * 给某一路径下json文件修改json节点
     */
    public static String updJsonNode(String path, String fileName, String id, JSONObject obj) {
        if (StringHelper.isNullOrEmpty(id)) {
            return "节点唯一编号不能为空";
        }
        JSONArray arr = JSONArray.parseArray(FileUtil.getJson(path, fileName));
        if (arr == null) {
            return "原json数据不能为空";
        }
        //获得递进修改子节点
        recursionUpdNode(id, arr, obj);
        // System.out.println(arr.toJSONString());
        return writeJsonFile(path, fileName, arr.toJSONString(), true);
    }

    /**
     * 递归修改节点
     */
    public static void recursionUpdNode(String id, JSONArray treeNodes, JSONObject obj) {
        for (int i = 0, size = treeNodes.size(); i < size; i++) {
            JSONObject it = treeNodes.getJSONObject(i);
            JSONArray children = it.getJSONArray(Constants.JSON_NODE_CHILD);
            if (id.equals(it.getString(Constants.JSON_NODE_UNIQUE))) {
                //   treeNodes.remove(i);//删除整个原节点
                Set<String> keySet = it.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (Constants.JSON_NODE_CHILD.equals(key) || Constants.JSON_NODE_UNIQUE.equals(key)) {
                        continue;
                    }
                    iterator.remove();//删除除子节点和编号外属性的原属性
                }
                it.putAll(obj);//修改
                return;
            } else {
                if (children != null) {
                    recursionUpdNode(id, children, obj);
                }
            }
        }
    }

    /**
     * 给某一路径下json文件添加json节点
     */
    public static String addJsonNode(String path, String fileName, String pId, JSONObject obj) {
        String jsonStr = FileUtil.getJson(path, fileName);
        if (jsonStr == null) {
            return "文件路径错误";
        }
        JSONArray arr = JSONArray.parseArray(jsonStr);
        if (arr == null) {
            arr = new JSONArray();
        }
        if (StringHelper.isNullOrEmpty(pId)) {
            int maxId = 0;
            //添加根节点
            for (int i = 0, len = arr.size(); i < len; i++) {
                JSONObject json = arr.getJSONObject(i);
                String idStr = json.getString(Constants.JSON_NODE_UNIQUE);
                int id = Integer.parseInt(idStr);
                if (id > maxId) {
                    //获得最大根节点编号
                    maxId = id;
                }
            }
            maxId++;
            obj.put(Constants.JSON_NODE_UNIQUE, String.valueOf(maxId));
            arr.add(obj);
        } else {
            //获得递进添加子节点
            recursionAddNode(pId, arr, obj);
        }
        // System.out.println(arr.toJSONString());
        return writeJsonFile(path, fileName, arr.toJSONString(), true);
    }

    /**
     * 递归添加节点
     */
    public static void recursionAddNode(String pId, JSONArray treeNodes, JSONObject obj) {
        for (int i = 0, size = treeNodes.size(); i < size; i++) {
            JSONObject it = treeNodes.getJSONObject(i);
            JSONArray children = it.getJSONArray(Constants.JSON_NODE_CHILD);
            if (pId.equals(it.getString(Constants.JSON_NODE_UNIQUE))) {
                if (children == null) {
                    children = new JSONArray();
                }
                int maxId = 0;
                //添加子节点
                for (int j = 0, len = children.size(); j < len; j++) {
                    JSONObject json = children.getJSONObject(j);
                    String idStr = json.getString(Constants.JSON_NODE_UNIQUE);
                    int index = idStr.lastIndexOf(Constants.JSON_NODE_UNIQUE_SEPARATOR);
                    int jsonId = Integer.parseInt(idStr.substring(index + 1));
                    if (jsonId > maxId) {
                        //获得父节点下最大子节点编号
                        maxId = jsonId;
                    }
                }
                maxId++;
                String id = pId + Constants.JSON_NODE_UNIQUE_SEPARATOR + maxId;
                obj.put(Constants.JSON_NODE_UNIQUE, id);
                children.add(obj);
                it.put(Constants.JSON_NODE_CHILD, children);
                return;
            } else {
                if (children != null) {
                    recursionAddNode(pId, children, obj);
                }
            }
        }
    }


    /**
     * 删除文件
     */
    public static boolean deleteFile(File file) {
        if (file != null && file.exists()) {
            return file.delete();
        }
        return false;
    }


    /**
     * 删除某一目录某一文件名对应的文件
     */
    public static boolean deleteFile(String path, String fileName) {
        if (path != null && StringHelper.noNullOrEmpty(fileName)) {
            File file = new File(path, fileName);
            return deleteFile(file);
        }
        return false;
    }


    /**
     * 写出json文件
     */
    public static String writeJsonFile(String path, String fileName, String newJsonString, boolean isReplace) {
        if (path == null || StringHelper.isNullOrEmpty(fileName)) {
            return "目录为null或者文件名为空";
        }
        FileWriter fw = null;
        PrintWriter out = null;
        try {
            File folder = new File(path);
            //判断文件父目录是否存在
            if (!folder.exists() || !folder.isDirectory()) {
                //删除不是文件夹的同名文件后创建文件夹
                folder.delete();
                folder.mkdirs();
            }
            File dest = new File(path, fileName);
            //判断是否直接替换同名文件
            if (!isReplace && dest.exists()) {
                return "已存在同名文件或文件夹";
            }
           /* if(dest.exists()){
                if(dest.isFile()){//是否为文件
                    if(!isReplace){
                        return "已存在同名文件";
                    }
                }else {
                    //删除原路径，新建文件新路径
                    dest.delete();
                    dest.createNewFile();
                }
            }*/
            fw = new FileWriter(dest);
            out = new PrintWriter(fw);
            out.write(newJsonString == null ? "" : newJsonString);
            out.println();

            fw.close();
            out.close();
            return null;
        } catch (Exception e) {
            try {
                fw.close();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        }
        return "写出文件失败";
    }


    /**
     * 读取json文件
     */
    public static String getJson(String path, String fileName) {
        if (StringHelper.isNullOrEmpty(path) || StringHelper.isNullOrEmpty(fileName)) {
            return null;
        }
        try {
            File file = new File(path, fileName);
            //路径下文件存在，且是普通文件
            if (file.exists() && file.isFile()) {
                FileReader fileReader = new FileReader(file);
                //获取的语言环境的编码
                Charset charset = Charset.defaultCharset();
                Reader reader = new InputStreamReader(new FileInputStream(file), charset);
                int ch;
                StringBuffer sb = new StringBuffer();
                while ((ch = reader.read()) != -1) {
                    sb.append((char) ch);
                }
                fileReader.close();
                reader.close();
                return sb.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过某一节点属性（模糊查询）过滤
     */
    public static JSONArray filterByProperty(String nodeKey, String nodeValue, JSONArray oldArr) {
        if (oldArr == null) {
            return null;
        }
        if (StringHelper.isNullOrEmpty(nodeKey) || StringHelper.isNullOrEmpty(nodeValue)) {
            return oldArr;
        }
        JSONArray arr = new JSONArray();
        recursionFilterNode(nodeKey, nodeValue, oldArr, arr);
        return arr;
    }

    /**
     * 递归匹配符合条件的节点
     */
    public static void recursionFilterNode(String nodeKey, String nodeValue, JSONArray treeNodes, JSONArray arr) {

        for (int i = 0, size = treeNodes.size(); i < size; i++) {
            JSONObject it = treeNodes.getJSONObject(i);
            String value = it.getString(nodeKey);

            JSONArray children = it.getJSONArray(Constants.JSON_NODE_CHILD);
            //模糊匹配
            if (value != null && value.indexOf(nodeValue) != -1) {
                //移除子节点
                it.remove(Constants.JSON_NODE_CHILD);
                arr.add(it);
            }
            if (children != null) {
                recursionFilterNode(nodeKey, nodeValue, children, arr);
            }
        }
    }


    /**
     * 读取文件夾,得到目录下所有文件夹和指定的文件
     */
    public static JSONArray getDirsMayFilesJSON(String path, boolean noExistDir,
                                                boolean existAllFile, String[] fileTypes, boolean existNoSuffixFIle) throws Exception {
        if (path == null) {
            return null;
        }
        JSONArray arr = new JSONArray();
        //""为根路径
        if ("".equals(path)) {
            File[] fs = File.listRoots();
            String os = System.getProperty("os.name");
            for (int i = 0, len = fs.length; i < len; i++) {
                File file = fs[i];
                if (!file.canRead() || !file.canWrite()) continue;
                String fPath = file.getPath();
                //判断是否为windows系统
                if (os.toLowerCase().indexOf("windows") == -1) {

                } else {
                    fPath = fPath.substring(0, fPath.length() - 1);
                }
                JSONObject obj = new JSONObject(true);
                obj.put("name", fPath);
                obj.put("type", "root");
                arr.add(obj);
            }
        } else { //給定路径
            if (path.indexOf("/") == -1) {
                path += "/";//盘符，如C:,不加/有时查不出文件列表
            }
            File file = new File(path);
            if (!file.exists()) {
                throw new Exception("该路径不存在");
            } else {
                File[] fileList = file.listFiles();
                if (fileList != null) {
                    for (int i = 0, len = fileList.length; i < len; i++) {
                        File f = fileList[i];
                        if (f.isHidden()) continue;
                        if (!f.canRead() || !f.canWrite()) continue;
                        if (f.isDirectory()) {
                            if (!noExistDir) {//不展示文件夹
                                JSONObject obj = new JSONObject(true);
                                String dir = f.getName();
                                obj.put("name", dir);
                                obj.put("type", "dir");
                                arr.add(obj);
                            }
                        } else if (f.isFile()) {
                            String fileStr = f.getName();
                            if (existAllFile) {//展示所有文件
                                JSONObject obj = new JSONObject(true);
                                obj.put("name", fileStr);
                                obj.put("type", "file");
                                arr.add(obj);

                            } else {
                                int suffixIndex = fileStr.lastIndexOf(".");
                                //展示某些后缀的文件
                                if (fileTypes != null) {
                                    int ftl = fileTypes.length;
                                    if (ftl > 0) {
                                        if (suffixIndex != -1) {
                                            String suffix = fileStr.substring(suffixIndex + 1);
                                            for (int j = 0; j < ftl; j++) {
                                                String item = fileTypes[j];
                                                if (item.equalsIgnoreCase(suffix)) {
                                                    JSONObject obj = new JSONObject(true);
                                                    obj.put("name", fileStr);
                                                    obj.put("type", "file");
                                                    arr.add(obj);
                                                }
                                            }
                                        }
                                    }
                                }
                                //展示无后缀文件
                                if (existNoSuffixFIle && suffixIndex == -1) {
                                    JSONObject obj = new JSONObject(true);
                                    obj.put("name", fileStr);
                                    obj.put("type", "file");
                                    arr.add(obj);
                                }
                            }
                        }
                    }
                }

            }
        }
        return arr;
    }



    /**
     * 上传文件到某一文件夹返回在该文件夹中的新文件
     */
    public static File uploadFile0(MultipartFile file, String path, String addFileName)  {
        //判断文件是否为空
        if (file==null||file.isEmpty()) {
            return null;
        }
        // 获取文件名
        String fileName = file.getOriginalFilename();

        //有些ie浏览器获得的是文件的全路径，需要将其截掉
        if(fileName!=null){
            Integer index=fileName.lastIndexOf("/");
            if(index!=-1){
                fileName=fileName.substring(index+1);
            }
            Integer j=fileName.lastIndexOf("\\");
            if(j!=-1){
                fileName=fileName.substring(j+1);
            }
        }

        if(addFileName!=null){
            fileName = addFileName+"__"+ fileName;
        }
        //替换非法字符
        fileName=StringHelper.replaceIllegalChar(fileName," ");

        fileName = StringHelper.getUniqueCode()+"__" + fileName;


        File folder = new File(path);
        //判断文件父目录是否存在
        if (!folder.exists()||!folder.isDirectory()) {
            //删除不是文件夹的同名文件后创建文件夹
            folder.delete();
            folder.mkdirs();
        }
        File dest=new File(path,fileName);
        try{
            //上传文件
            file.transferTo(dest);  //直接去将内存中的文件通过输出流写出到指定的file
        }catch (IOException e){
         return null;
        }
        //url=协议+ip+端口+*     项目启动时访问文件的url
        //    String url=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + visitPrefix+fileName;
        return dest;
    }

    /**
     * 上传文件到某一文件夹返回在该文件夹中的新文件名
     */
    public static String uploadFile(MultipartFile file,  String path,String addFileName)  {
        File dest=uploadFile0(file,path,addFileName);
        if(dest!=null){
            return dest.getName();
        }
        return null;
    }
}
