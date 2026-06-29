package com.sta.module.blog.service;

import com.sta.module.blog.controller.admin.category.vo.BlogCategorySaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogCategoryDO;

import java.util.List;

/**
 * 博客分类 Service 接口
 */
public interface BlogCategoryService {

    Long createCategory(BlogCategorySaveReqVO createReqVO);

    void updateCategory(BlogCategorySaveReqVO updateReqVO);

    void deleteCategory(Long id);

    BlogCategoryDO getCategory(Long id);

    List<BlogCategoryDO> getCategoryList();

}
