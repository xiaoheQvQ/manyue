package com.hsx.handler;

import com.hsx.exception.ConditionException;
import com.hsx.pojo.JsonResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
//最高优先级
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e){
        String eMessage = e.getMessage();
        if(e instanceof ConditionException){
            String code = ((ConditionException) e).getCode();
            return new JsonResponse<>(code,eMessage);
        }else{
            return new JsonResponse<>("500",eMessage);
        }
    }
}
