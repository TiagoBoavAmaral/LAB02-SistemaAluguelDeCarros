package com.sistema.aluguelcarros.repository;

import com.sistema.aluguelcarros.model.Contract;
import com.sistema.aluguelcarros.model.RentalOrder;
import com.sistema.aluguelcarros.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    
    Optional<Contract> findByContractNumber(String contractNumber);
    
    Optional<Contract> findByRentalOrder(RentalOrder rentalOrder);
    
    List<Contract> findByBank(Agent bank);
    
    List<Contract> findByStatus(Contract.ContractStatus status);
    
    List<Contract> findByContractType(Contract.ContractType contractType);
    
    @Query("SELECT c FROM Contract c WHERE c.signatureDate BETWEEN :startDate AND :endDate")
    List<Contract> findBySignatureDateBetween(@Param("startDate") LocalDate startDate, 
                                             @Param("endDate") LocalDate endDate);
    
    @Query("SELECT c FROM Contract c WHERE c.endDate < :currentDate AND c.status = 'ACTIVE'")
    List<Contract> findExpiredActiveContracts(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT c FROM Contract c WHERE c.bank = :bank AND c.status = 'ACTIVE'")
    List<Contract> findActiveContractsByBank(@Param("bank") Agent bank);
    
    @Query("SELECT c FROM Contract c WHERE c.contractType IN ('CREDIT', 'RENTAL_WITH_CREDIT')")
    List<Contract> findContractsWithCredit();
    
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.bank = :bank")
    Long countByBank(@Param("bank") Agent bank);
    
    @Query("SELECT COUNT(c) FROM Contract c WHERE c.status = :status")
    Long countByStatus(@Param("status") Contract.ContractStatus status);
    
    @Query("SELECT c FROM Contract c WHERE c.rentalOrder.client.id = :clientId")
    List<Contract> findByClientId(@Param("clientId") Long clientId);
    
    @Query("SELECT c FROM Contract c ORDER BY c.signatureDate DESC")
    List<Contract> findAllOrderBySignatureDateDesc();
}

