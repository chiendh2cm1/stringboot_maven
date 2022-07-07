package com.example.demonew.repository;
import com.example.demonew.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface IRoleRepository extends JpaRepository<Role, Long> {
}