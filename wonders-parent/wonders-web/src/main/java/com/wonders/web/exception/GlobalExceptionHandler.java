package com.wonders.web.exception;

import com.wonders.web.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @ClassName：GlobalExceptionHandler
 * @Description：全局异常处理
 * @Author：wuxx
 * @Date：2020/3/11 12:18
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {




    /**
     * 定义要捕获的异常 可以多个 @ExceptionHandler({})
     *
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
    @ExceptionHandler({CustomException.class})
    public R customExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        CustomException exception = (CustomException) e;
        log.error(e.getMessage());
        return R.error(exception.getCode(), exception.getMessage());
    }

    /**
     * 捕获  RuntimeException 异常
     * TODO  如果你觉得在一个 exceptionHandler 通过  if (e instanceof xxxException) 太麻烦
     * TODO  那么你还可以自己写多个不同的 exceptionHandler 处理不同异常
     *
     * @param request  request
     * @param e        exception
     * @param response response
     * @return 响应结果
     */
    @ExceptionHandler(RuntimeException.class)
    public R runtimeExceptionHandler(HttpServletRequest request, final Exception e, HttpServletResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        RuntimeException exception = (RuntimeException) e;
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error(response.getStatus(), exception.getMessage());
    }

    /**
     * 捕捉404异常,这个方法只在配置
     * spring.mvc.throw-exception-if-no-handler-found=true来后起作用
     *
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    public Object handle(HttpServletRequest request,NoHandlerFoundException e) {
        String msg = "没有【"+request.getMethod()+"】"+request.getRequestURI()+"方法可以访问";
        log.error(e.getMessage());
        e.printStackTrace();
        return R.error(404,msg);
    }


    /**
     * 捕捉其他所有异常
     * @param request
     * @param handlerMethod
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Object globalException(HttpServletRequest request, HandlerMethod handlerMethod, Throwable ex) {
        ex.printStackTrace();
        log.error(ex.toString());
        log.error(ex.getMessage());
        return R.error("服务器内部错误: " + ex.getMessage());
    }


}