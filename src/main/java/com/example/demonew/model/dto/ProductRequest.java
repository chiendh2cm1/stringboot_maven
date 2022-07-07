package com.example.demonew.model.dto;

import com.example.demonew.model.entity.Category;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private double price;
    private String description;
    private MultipartFile image;
    private Category category;

    public ProductRequest(Long id, String name, double price, String description, MultipartFile image, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public ProductRequest(String name, double price, String description, MultipartFile image, Category category) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.image = image;
        this.category = category;
    }

    public ProductRequest() {
    }
}
