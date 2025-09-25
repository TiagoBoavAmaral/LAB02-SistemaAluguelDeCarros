package com.sistema.aluguelcarros.repository;

import com.sistema.aluguelcarros.model.Employment;
import com.sistema.aluguelcarros.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, Long> {
    
    List<Employment> findByClient(Client client);
    
    List<Employment> findByClientId(Long clientId);
    
    List<Employment> findByCompanyName(String companyName);
    
    @Query("SELECT e FROM Employment e WHERE e.companyName LIKE %:companyName%")
    List<Employment> findByCompanyNameContaining(@Param("companyName") String companyName);
    
    @Query("SELECT e FROM Employment e WHERE e.salary >= :minSalary")
    List<Employment> findBySalaryGreaterThanEqual(@Param("minSalary") BigDecimal minSalary);
    
    @Query("SELECT e FROM Employment e WHERE e.salary BETWEEN :minSalary AND :maxSalary")
    List<Employment> findBySalaryBetween(@Param("minSalary") BigDecimal minSalary, 
                                        @Param("maxSalary") BigDecimal maxSalary);
    
    @Query("SELECT SUM(e.salary) FROM Employment e WHERE e.client = :client")
    BigDecimal getTotalSalaryByClient(@Param("client") Client client);
    
    @Query("SELECT COUNT(e) FROM Employment e WHERE e.client = :client")
    Long countByClient(@Param("client") Client client);
    
    @Query("SELECT e FROM Employment e WHERE e.position LIKE %:position%")
    List<Employment> findByPositionContaining(@Param("position") String position);
}

