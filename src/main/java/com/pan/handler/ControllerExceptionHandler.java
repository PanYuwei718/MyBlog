package com.pan.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {  //处理异常，拦截404，跳转到自定义的404
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class) //表示这个方法作为异常处理，括号里的表示拦截的错误信息只要是Exception级别的都可以
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e)throws Exception{

        logger.error("Request URL:{},Exception:{}",request.getRequestURI(),e);  //记录异常信息
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;        //附带了状态码的，让springboot帮我们跳转到500
        }
        ModelAndView mv = new ModelAndView();;
        mv.addObject("url",request.getRequestURI());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return mv;
    }
}
