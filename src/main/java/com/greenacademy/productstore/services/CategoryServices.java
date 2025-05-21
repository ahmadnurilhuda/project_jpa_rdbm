package com.greenacademy.productstore.services;

import java.time.Instant;

import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.Category;
import com.greenacademy.productstore.repositories.CategoryRepository;

@Service
public class CategoryServices {
    private CategoryRepository categoryRepository;

    public CategoryServices(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Iterable<Category> getAll() {
        return categoryRepository.findAll();
    }

    public Category getById(Integer id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public Category create(Category category) {
        return categoryRepository.save(category);
    }

    public Category update(Category category) {

        Category categoryExisting = categoryRepository.findById(category.getId()).orElse(null);

        if(categoryExisting != null) {
            categoryExisting.setName(category.getName());
            categoryExisting.setDescription(category.getDescription());
            categoryExisting.setUpdated_at(Instant.now());
            categoryRepository.save(categoryExisting);
        }
        return categoryExisting;
    }

    public void delete(Integer id) {
        categoryRepository.deleteById(id);
    }
}
