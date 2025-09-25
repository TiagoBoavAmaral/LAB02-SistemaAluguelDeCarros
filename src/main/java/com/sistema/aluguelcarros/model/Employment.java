package com.sistema.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
@Table(name = "employments")
public class Employment extends BaseEntity {
    
    @NotBlank(message = "Nome da empresa é obrigatório")
    @Column(nullable = false, length = 200)
    private String companyName;
    
    @NotNull(message = "Salário é obrigatório")
    @Positive(message = "Salário deve ser positivo")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal salary;
    
    @Column(length = 100)
    private String position;
    
    @Column(length = 200)
    private String companyAddress;
    
    @Column(length = 20)
    private String companyPhone;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    
    // Constructors
    public Employment() {}
    
    public Employment(String companyName, BigDecimal salary, String position, Client client) {
        this.companyName = companyName;
        this.salary = salary;
        this.position = position;
        this.client = client;
    }
    
    // Getters and Setters
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public String getPosition() {
        return position;
    }
    
    public void setPosition(String position) {
        this.position = position;
    }
    
    public String getCompanyAddress() {
        return companyAddress;
    }
    
    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress;
    }
    
    public String getCompanyPhone() {
        return companyPhone;
    }
    
    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }
    
    public Client getClient() {
        return client;
    }
    
    public void setClient(Client client) {
        this.client = client;
    }
}

