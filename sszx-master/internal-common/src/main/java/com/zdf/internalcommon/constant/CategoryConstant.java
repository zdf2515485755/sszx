package com.zdf.internalcommon.constant;

import lombok.Data;

/**
 * @Description CategoryConstant
 * @Author mrzhang
 * @Date 2024/5/27 11:43
 */
@Data
public class CategoryConstant {

    public static final String PRIMARY_CATEGORY = "primary_category:";
    public static final String ALL_CATEGORY = "all_category:";
    public static final int ROOT_CATEGORY_ID = 0;

    private CategoryConstant(){}
}
