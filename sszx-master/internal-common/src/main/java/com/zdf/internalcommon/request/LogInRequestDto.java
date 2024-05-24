package com.zdf.internalcommon.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 *@Description LogIn Request Dto
 *@Author mrzhang
 *@Date 2024/5/22 20:23
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogInRequestDto {

    @NotBlank(message = "username can not be empty")
    @Length(min = 3, max = 8)
    private String username;

    @NotBlank(message = "password can not be empty")
    private String password;

    @NotBlank(message = "verificationCode can not be empty")
    private String verificationCode;

    @NotBlank(message = "uuid can not be empty")
    private String uuid;
}
