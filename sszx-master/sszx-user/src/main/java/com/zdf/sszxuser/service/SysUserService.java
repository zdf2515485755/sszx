package com.zdf.sszxuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zdf.internalcommon.entity.SysUser;
import com.zdf.internalcommon.request.LogInRequestDto;
import com.zdf.internalcommon.response.VerificationCodeResponseDto;
import com.zdf.internalcommon.result.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
* @author mrzhang
* @description 针对表【sys_user(用户表)】的数据库操作Service
* @createDate 2024-05-24 20:45:22
*/
public interface SysUserService extends IService<SysUser> {
    ResponseResult<VerificationCodeResponseDto> getVerificationCode();
    ResponseResult<String> login(LogInRequestDto logInRequestDto);
    ResponseResult<String> fileUpload(MultipartFile file);
}
