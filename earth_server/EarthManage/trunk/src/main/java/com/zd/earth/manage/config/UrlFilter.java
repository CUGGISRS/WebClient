package com.zd.earth.manage.config;

import com.zd.earth.manage.biz.ServerStatusBiz;
import com.zd.earth.manage.entity.ServerStatus;
import com.zd.earth.manage.service.PermissionService;
import com.zd.earth.manage.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * 请求过滤器
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "reqResFilter")
public class UrlFilter implements Filter {
    /**
     * 不需要token的get请求
     */
    @Value("${noVerify.get}")
    private String noVerifyGet;
    /**
     * 不需要token的post请求
     */
    @Value("${noVerify.post}")
    private String noVerifyPost;
    /**
     * 不需要token的put请求
     */
    @Value("${noVerify.put}")
    private String noVerifyPut;
    /**
     * 不需要token的delete请求
     */
    @Value("${noVerify.delete}")
    private String noVerifyDelete;


    @Autowired
    private PermissionService permissionService;
    @Autowired
    private ServerStatusBiz serverStatusBiz;

    @Value("${spring.servlet.multipart.max-request-size}")
    private String maxRequestSize;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;


        //设置允许跨域的配置
        // 这里填写你允许进行跨域的主机ip（正式上线时可以动态配置具体允许的域名和IP）
        response.setHeader("Access-Control-Allow-Origin", "*");
        // 允许的访问方法
        response.setHeader("Access-Control-Allow-Methods","POST, GET, PUT, OPTIONS,HEAD, DELETE, PATCH");
        // Access-Control-Max-Age 用于 CORS 相关配置的缓存  单位为秒，超时时间为1小时
        response.setHeader("Access-Control-Max-Age", "3600");
        //设置允许的请求头
        response.setHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept,Authorization");


        String contentLength=request.getHeader("content-length");
        if(contentLength!=null){
            //判断请求大小是否超过限制
            long length = Long.parseLong(contentLength);
            if(StringHelper.checkMBSize(length,maxRequestSize)){
                writeExMsg("请求过大",response);
                return;
             //   throw new  IOException("请求过大");
            }
        }

        String uri=request.getRequestURI();
        String type=request.getMethod();
        if(StringHelper.isNullOrEmpty(uri)||StringHelper.isNullOrEmpty(type)){
            writeExMsg("请求不完整",response);
            return;
        }

        //判断服务状态
        boolean isStop=false;
        List<ServerStatus> statusList=serverStatusBiz.getByStatus(0);//查询停止的服务接口
        if(statusList!=null){
            for (int i=0,len=statusList.size();i<len;i++){
                ServerStatus item=statusList.get(i);
                String stopUri=item.getUri();
                if(StringHelper.isNullOrEmpty(stopUri)){
                    continue;
                }
                if(uri.startsWith(stopUri)){
                    isStop=true;
                    break;
                }

            }
        }
        if(isStop){
            writeExMsg("服务未启动",response);
            return;
        }

        //判断是否需要验证token
        boolean isIgnore;

        if("GET".equalsIgnoreCase(type)){
            isIgnore=isIgnore(uri,noVerifyGet);
        }else if("POST".equalsIgnoreCase(type)){
            isIgnore=isIgnore(uri,noVerifyPost);
        }else if("PUT".equalsIgnoreCase(type)){
            isIgnore=isIgnore(uri,noVerifyPut);
        }else if("DELETE".equalsIgnoreCase(type)){
            isIgnore=isIgnore(uri,noVerifyDelete);
        }else {
            //其他类型请求直接放行
            isIgnore=true;
        }

        if(isIgnore){
            //放行
            filterChain.doFilter(request,response);
            return;
        }

        //验证
        String token = permissionService.getCurrentToken(request);
        if (StringHelper.isNullOrEmpty(token)) {
            writeExMsg("token不存在",response);
            return;
        }

        if(permissionService.isTokenExpired(token)){
            //放行
            filterChain.doFilter(request,response);
            return;
        }
        writeExMsg("token不合法或已过期",response);

    }

    /**
     * 返回异常信息
     */
    private void writeExMsg(String msg,HttpServletResponse response) throws IOException {
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        printWriter.write("{\"message\":\""+msg+"\",\"status\":500}");
        printWriter.flush();
        printWriter.close();
    }

    /**
     * 判断某一类型请求是否忽略token验证
     */
    private boolean isIgnore(String uri ,String noVerify){

        if(StringHelper.noNullOrEmpty(noVerify)){
            String[] noVerifyS=noVerify.split(",");
            for (int i=0,len=noVerifyS.length;i<len;i++){
                String item=noVerifyS[i];
                if(StringHelper.isNullOrEmpty(item)){
                    continue;
                }
                if(item.endsWith("/**")){
                    int index=item.indexOf("/**");
                    item=item.substring(0,index);
                    if(uri.startsWith(item)){
                        return true;
                    }
                }else {
                  if(item.equals(uri)){
                      return true;
                  }

                }
            }
        }
        return false;
    }
}
