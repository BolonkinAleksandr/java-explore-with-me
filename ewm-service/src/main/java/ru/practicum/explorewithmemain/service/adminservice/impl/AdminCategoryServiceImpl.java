package ru.practicum.explorewithmemain.service.adminservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.dto.CategoryDto;
import ru.practicum.explorewithmemain.dto.NewCategoryDto;
import ru.practicum.explorewithmemain.exceptions.RequestException;
import ru.practicum.explorewithmemain.mapper.CategoryMapper;
import ru.practicum.explorewithmemain.model.Category;
import ru.practicum.explorewithmemain.repository.CategoryRepository;
import ru.practicum.explorewithmemain.repository.EventRepository;
import ru.practicum.explorewithmemain.service.adminservice.AdminCategoryService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminCategoryServiceImpl implements AdminCategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto newCategoryDto) {
        Category category = CategoryMapper.toCategory(newCategoryDto);
        categoryRepository.save(category);
        log.info("add new category={}", category.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto updateCategoryByIdAndName(
            CategoryDto categoryDto) {
        Category category = findAndValidateCategory(categoryDto.getId());
        category.setName(categoryDto.getName());
        categoryRepository.saveAndFlush(category);
        log.info("change category to categoryDto={}", category.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto deleteCategoryById(Long catId) {
        Category category = findAndValidateCategory(catId);
        if (!eventRepository.findCategoryByIdInEvent(catId).get().isEmpty()) {
            throw new RequestException("error with request", " category have events");
        }
        categoryRepository.deleteById(category.getId());
        log.info("delete category={}", category.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    private Category findAndValidateCategory(Long catId) {
        return categoryRepository
                .findById(catId)
                .orElseThrow(() ->
                        new RequestException("error with request", "categoryRepository"));
    }
}
