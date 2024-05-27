package com.zdf.sszxproduct.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zdf.internalcommon.constant.CategoryConstant;
import com.zdf.internalcommon.entity.Category;
import com.zdf.internalcommon.response.CategoryResponseDto;
import com.zdf.internalcommon.result.ResponseResult;
import com.zdf.sszxproduct.mapper.CategoryMapper;
import com.zdf.sszxproduct.service.CategoryService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author mrzhang
* @description 针对表【category(商品分类)】的数据库操作Service实现
* @createDate 2024-05-27 11:23:52
*/
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService {

    @Resource
    private RedisTemplate<String, String> redisTemplate;
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult<List<Category>> obtainPrimaryCategory() {
        String primaryCategory = redisTemplate.opsForValue().get(CategoryConstant.PRIMARY_CATEGORY);
        if (Objects.isNull(primaryCategory)) {
            List<Category> categoryList = getPrimaryCategory();
            redisTemplate.opsForValue().set(CategoryConstant.PRIMARY_CATEGORY, JSONUtil.parseArray(categoryList).toString());
            return ResponseResult.success(categoryList);
        }
        return ResponseResult.success(JSONUtil.parseArray(primaryCategory).toList(Category.class));
    }

    public List<Category> getPrimaryCategory(){
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.eq("parent_id", CategoryConstant.ROOT_CATEGORY_ID);
        return categoryMapper.selectList(categoryQueryWrapper);
    }

    @Override
    public ResponseResult<List<CategoryResponseDto>> obtainAllCategory() {
        String allCategory = redisTemplate.opsForValue().get(CategoryConstant.ALL_CATEGORY);
        if (Objects.isNull(allCategory)) {
            List<CategoryResponseDto> categoryResponseDtoList = getAllCategory();
            redisTemplate.opsForValue().set(CategoryConstant.ALL_CATEGORY, JSONUtil.parseArray(categoryResponseDtoList).toString());
            return ResponseResult.success(categoryResponseDtoList);
        }
        return ResponseResult.success(JSONUtil.parseArray(allCategory).toList(CategoryResponseDto.class));
    }

    public List<CategoryResponseDto> getAllCategory(){
        List<Category> categories = categoryMapper.selectList(null);
        return categories.stream()
                .filter(category -> category.getParentId() == CategoryConstant.ROOT_CATEGORY_ID)
                .map(category -> {
                    CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
                    categoryResponseDto.setId(category.getId());
                    categoryResponseDto.setName(category.getName());
                    categoryResponseDto.setCategoryResponseDtoList(getchild(category, categories));
                    return categoryResponseDto;
                }).collect(Collectors.toList());
    }

    public List<CategoryResponseDto> getchild(Category category, List<Category> categories) {
        List<Category> categoryList = categories.stream()
                .filter(child -> Objects.equals(child.getParentId(), category.getId()))
                .collect(Collectors.toList());
        if (categoryList.isEmpty()) {
            return Collections.emptyList();
        }

        return categoryList.stream().map(childCategory -> {
            CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
            categoryResponseDto.setId(childCategory.getId());
            categoryResponseDto.setName(childCategory.getName());
            categoryResponseDto.setCategoryResponseDtoList(getchild(childCategory, categories));
            return categoryResponseDto;
        }).collect(Collectors.toList());
    }
}