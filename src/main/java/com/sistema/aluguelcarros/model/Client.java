package com.sistema.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "user_id")
public class Client extends User {
    
    @NotBlank(message = "Profissão é obrigatória")
    @Column(nullable = false, length = 100)
    private String profession;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employment> employments = new ArrayList<>();
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalOrder> rentalOrders = new ArrayList<>();
    
    // Constructors
    public Client() {
        super();
        setUserType(UserType.CLIENT);
        addRole(Role.ROLE_CLIENT);
    }
    
    public Client(String name, String email, String password, String cpf, String rg, String profession) {
        super(name, email, password, cpf, rg, UserType.CLIENT);
        this.profession = profession;
        addRole(Role.ROLE_CLIENT);
    }
    
    // Getters and Setters
    public String getProfession() {
        return profession;
    }
    
    public void setProfession(String profession) {
        this.profession = profession;
    }
    
    public List<Employment> getEmployments() {
        return employments;
    }
    
    public void setEmployments(List<Employment> employments) {
        this.employments = employments;
    }
    
    public void addEmployment(Employment employment) {
        employments.add(employment);
        employment.setClient(this);
    }
    
    public void removeEmployment(Employment employment) {
        employments.remove(employment);
        employment.setClient(null);
    }
    
    public List<RentalOrder> getRentalOrders() {
        return rentalOrders;
    }
    
    public void setRentalOrders(List<RentalOrder> rentalOrders) {
        this.rentalOrders = rentalOrders;
    }
    
    public void addRentalOrder(RentalOrder rentalOrder) {
        rentalOrders.add(rentalOrder);
        rentalOrder.setClient(this);
    }
    
    public void removeRentalOrder(RentalOrder rentalOrder) {
        rentalOrders.remove(rentalOrder);
        rentalOrder.setClient(null);
    }
}

