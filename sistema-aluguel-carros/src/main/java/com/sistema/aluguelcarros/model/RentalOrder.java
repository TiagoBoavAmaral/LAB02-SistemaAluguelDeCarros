package com.sistema.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rental_orders")
public class RentalOrder extends BaseEntity {
    
    @NotNull(message = "Data de início é obrigatória")
    @Column(nullable = false)
    private LocalDate startDate;
    
    @NotNull(message = "Data de fim é obrigatória")
    @Column(nullable = false)
    private LocalDate endDate;
    
    @NotNull(message = "Valor total é obrigatório")
    @Positive(message = "Valor total deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(length = 500)
    private String observations;
    
    @Column(length = 500)
    private String evaluationNotes;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluated_by_id")
    private Agent evaluatedBy;
    
    @OneToOne(mappedBy = "rentalOrder", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contract contract;
    
    public enum OrderStatus {
        PENDING("Pendente"),
        UNDER_EVALUATION("Em Avaliação"),
        APPROVED("Aprovado"),
        REJECTED("Rejeitado"),
        CANCELLED("Cancelado"),
        ACTIVE("Ativo"),
        COMPLETED("Concluído");
        
        private final String description;
        
        OrderStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
    
    // Constructors
    public RentalOrder() {}
    
    public RentalOrder(LocalDate startDate, LocalDate endDate, BigDecimal totalAmount, 
                       Client client, Car car) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalAmount = totalAmount;
        this.client = client;
        this.car = car;
    }
    
    // Getters and Setters
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
    
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public String getObservations() {
        return observations;
    }
    
    public void setObservations(String observations) {
        this.observations = observations;
    }
    
    public String getEvaluationNotes() {
        return evaluationNotes;
    }
    
    public void setEvaluationNotes(String evaluationNotes) {
        this.evaluationNotes = evaluationNotes;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
    
    public Car getCar() {
        return car;
    }
    
    public void setCar(Car car) {
        this.car = car;
    }
    
    public Agent getEvaluatedBy() {
        return evaluatedBy;
    }
    
    public void setEvaluatedBy(Agent evaluatedBy) {
        this.evaluatedBy = evaluatedBy;
    }
    
    public Contract getContract() {
        return contract;
    }
    
    public void setContract(Contract contract) {
        this.contract = contract;
    }
    
    // Business methods
    public long getRentalDays() {
        return java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
    
    public boolean canBeModified() {
        return status == OrderStatus.PENDING || status == OrderStatus.UNDER_EVALUATION;
    }
    
    public boolean canBeCancelled() {
        return status != OrderStatus.CANCELLED && status != OrderStatus.COMPLETED;
    }
    
    public boolean canBeEvaluated() {
        return status == OrderStatus.PENDING || status == OrderStatus.UNDER_EVALUATION;
    }
}

