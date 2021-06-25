package com.zd.earth.manage.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class URLUtil {
    private static final String BOUNDARY = java.util.UUID.randomUUID().toString();
    private static final String TWO_HYPHENS = "--";
    private static final String LINE_END = "\r\n";

    /**
     * 使用post,put请求,json参数
     */
    public static String postOrPutJson(String url, String method, JSONObject obj, String token, String tokenKey){
      return   connectUrl(url,method,json2String(obj),"application/json;charset=utf-8",token,tokenKey);
    }

    /**
     * 使用post,put请求,键值对参数
     */
    public static String postOrPut(String url, String method, Map<String,String> paramsMap, String token,String tokenKey){
        return   connectUrl(url,method,getPostBodyFormParamMap(paramsMap),null,token,tokenKey);
    }
    /**
     * 使用get,delete请求,键值对参数
     */
    public static String getOrDelete(String url, String method, Map<String,String> paramsMap, String token,String tokenKey){
        return connectUrl(getUrl(url,paramsMap),method,null,null,token,tokenKey);
    }

    /**
     * 使用url请求
     */
    public static String connectUrl(String requestURL, String method, String body,
                                    String contentType,String token,String tokenKey){
        try {
            HttpURLConnection connection =getHttpURLConnection(requestURL,method);

            if(!method.equalsIgnoreCase("GET")){

                // 设置允许输出
                connection.setDoOutput(true);
                connection.setDoInput(true);
                // 设置不用缓存
                connection.setUseCaches(false);
                // 设置维持长连接
                connection.setRequestProperty("Connection", "Keep-Alive");
            }
            //设置参数类型是json格式
            if(contentType!=null){
                connection.setRequestProperty("Content-Type", contentType);
            }
            if(token!=null&&tokenKey!=null){
                connection.setRequestProperty(tokenKey, token);//设置token
            }

            //解决连接异常(安卓)
          /*  StrictMode.setThreadPolicy(
                    new StrictMode.ThreadPolicy.Builder()
                            .detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());*/
            //连接
            connection.connect();

          //  String body = "{\"username\":\""+username+"\",\"password\":\""+password+"\"}";   json格式参数
            //     String body = "userName=zhangsan&password=123456";  键值对参数
            if(body!=null){
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
                writer.write(body);
                writer.close();
            }

            int responseCode = connection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                String result=inToString(inputStream);//将流转换为字符串。
                inputStream.close();
               return  result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 上传文件
     */
    public static String commonUploadFile(String method, String requestURL,
                                              Map<String,File> fileMap, Map<String,List<File>> fileListMap, String fileType,
                                              Map<String,String> paramsMap, Map<String, String> headerMap)  {
        HttpURLConnection conn = null;
        try {
            conn = getHttpURLConnection(requestURL,method);
            setConnection(conn);
            if(headerMap != null){
                setHeader(conn,headerMap);
            }
            conn.connect();
            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            if (paramsMap != null) {
                outputStream.write(getParamsString(paramsMap).getBytes());//上传参数
                outputStream.flush();
            }
            if(fileListMap != null) {
                for (String key : fileListMap.keySet()){
                    List<File> list=fileListMap.get(key);
                    for (File f : list){
                        writeFile(f, key, fileType, outputStream);
                    }
                }
            }else if(fileMap != null){
                for (String key : fileMap.keySet()){
                    writeFile(fileMap.get(key), key, fileType, outputStream);
                }
            }
            byte[] endData = (LINE_END + TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END).getBytes();//写结束标记位
            outputStream.write(endData);
            outputStream.flush();
            int responseCode = conn.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK){
                InputStream inputStream = conn.getInputStream();
                String result=inToString(inputStream);//将流转换为字符串。
                inputStream.close();
                return  result;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传文件时写文件
     */
    private static void writeFile(File file, String fileKey, String fileType, DataOutputStream outputStream) throws IOException {
        outputStream.write(getFileParamsString(file, fileKey, fileType).getBytes());
        outputStream.flush();
        FileInputStream inputStream = new FileInputStream(file);
        byte[] buffer = new byte[1024*2];
        int length ;
        while ((length = inputStream.read(buffer)) != -1){
            outputStream.write(buffer,0,length);
        }
        outputStream.flush();
        inputStream.close();
    }
    /**
     * 上传文件时得到一定格式的拼接字符串
     */
    private static String getFileParamsString(File file, String fileKey, String fileType) {
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(LINE_END);
        strBuf.append(TWO_HYPHENS);
        strBuf.append(BOUNDARY);
        strBuf.append(LINE_END);
        strBuf.append("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\"" + file.getName() + "\"");
        strBuf.append(LINE_END);
        strBuf.append("Content-Type: " + fileType );
        strBuf.append(LINE_END);
        strBuf.append("Content-Lenght: "+file.length());
        strBuf.append(LINE_END);
        strBuf.append(LINE_END);
        return strBuf.toString();
    }

    /**
     * 上传文件时得到拼接的参数字符串
     */
    private static String getParamsString(Map<String, String> paramsMap) {
        StringBuffer strBuf = new StringBuffer();
        for (String key : paramsMap.keySet()){
            strBuf.append(TWO_HYPHENS);
            strBuf.append(BOUNDARY);
            strBuf.append(LINE_END);
            strBuf.append("Content-Disposition: form-data; name=\"" + key + "\"");
            strBuf.append(LINE_END);

            strBuf.append("Content-Type: " + "text/plain" );
            strBuf.append(LINE_END);
            strBuf.append("Content-Length: "+paramsMap.get(key).length());
            strBuf.append(LINE_END);
            strBuf.append(LINE_END);
            strBuf.append(paramsMap.get(key));
            strBuf.append(LINE_END);
        }
        return strBuf.toString();
    }

    /**
     * 得到Connection对象，并进行一些设置
     */
    private static HttpURLConnection getHttpURLConnection(String requestURL,String requestMethod) throws IOException {
        URL url = new URL(requestURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置网络超时
        conn.setConnectTimeout(10*1000);
        conn.setReadTimeout(15*1000);
        conn.setRequestMethod(requestMethod);
        return conn;
    }
    /**
     * 设置请求头
     */
    private static void setHeader(HttpURLConnection conn, Map<String, String> headerMap) {
        if(headerMap != null){
            for (String key: headerMap.keySet()){
                conn.setRequestProperty(key, headerMap.get(key));
            }
        }
    }
    /**
     * 上传文件时设置Connection参数
     */
    private static void setConnection(HttpURLConnection conn) throws ProtocolException {
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type","multipart/form-data; BOUNDARY=" + BOUNDARY);
    }


    /**
     * get请求，将键值对凭接到url上
     */
    public static String getUrl(String path,Map<String, String> paramsMap) {
        if(paramsMap != null){
            path = path+"?";
            for (String key: paramsMap.keySet()){
                String value=paramsMap.get(key);
                if(value!=null){
                    path = path + key+"="+value+"&";
                }
            }
            path = path.substring(0,path.length()-1);
        }
        return path;
    }
    /**
     * 根据键值对参数得到body
     */
    public static String getPostBodyFormParamMap(Map<String, String> params) {
        //throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        } catch (UnsupportedEncodingException e) {
            return null;
        }

    }



    /**
     * 读取流变为String
     */
    private static String inToString(InputStream in) throws IOException {
        //读取输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }



    public static Map<String,String> getParamsMap(JSONObject object){
        if(object!=null){
            Map<String,String> map=new HashMap<>();
            Iterator<String> keys = object.keySet().iterator();
            while (keys.hasNext()) {
                String teams = keys.next();
                String teamsInfo = object.getString(teams);
                map.put(teams,teamsInfo);
            }
            return map;
        }
        return null;
    }

    public static String json2String(JSONObject obj){
        return obj.toString();
    }


    /**
     * 字符串转化JSONObject
     */
    public static JSONObject string2JSONObject(String str, boolean retainRoot)  {
        if(str==null){
            return null;
        }
        JSONObject obj;
        try{
            //xml格式字符串
            obj=XmlTool.strToJSONObject(str,retainRoot);
        }catch (NullPointerException e){
            //json格式字符串
            obj= JSONObject.parseObject(str);
        }
        return obj;
    }
    /**
     * 字符串转化JSONArray
     */
    public static JSONArray string2JSONArray(String str, String xmlRootKey){
        if(str==null){
            return null;
        }
        JSONArray arr;
        try{
            //xml格式字符串
          JSONObject obj=XmlTool.strToJSONObject(str,false);
          arr=obj.getJSONArray(xmlRootKey);
        }catch (NullPointerException e){
            //json格式字符串
            arr= JSONArray.parseArray(str);
        }
        return arr;
    }






}
