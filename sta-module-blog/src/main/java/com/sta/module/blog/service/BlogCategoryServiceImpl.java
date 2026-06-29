package com.sta.module.blog.service;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.category.vo.BlogCategorySaveReqVO;
import com.sta.module.blog.dal.dataobject.BlogCategoryDO;
import com.sta.module.blog.dal.mysql.BlogCategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.CATEGORY_NOT_EXISTS;

@Service
@Validated
public class BlogCategoryServiceImpl implements BlogCategoryService {

    @Resource
    private BlogCategoryMapper categoryMapper;

    @Override
    public Long createCategory(BlogCategorySaveReqVO createReqVO) {
        BlogCategoryDO category = BeanUtils.toBean(createReqVO, BlogCategoryDO.class);
        categoryMapper.insert(category);
        return category.getId();
    }

    @Override
    public void updateCategory(BlogCategorySaveReqVO updateReqVO) {
        validateCategoryExists(updateReqVO.getId());
        BlogCategoryDO updateObj = BeanUtils.toBean(updateReqVO, BlogCategoryDO.class);
        categoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteCategory(Long id) {
        validateCategoryExists(id);
        categoryMapper.deleteById(id);
    }

    @Override
    public BlogCategoryDO getCategory(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<BlogCategoryDO> getCategoryList() {
        return categoryMapper.selectListAll();
    }

    private void validateCategoryExists(Long id) {
        if (categoryMapper.selectById(id) == null) {
            throw exception(CATEGORY_NOT_EXISTS);
        }
    }

}
