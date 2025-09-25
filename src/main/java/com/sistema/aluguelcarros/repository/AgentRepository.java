package com.sistema.aluguelcarros.repository;

import com.sistema.aluguelcarros.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends JpaRepository<Agent, Long> {
    
    Optional<Agent> findByEmail(String email);
    
    Optional<Agent> findByCnpj(String cnpj);
    
    boolean existsByCnpj(String cnpj);
    
    List<Agent> findByAgentType(Agent.AgentType agentType);
    
    List<Agent> findByActiveTrue();
    
    @Query("SELECT a FROM Agent a WHERE a.companyName LIKE %:companyName%")
    List<Agent> findByCompanyNameContaining(@Param("companyName") String companyName);
    
    @Query("SELECT a FROM Agent a WHERE a.agentType = :agentType AND a.active = true")
    List<Agent> findActiveAgentsByType(@Param("agentType") Agent.AgentType agentType);
    
    @Query("SELECT a FROM Agent a WHERE SIZE(a.evaluatedOrders) > 0")
    List<Agent> findAgentsWithEvaluatedOrders();
    
    @Query("SELECT a FROM Agent a WHERE a.agentType = 'BANK' AND SIZE(a.contracts) > 0")
    List<Agent> findBanksWithContracts();
}

