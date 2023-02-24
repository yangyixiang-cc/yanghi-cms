package com.example.sstest.service.impl;

import com.example.sstest.service.CaptchaService;
import com.example.sstest.utils.Result;
import com.example.sstest.utils.UUIDUtil;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private UUIDUtil uuidUtil;

    @Autowired
    private DefaultKaptcha producer;
    //从SpringBoot的配置文件中取出过期时间
    public static final Integer TIME_OUT = 60 * 1000;// 60 * 60 *1000  1min

    //UUID为key, 验证码为Value放在Redis中
    @Override
    public Map<String, Object> createToken(String captcha) {
        //生成一个token
        String key = uuidUtil.getUUID32();
        //生成验证码对应的token  以token为key  验证码为value存在redis中
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, captcha);
        //设置验证码过期时间
        redisTemplate.expire(key, TIME_OUT, TimeUnit.MINUTES);
        Map<String, Object> map = new HashMap<>();
        map.put("token", key);
        map.put("expire", TIME_OUT);
        return map;
    }

    //生成captcha验证码
    @Override
    public Result captchaCreator() throws IOException {
        //生成文字验证码
        String text = producer.createText();
        //生成文字对应的图片验证码
        BufferedImage image = producer.createImage(text);
        //将图片写出
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        //对写出的字节数组进行Base64编码 ==> 用于传递8比特字节码
        BASE64Encoder encoder = new BASE64Encoder();
        //生成token
        Map<String, Object> token = createToken(text);
        token.put("img", encoder.encode(outputStream.toByteArray()));
        return Result.ok(token);
    }

    //验证输入的验证码是否正确
    @Override
    public Result versifyCaptcha(String token, String inputCode) {
        //根据前端传回的token在redis中找对应的value
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(token)) {
            //验证通过, 删除对应的key
            if (valueOperations.get(token).equals(inputCode)) {
                redisTemplate.delete(token);
                return Result.ok("验证码正确");
            } else {
                return Result.failed("验证码不正确");

            }
        } else {
            return Result.failed("验证码失效，请刷新验证码");
        }
    }
}