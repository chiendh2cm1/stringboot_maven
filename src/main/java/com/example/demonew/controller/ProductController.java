package com.example.demonew.controller;

import com.example.demonew.model.dto.CustomPage;
import com.example.demonew.model.dto.ProductRequest;
import com.example.demonew.model.entity.Product;
import com.example.demonew.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    @Value("${file-upload}")
    String uploadPath;
    @Autowired
    IProductService productService;
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<CustomPage> getProduct(@RequestParam(name = "q", required = false, defaultValue = "") String q,
                                                 @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                                                 @RequestParam(name = "size", required = false, defaultValue = "20") int size) {
        try {
            int page_Currents = page >= 1 ? page - 1 : page;
            Pageable pageable = PageRequest.of(page_Currents, size);
            Page<Product> productPage = productService.findByName(q, pageable);
            if (productPage.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            List<Product> products = productPage.getContent();
            Map<String, Object> paging = new HashMap<>();
            paging.put("currentPage", page_Currents);
            paging.put("totalPage", productPage.getTotalPages());
            paging.put("totalElements", productPage.getTotalElements());
            paging.put("size", size);
            CustomPage customPage = new CustomPage();
            customPage.setContent(products);
            customPage.setPageable(paging);
            return new ResponseEntity<>(customPage, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        try {
            Optional<Product> productOptional = productService.findById(id);
            if (!productOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(productOptional.get(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/{id}")
    private ResponseEntity<Product> updateProduct(@PathVariable Long id, ProductRequest productRequest) {
        try {
            Optional<Product> productOptional = productService.findById(id);
            if (!productOptional.isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            Product product = productOptional.get();
            MultipartFile multipartFile = productRequest.getImage();
            if (multipartFile != null && multipartFile.getSize() != 0) {
                File file = new File(uploadPath + product.getImage());
                if (file.exists()) {
                    file.delete();
                }
                String fileName = productRequest.getImage().getOriginalFilename();
                Long currenTime = System.currentTimeMillis();
                fileName = currenTime + fileName;
                product.setImage(fileName);
                try {
                    FileCopyUtils.copy(multipartFile.getBytes(), new File(uploadPath + fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            product.setName(productRequest.getName());
            product.setPrice(productRequest.getPrice());
            product.setDescription(productRequest.getDescription());
            product.setCategory(productRequest.getCategory());
            productService.save(product);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<Product> saveProduct(@ModelAttribute ProductRequest productRequest) {
        String fileName = productRequest.getImage().getOriginalFilename();
        Long currenTime = System.currentTimeMillis();
        fileName = currenTime + fileName;
        try {
            FileCopyUtils.copy(productRequest.getImage().getBytes(), new File(uploadPath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Product product = new Product(productRequest.getId(), productRequest.getName(), productRequest.getPrice(), productRequest.getDescription(), fileName, productRequest.getCategory());
        return new ResponseEntity<>(productService.save(product), HttpStatus.OK);
    }

}
