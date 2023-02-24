package com.example.sstest.handler;

import com.example.sstest.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public Result handlerException(Exception e) { //e 接收异常信息
        //获取异常信息，存放到 ResponseResult 的 msg 属性中
        String message = e.getMessage();
        //把 ResponseResult 作为返回值返回，要求到时候转换 json 存入到响应体中
        return Result.failed(300, message);
    }

}