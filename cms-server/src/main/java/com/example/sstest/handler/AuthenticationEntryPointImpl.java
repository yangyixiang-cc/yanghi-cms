package com.example.sstest.handler;

import com.alibaba.fastjson.JSON;
import com.example.sstest.utils.Result;
import com.example.sstest.utils.ResultCode;
import com.example.sstest.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理认证异常
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result responseResult = Result.failed(ResultCode.UN_AUTHORIZED);
        String s = JSON.toJSONString(responseResult);
        WebUtils.renderString(response,s);
    }
}
