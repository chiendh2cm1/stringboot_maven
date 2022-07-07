package com.example.demonew.repository;

import com.example.demonew.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "select * " +
            "from product p join category c on p.category_id = c.id " +
            "where p.category_id = :id", nativeQuery = true)
    Page<Product> getProductByCategoryId(@Param("id") long id, Pageable pageable);


    @Query(value = "select * " +
            "from product p " +
            "join category c on p.category_id = c.id " +
            "where c.is_delete = false " +
            "and p.name like :q", nativeQuery = true)
    Page<Product> searchProductByName(@Param("q") String q, Pageable pageable);

}
