package com.sta.module.blog.service.content;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.sta.module.blog.controller.admin.content.vo.CategorySaveReqVO;
import com.sta.module.blog.dal.dataobject.content.CategoryDO;
import com.sta.module.blog.dal.mysql.content.CategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.sta.module.blog.enums.ErrorCodeConstants.CATEGORY_NOT_EXISTS;

@Service
@Validated
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Long createCategory(CategorySaveReqVO createReqVO) {
        CategoryDO category = BeanUtils.toBean(createReqVO, CategoryDO.class);
        categoryMapper.insert(category);
        return category.getId();
    }

    @Override
    public void updateCategory(CategorySaveReqVO updateReqVO) {
        validateCategoryExists(updateReqVO.getId());
        CategoryDO updateObj = BeanUtils.toBean(updateReqVO, CategoryDO.class);
        categoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteCategory(Long id) {
        validateCategoryExists(id);
        categoryMapper.deleteById(id);
    }

    @Override
    public CategoryDO getCategory(Long id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public List<CategoryDO> getCategoryList() {
        return categoryMapper.selectList();
    }

    private void validateCategoryExists(Long id) {
        if (categoryMapper.selectById(id) == null) {
            throw exception(CATEGORY_NOT_EXISTS);
        }
    }

}
