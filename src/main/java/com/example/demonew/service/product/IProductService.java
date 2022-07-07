package com.example.demonew.service.product;

import com.example.demonew.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IProductService {
    Page<Product> getProductByCategoryId(long Id, Pageable pageable);

    Optional<Product> findById(Long id);

    Page<Product> findByName(String name, Pageable pageable);

    Product save(Product product);

    void remove(Long id);

}
