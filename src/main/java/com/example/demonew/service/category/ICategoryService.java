package com.example.demonew.service.category;


import com.example.demonew.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICategoryService {
    Optional<Category> findById(Long id);
    Page<Category> findByName(String name, Pageable pageable);
    Category save(Category category);
    void remove(Long id);
}
