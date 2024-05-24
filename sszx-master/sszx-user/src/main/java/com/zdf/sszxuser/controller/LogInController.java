package com.zdf.sszxuser.controller;

import com.zdf.internalcommon.request.LogInRequestDto;
import com.zdf.internalcommon.response.VerificationCodeResponseDto;
import com.zdf.internalcommon.result.ResponseResult;
import com.zdf.sszxuser.annotation.PassTokenCheck;
import com.zdf.sszxuser.service.impl.SysUserServiceImpl;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *@Description api for operating Users
 *@Author mrzhang
 *@Date 2024/5/24 20:50
 */
@RestController
@RequestMapping("/user")
@PassTokenCheck
public class LogInController {

    @Resource
    private SysUserServiceImpl sysUserService;

    @GetMapping("/getVerificationCode")
    public ResponseResult<VerificationCodeResponseDto> getVerificationCode(){
        return sysUserService.getVerificationCode();
    }

    @GetMapping("/login")
    public ResponseResult<String> login(@Validated @RequestBody LogInRequestDto logInRequestDto){
        return sysUserService.login(logInRequestDto);
    }
}
