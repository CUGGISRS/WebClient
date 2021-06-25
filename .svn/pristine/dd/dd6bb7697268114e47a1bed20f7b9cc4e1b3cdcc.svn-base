package com.zd.earth.manage.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * yml配置文件读取工具类
 */
public class YmlUtil {

    //读取yml的对象
    private static String bootstrap_file = "application.yml";

    private static Map<String,String> result = new HashMap<>();

    /**
     * 根据文件名获取yml的文件内容
     * @param filePath
     * @param keys 第一个参数对应第一个key，第二个参数对应第二个key 比如spring.name下的所有 就是两个参数、
     *              getYmlByFileName(bootstrap_file,"spring", "name");
     * @return
     */
    public static Map<String,String> getYmlByFileName(String filePath, String... keys) {
        result = new HashMap<>();
        if(filePath == null) filePath = bootstrap_file;
        InputStream in = null;
        try {
            //getFile()只支持vfs和file两种，不支持jar ,打包后无法读取，故采用ClassPathResource
          /*  File file = ResourceUtils.getFile(filePath);
            in = new BufferedInputStream(new FileInputStream(file));*/

            ClassPathResource resource = new ClassPathResource(bootstrap_file);
            in= resource.getInputStream();

            Yaml props = new Yaml();
            Object obj = props.loadAs(in,Map.class);
            Map<String,Object> param = (Map<String, Object>) obj;

            for(Map.Entry<String,Object> entry:param.entrySet()){
                String key = entry.getKey();
                Object val = entry.getValue();
                if (keys.length != 0 && !keys[0].equals(key)){
                    continue;
                }
                if(val instanceof Map){
                    forEachYaml(key,(Map<String, Object>) val, 1, keys);
                }else{
                    if(val==null){
                        val="";
                    }
                    result.put(key, val==null?"":val.toString());
                }
            }
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null){
                try {
                    in.close();
                } catch (IOException e) {
                  e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 根据key获取(keys下的)值
     */
    public static String getValue(String key, String... keys) {
        Map<String,String> map = getYmlByFileName(bootstrap_file,keys);
        if(map==null)return null;
        return map.get(key);
    }

    /**
     * 获取(keys下的)值
     */
    public static  Map<String,String> getValueMap( String... keys) {
        return getYmlByFileName(bootstrap_file,keys);
    }


    /**
     * 遍历yml文件，获取map集合
     */
    public static Map<String,String> forEachYaml(String key_str,Map<String, Object> obj, int i, String... keys){
        for(Map.Entry<String,Object> entry:obj.entrySet()){
            String key = entry.getKey();
            Object val = entry.getValue();
            if (keys.length > i && !keys[i].equals(key)){
                continue;
            }
            String str_new = "";
            if(StringUtils.isNotEmpty(key_str)){
                str_new = key_str+ "."+key;
            }else{
                str_new = key;
            }
            if(val instanceof Map){
                forEachYaml(str_new,(Map<String, Object>) val, ++i, keys);
                i--;
            }else{
                result.put(str_new,val==null?"":val.toString());
            }
        }

        return result;
    }

/*

    public static void main(String[] args)  {

        Map<String, String> ymlByFileName = getValueMap();
        Set<Map.Entry<String, String>> entries = ymlByFileName.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            System.out.println(entry.getKey()+"==="+entry.getValue());
        }

        System.out.println(getValue("spring.application.name"));
    }
*/



}
