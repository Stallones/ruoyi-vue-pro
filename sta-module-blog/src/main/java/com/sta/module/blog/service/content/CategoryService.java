package com.sta.module.blog.service.content;

import com.sta.module.blog.controller.admin.content.vo.CategorySaveReqVO;
import com.sta.module.blog.dal.dataobject.content.CategoryDO;

import java.util.List;

/**
 * 博客分类 Service 接口
 */
public interface CategoryService {

    Long createCategory(CategorySaveReqVO createReqVO);

    void updateCategory(CategorySaveReqVO updateReqVO);

    void deleteCategory(Long id);

    CategoryDO getCategory(Long id);

    List<CategoryDO> getCategoryList();

}
