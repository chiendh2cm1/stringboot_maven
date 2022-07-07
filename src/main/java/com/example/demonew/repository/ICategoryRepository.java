package com.example.demonew.repository;

import com.example.demonew.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Long> {
    @Query(value = "select * " +
            "from category c " +
            "where c.name " +
            "like :q and is_delete = false", nativeQuery = true)
    Page<Category> searchCategory(@Param("q") String q,
                                  Pageable pageable);

    @Query(value = "select * " +
            "from category " +
            "where is_delete = false ", nativeQuery = true)
    Page<Category> findAllCategory(Pageable pageable);

    @Modifying
    @Query(value = "update category " +
            "set is_delete = true " +
            "where id = ?1;", nativeQuery = true)
    void deleteCategory(long id);

}
