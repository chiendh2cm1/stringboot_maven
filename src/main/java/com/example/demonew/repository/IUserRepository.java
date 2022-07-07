package com.example.demonew.repository;
import com.example.demonew.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query(value = "select * " +
            "from users u " +
            "where u.username " +
            "like :q", nativeQuery = true)
    Page<User> getAllUserByName(@Param("q") String q, Pageable pageable);
}