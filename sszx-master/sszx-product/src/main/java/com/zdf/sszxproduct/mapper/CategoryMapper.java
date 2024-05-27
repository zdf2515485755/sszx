package com.zdf.sszxproduct.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zdf.internalcommon.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
* @author mrzhang
* @description 针对表【category(商品分类)】的数据库操作Mapper
* @createDate 2024-05-27 11:23:52
* @Entity generator.domain.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




