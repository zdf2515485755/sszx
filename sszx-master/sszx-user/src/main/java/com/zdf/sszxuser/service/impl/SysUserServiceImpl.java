package com.zdf.sszxuser.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdf.internalcommon.constant.*;
import com.zdf.internalcommon.entity.SysUser;
import com.zdf.internalcommon.request.LogInRequestDto;
import com.zdf.internalcommon.response.VerificationCodeResponseDto;
import com.zdf.internalcommon.result.ResponseResult;
import com.zdf.sszxuser.config.MinioConfig;
import com.zdf.sszxuser.mapper.SysUserMapper;
import com.zdf.sszxuser.service.SysUserService;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
* @author mrzhang
* @description 针对表【sys_user(用户表)】的数据库操作Service实现
* @createDate 2024-05-24 20:45:22
*/
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser>
    implements SysUserService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private MinioClient minioClient;
    @Resource
    private MinioConfig minioConfig;

    @Override
    public ResponseResult<VerificationCodeResponseDto> getVerificationCode() {
        //生成验证码
        CircleCaptcha circleCaptcha = CaptchaUtil.createCircleCaptcha(ImageConstant.DEFAULT_WIDTH, ImageConstant.DEFAULT_HEIGHT, ImageConstant.DEFAULT_LENGTH, ImageConstant.DEFAULT_CIRCLE_COUNT);
        String uuid = IdUtil.simpleUUID();
        String verificationCodeKey = RedisConstant.VERIFY_CODE_KEY_PREFIX + uuid;
        String verificationCode = circleCaptcha.getCode();
        log.info("uuid: {}", uuid);
        log.info("code: {}", verificationCode);
        // 存入redis
        redisTemplate.opsForValue().set(verificationCodeKey, verificationCode, 120, TimeUnit.SECONDS);

        VerificationCodeResponseDto verificationCodeResponseDto = VerificationCodeResponseDto.builder().uuid(uuid)
                .image(circleCaptcha.getImageBase64())
                .build();

        return ResponseResult.success(verificationCodeResponseDto);
    }

    @Override
    public ResponseResult<String> login(LogInRequestDto logInRequestDto) {
        QueryWrapper<SysUser> userinfoQueryWrapper = new QueryWrapper<>();
        userinfoQueryWrapper.eq("username", logInRequestDto.getUsername())
                .eq("is_deleted", UserConstant.IS_NORMAL);
        SysUser sysUser = sysUserMapper.selectOne(userinfoQueryWrapper);
        if (Objects.isNull(sysUser)){
            return ResponseResult.fail(StatusCode.USER_IS_NOT_EXIT.getCode(), StatusCode.USER_IS_NOT_EXIT.getMessage());
        }
        if (!SecureUtil.md5(logInRequestDto.getPassword()).equals(sysUser.getPassword())){
            return ResponseResult.fail(StatusCode.PASSWORD_IS_ERROR.getCode(), StatusCode.PASSWORD_IS_ERROR.getMessage());
        }
        //删除redis中的验证码
        redisTemplate.delete(RedisConstant.VERIFY_CODE_KEY_PREFIX + logInRequestDto.getUuid());
        //生成token
        HashMap<String, Object> playLoad = new HashMap<>(1);
        playLoad.put(JwtConstant.JWT_TOKEN_NAME, logInRequestDto.getUsername());
        String token = JWTUtil.createToken(playLoad, JwtConstant.SIGN.getBytes());
        String tokenKey = RedisConstant.TOKEN_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(tokenKey, JSONUtil.toJsonStr(sysUser), RedisConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        return ResponseResult.success(token);
    }

    @Override
    public ResponseResult<String> fileUpload(MultipartFile file) {
        try {
            boolean found =
                    minioClient.bucketExists(BucketExistsArgs.builder().bucket(minioConfig.getBucketName()).build());
            if (!found) {
                // Make a new bucket called 'asiatrip'.
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(minioConfig.getBucketName()).build());
            } else {
                log.info("Bucket 'asiatrip' already exists.");
            }

            String filename = file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder().bucket(minioConfig.getBucketName()).object(filename).stream(
                                    file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build());
            return ResponseResult.success(filename);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}