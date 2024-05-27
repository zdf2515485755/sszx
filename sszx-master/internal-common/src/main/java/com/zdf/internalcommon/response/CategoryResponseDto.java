package com.zdf.internalcommon.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 *@Description CategoryResponseDto
 *@Author mrzhang
 *@Date 2024/5/27 12:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponseDto {
    private Long id;
    private String name;
    private List<CategoryResponseDto> categoryResponseDtoList;
}
