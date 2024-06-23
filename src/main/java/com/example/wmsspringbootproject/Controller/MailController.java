package com.example.wmsspringbootproject.Controller;

import com.example.wmsspringbootproject.Service.MailService;
import com.example.wmsspringbootproject.common.result.Result;
import com.example.wmsspringbootproject.model.entity.Users;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "11.邮箱验证")
@CrossOrigin

@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;
    @Operation(summary = "发送验证码")
    @PostMapping("code/sendEmail")
    public Result sendEmail(
            @RequestBody Users user
    ) {
        System.out.println(user);
        return mailService.sendEmail(user);
    }
}
