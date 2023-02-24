package com.example.sstest.service;
import com.example.sstest.utils.Result;

import java.io.IOException;
import java.util.Map;

public interface CaptchaService {

    //生成token
    Map<String, Object> createToken(String captcha);

    //生成captcha验证码
    Result captchaCreator() throws IOException;

    //验证输入的验证码是否正确
    Result versifyCaptcha (String token, String inputCode);
}