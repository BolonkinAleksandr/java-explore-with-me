package ru.practicum.explorewithmemain.service.publicservice;

import ru.practicum.explorewithmemain.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {

    List<CategoryDto> getCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}
