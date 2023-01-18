package ru.practicum.explorewithmemain.controller.admincontroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmemain.dto.CategoryDto;
import ru.practicum.explorewithmemain.dto.NewCategoryDto;
import ru.practicum.explorewithmemain.service.adminservice.AdminCategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @PatchMapping
    public CategoryDto updateCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("update categoryDto {}", categoryDto);
        return adminCategoryService.updateCategoryByIdAndName(categoryDto);
    }

    @PostMapping
    public CategoryDto addCategory(@Valid @RequestBody NewCategoryDto newCategoryDto) {
        log.info("adminPostCategory, create category newCategoryDto {}", newCategoryDto);
        return adminCategoryService.addCategory(newCategoryDto);
    }

    @DeleteMapping("/{catId}")
    public CategoryDto deleteCategory(@PathVariable Long catId) {
        log.info("adminDeleteCategory, delete category catId={}", catId);
        return adminCategoryService.deleteCategoryById(catId);
    }
}
