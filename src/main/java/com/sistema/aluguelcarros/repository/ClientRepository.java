package com.sistema.aluguelcarros.repository;

import com.sistema.aluguelcarros.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    
    Optional<Client> findByEmail(String email);
    
    Optional<Client> findByCpf(String cpf);
    
    List<Client> findByProfession(String profession);
    
    List<Client> findByActiveTrue();
    
    @Query("SELECT c FROM Client c WHERE c.name LIKE %:name%")
    List<Client> findByNameContaining(@Param("name") String name);
    
    @Query("SELECT c FROM Client c JOIN c.employments e WHERE e.companyName LIKE %:companyName%")
    List<Client> findByEmploymentCompanyName(@Param("companyName") String companyName);
    
    @Query("SELECT c FROM Client c WHERE SIZE(c.rentalOrders) > 0")
    List<Client> findClientsWithRentalOrders();
    
    @Query("SELECT c FROM Client c WHERE SIZE(c.employments) >= :minEmployments")
    List<Client> findClientsWithMinimumEmployments(@Param("minEmployments") int minEmployments);
}

