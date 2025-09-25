package com.sistema.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "agents")
@PrimaryKeyJoinColumn(name = "user_id")
public class Agent extends User {
    
    @NotBlank(message = "CNPJ é obrigatório")
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
    
    @NotBlank(message = "Nome da empresa é obrigatório")
    @Column(nullable = false, length = 200)
    private String companyName;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AgentType agentType;
    
    @OneToMany(mappedBy = "evaluatedBy", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalOrder> evaluatedOrders = new ArrayList<>();
    
    @OneToMany(mappedBy = "bank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Contract> contracts = new ArrayList<>();
    
    public enum AgentType {
        COMPANY, BANK
    }
    
    // Constructors
    public Agent() {
        super();
        setUserType(UserType.AGENT);
        addRole(Role.ROLE_AGENT);
    }
    
    public Agent(String name, String email, String password, String cpf, String rg, 
                 String cnpj, String companyName, AgentType agentType) {
        super(name, email, password, cpf, rg, UserType.AGENT);
        this.cnpj = cnpj;
        this.companyName = companyName;
        this.agentType = agentType;
        addRole(Role.ROLE_AGENT);
    }
    
    // Getters and Setters
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public AgentType getAgentType() {
        return agentType;
    }
    
    public void setAgentType(AgentType agentType) {
        this.agentType = agentType;
    }
    
    public List<RentalOrder> getEvaluatedOrders() {
        return evaluatedOrders;
    }
    
    public void setEvaluatedOrders(List<RentalOrder> evaluatedOrders) {
        this.evaluatedOrders = evaluatedOrders;
    }
    
    public void addEvaluatedOrder(RentalOrder rentalOrder) {
        evaluatedOrders.add(rentalOrder);
        rentalOrder.setEvaluatedBy(this);
    }
    
    public void removeEvaluatedOrder(RentalOrder rentalOrder) {
        evaluatedOrders.remove(rentalOrder);
        rentalOrder.setEvaluatedBy(null);
    }
    
    public List<Contract> getContracts() {
        return contracts;
    }
    
    public void setContracts(List<Contract> contracts) {
        this.contracts = contracts;
    }
    
    public void addContract(Contract contract) {
        contracts.add(contract);
        contract.setBank(this);
    }
    
    public void removeContract(Contract contract) {
        contracts.remove(contract);
        contract.setBank(null);
    }
}

