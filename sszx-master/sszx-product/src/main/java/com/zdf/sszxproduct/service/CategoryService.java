package com.zdf.sszxproduct.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zdf.internalcommon.entity.Category;
import com.zdf.internalcommon.response.CategoryResponseDto;
import com.zdf.internalcommon.result.ResponseResult;

import java.util.List;

/**
* @author mrzhang
* @description 针对表【category(商品分类)】的数据库操作Service
* @createDate 2024-05-27 11:23:52
*/
public interface CategoryService extends IService<Category> {
    ResponseResult<List<Category>> obtainPrimaryCategory();
    ResponseResult<List<CategoryResponseDto>> obtainAllCategory();
}
