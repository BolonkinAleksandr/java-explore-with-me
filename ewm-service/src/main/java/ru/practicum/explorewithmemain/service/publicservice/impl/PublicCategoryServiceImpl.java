package ru.practicum.explorewithmemain.service.publicservice.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmemain.dto.CategoryDto;
import ru.practicum.explorewithmemain.exceptions.NotFoundException;
import ru.practicum.explorewithmemain.mapper.CategoryMapper;
import ru.practicum.explorewithmemain.model.Category;
import ru.practicum.explorewithmemain.repository.CategoryRepository;
import ru.practicum.explorewithmemain.repository.CustomPageable;
import ru.practicum.explorewithmemain.service.publicservice.PublicCategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublicCategoryServiceImpl implements PublicCategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getCategories(Integer from, Integer size) {
        final Pageable pageable = CustomPageable.of(from, size);
        List<Category> listCategory = categoryRepository.findAll(pageable).getContent();
        if (listCategory.size() == 0) {
            throw new NotFoundException("object doesn't found ",
                    String.format("Compilation was not found."));
        }

        return CategoryMapper.toListCategoryDto(listCategory);
    }


    @Override
    public CategoryDto getCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException("object doesn't found ",
                        String.format("Compilation with id={} was not found.", catId)));
        return CategoryMapper.toCategoryDto(category);
    }
}
