package ru.practicum.explorewithmemain.service.adminservice;

import ru.practicum.explorewithmemain.dto.CategoryDto;
import ru.practicum.explorewithmemain.dto.NewCategoryDto;

public interface AdminCategoryService {

    CategoryDto updateCategoryByIdAndName(CategoryDto categoryDto);

    CategoryDto addCategory(NewCategoryDto newCategoryDto);

    CategoryDto deleteCategoryById(Long catId);
}
