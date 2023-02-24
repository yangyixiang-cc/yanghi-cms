package com.example.sstest.handler;

import com.alibaba.fastjson.JSON;
import com.example.sstest.utils.Result;
import com.example.sstest.utils.ResultCode;
import com.example.sstest.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理授权异常
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result responseResult = Result.failed(ResultCode.NOT_PERMISSION);
        String s = JSON.toJSONString(responseResult);
        WebUtils.renderString(response,s);
    }
}
