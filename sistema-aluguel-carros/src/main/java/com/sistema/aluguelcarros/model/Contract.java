package com.sistema.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "contracts")
public class Contract extends BaseEntity {
    
    @Column(nullable = false, unique = true, length = 50)
    private String contractNumber;
    
    @NotNull(message = "Data de assinatura é obrigatória")
    @Column(nullable = false)
    private LocalDate signatureDate;
    
    @Column
    private LocalDate startDate;
    
    @Column
    private LocalDate endDate;
    
    @NotNull(message = "Valor do contrato é obrigatório")
    @Positive(message = "Valor do contrato deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal contractValue;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal interestRate;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal creditAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status = ContractStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractType contractType = ContractType.RENTAL;
    
    @Column(length = 500)
    private String terms;
    
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rental_order_id", nullable = false)
    private RentalOrder rentalOrder;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Agent bank;
    
    public enum ContractStatus {
        ACTIVE("Ativo"),
        COMPLETED("Concluído"),
        CANCELLED("Cancelado"),
        SUSPENDED("Suspenso");
        
        private final String description;
        
        ContractStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    public enum ContractType {
        RENTAL("Aluguel"),
        CREDIT("Crédito"),
        RENTAL_WITH_CREDIT("Aluguel com Crédito");
        
        private final String description;
        
        ContractType(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // Constructors
    public Contract() {
        generateContractNumber();
    }
    
    public Contract(LocalDate signatureDate, BigDecimal contractValue, RentalOrder rentalOrder) {
        this.signatureDate = signatureDate;
        this.contractValue = contractValue;
        this.rentalOrder = rentalOrder;
        generateContractNumber();
    }
    
    // Getters and Setters
    public String getContractNumber() {
        return contractNumber;
    }
    
    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }
    
    public LocalDate getSignatureDate() {
        return signatureDate;
    }
    
    public void setSignatureDate(LocalDate signatureDate) {
        this.signatureDate = signatureDate;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public BigDecimal getContractValue() {
        return contractValue;
    }
    
    public void setContractValue(BigDecimal contractValue) {
        this.contractValue = contractValue;
    }
    
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    
    public BigDecimal getCreditAmount() {
        return creditAmount;
    }
    
    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }
    
    public ContractStatus getStatus() {
        return status;
    }
    
    public void setStatus(ContractStatus status) {
        this.status = status;
    }
    
    public ContractType getContractType() {
        return contractType;
    }
    
    public void setContractType(ContractType contractType) {
        this.contractType = contractType;
    }
    
    public String getTerms() {
        return terms;
    }
    
    public void setTerms(String terms) {
        this.terms = terms;
    }
    
    public RentalOrder getRentalOrder() {
        return rentalOrder;
    }
    
    public void setRentalOrder(RentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }
    
    public Agent getBank() {
        return bank;
    }
    
    public void setBank(Agent bank) {
        this.bank = bank;
    }
    
    // Business methods
    private void generateContractNumber() {
        this.contractNumber = "CONT-" + System.currentTimeMillis();
    }
    
    public boolean hasCredit() {
        return contractType == ContractType.CREDIT || contractType == ContractType.RENTAL_WITH_CREDIT;
    }
    
    public boolean isActive() {
        return status == ContractStatus.ACTIVE;
    }
}

