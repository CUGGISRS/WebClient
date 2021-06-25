package com.zd.earth.manage.config;

import com.alibaba.fastjson.JSONObject;
import com.zd.earth.manage.constants.Constants;
import com.zd.earth.manage.msg.ObjectRestResponse;
import com.zd.earth.manage.msg.StatusCode;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
/**
 * 记录日志和管理事务提交和回滚
 */
@Aspect
@Component
public class LogAspect {


    @Autowired
    DataSourceTransactionManager dataSourceTransactionManager;
    @Autowired
    TransactionDefinition transactionDefinition;


    /**
     * 设置切入点
     */
    @Pointcut("execution(* com.zd.earth.manage.config.RestExceptionHandler.*(..))||execution(* com.zd.earth.manage.common.BaseRest.*(..))||execution(* com.zd.earth.manage.rest.*.*(..))")
    private void myPointcut() {
    }

    /**
     * 环绕通知
     */
    @Transactional(rollbackFor = Exception.class)
    @Around("myPointcut()")
    public Object saveSysLog(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {


        //开启事务
        TransactionStatus transactionStatus = dataSourceTransactionManager.getTransaction(transactionDefinition);

        Object result ;
        try {

            result = proceedingJoinPoint.proceed();
            if (result != null) {
                String resultStr = JSONObject.toJSONString(result);
                JSONObject obj = JSONObject.parseObject(resultStr);
                Integer status = obj.getInteger(Constants.RESP_STATUS);
                if (Constants.RESP_STATUS_SUCCESS.equals(status)) {
                    dataSourceTransactionManager.commit(transactionStatus);//提交
                    return result;
                }
            }

        } catch (Exception ex) {
          //  throw ex;//抛出捕获的异常信息(信息过多)
            result=new ObjectRestResponse<>(StatusCode.FAIL,ex.getMessage());
        }
        //手动回滚，这样上层就无需去处理异常,返回自定义异常信息(注意不要在其他地方重复加 @Transactional，会导致报错)
        dataSourceTransactionManager.rollback(transactionStatus);
       //  TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return result;
    }


}
