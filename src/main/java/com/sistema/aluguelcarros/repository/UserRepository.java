package com.sistema.aluguelcarros.repository;

import com.sistema.aluguelcarros.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByCpf(String cpf);
    
    boolean existsByEmail(String email);
    
    boolean existsByCpf(String cpf);
    
    List<User> findByUserType(User.UserType userType);
    
    List<User> findByActiveTrue();
    
    List<User> findByActiveFalse();
    
    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT u FROM User u WHERE u.userType = :userType AND u.active = true")
    List<User> findActiveUsersByType(@Param("userType") User.UserType userType);
}

