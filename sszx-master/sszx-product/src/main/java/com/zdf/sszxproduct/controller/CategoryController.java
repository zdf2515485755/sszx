package com.zdf.sszxproduct.controller;

import com.zdf.internalcommon.annotation.PassTokenCheck;
import com.zdf.internalcommon.entity.Category;
import com.zdf.internalcommon.response.CategoryResponseDto;
import com.zdf.internalcommon.result.ResponseResult;
import com.zdf.sszxproduct.service.impl.CategoryServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *@Description 类功能简要描述
 *@Author mrzhang
 *@Date 2024/5/27 11:29
 */
@RestController
@RequestMapping("/category")
@PassTokenCheck
public class CategoryController {

    @Resource
    private CategoryServiceImpl categoryService;

    @GetMapping("/obtainPrimaryCategory")
    public ResponseResult<List<Category>> obtainPrimaryCategory() {
        return categoryService.obtainPrimaryCategory();
    }

    @GetMapping("/obtainAllCategory")
    public ResponseResult<List<CategoryResponseDto>> obtainAllCategory() {
        return categoryService.obtainAllCategory();
    }
}