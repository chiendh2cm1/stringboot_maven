package com.example.demonew.service.category;

import com.example.demonew.model.entity.Category;
import com.example.demonew.repository.ICategoryRepository;
import com.example.demonew.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {
    @Autowired
    private ICategoryRepository categoryRepository;

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Page<Category> findByName(String q, Pageable pageable) {
        if (Objects.equals(q, "")) {
            q = "%%";
        } else {
            q = "%" + q + "%";
        }
        return categoryRepository.searchCategory(q, pageable);
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public void remove(Long id) {
        categoryRepository.deleteCategory(id);
    }
}
