package com.example.wmsspringbootproject.Service.impl;

import com.example.wmsspringbootproject.Service.MailService;
import com.example.wmsspringbootproject.Utils.MailServiceUtil;
import com.example.wmsspringbootproject.Utils.TextUtil;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private MailServiceUtil mailServiceUtil;
    @Override
    public Result sendEmail(Users user) {
        String email = user.getEmail();
        if (!TextUtil.isEmail(email)) {
            return Result.failed("邮箱格式不正确");
        }
        // 随机生成6位数字验证码
        String code = TextUtil.randomSixCode();

        // 正文内容
        String content = "亲爱的用户：\n" +
                "您此次的验证码为：\n\n" +
                code + "\n\n" +
                "此验证码5分钟内有效，请立即进行下一步操作。 如非你本人操作，请忽略此邮件。\n" +
                "感谢您的使用！\n" +
                "肯德基疯狂星期四 v我50!\n";

        // 发送验证码
        mailServiceUtil.sendSimpleMail(email, "您此次的验证码为：" + code, content);
        System.out.println("验证码已发送！");
        System.out.println("验证码为:" + code);
        // 丢入缓存，设置5分钟过期
        try {
            System.out.println("验证码存入redis");
            // 设置RedisTemplate的值序列化器为StringRedisSerializer
            redisTemplate.setValueSerializer(new StringRedisSerializer());
            redisTemplate.opsForValue().set("EMAIL_" + email, code, 5, TimeUnit.MINUTES);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store value in Redis", e);
        }
        return Result.success(null);
    }
}
