package com.example.demonew.service.product;

import com.example.demonew.model.entity.Product;
import com.example.demonew.repository.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService implements IProductService {
    @Autowired
    private IProductRepository productRepository;

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public Page<Product> findByName(String q, Pageable pageable) {
        if (Objects.equals(q, "")) {
            q = "%%";
        } else {
            q = '%' + q + '%';
        }
        return productRepository.searchProductByName(q, pageable);
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public void remove(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Page<Product> getProductByCategoryId(long id, Pageable pageable) {
        return productRepository.getProductByCategoryId(id, pageable);
    }
}

