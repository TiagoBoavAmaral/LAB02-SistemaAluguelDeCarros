package com.sistema.aluguelcarros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car extends BaseEntity {
    
    @NotBlank(message = "Matrícula é obrigatória")
    @Column(nullable = false, unique = true, length = 20)
    private String registration;
    
    @NotNull(message = "Ano é obrigatório")
    @Positive(message = "Ano deve ser positivo")
    @Column(nullable = false)
    private Integer year;
    
    @NotBlank(message = "Marca é obrigatória")
    @Column(nullable = false, length = 50)
    private String brand;
    
    @NotBlank(message = "Modelo é obrigatório")
    @Column(nullable = false, length = 50)
    private String model;
    
    @NotBlank(message = "Placa é obrigatória")
    @Column(nullable = false, unique = true, length = 10)
    private String licensePlate;
    
    @Column(length = 50)
    private String color;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal dailyRate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CarStatus status = CarStatus.AVAILABLE;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OwnerType ownerType = OwnerType.COMPANY;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
    
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalOrder> rentalOrders = new ArrayList<>();
    
    public enum CarStatus {
        AVAILABLE, RENTED, MAINTENANCE, UNAVAILABLE
    }
    
    public enum OwnerType {
        CLIENT, COMPANY, BANK
    }
    
    // Constructors
    public Car() {}
    
    public Car(String registration, Integer year, String brand, String model, String licensePlate) {
        this.registration = registration;
        this.year = year;
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
    }
    
    // Getters and Setters
    public String getRegistration() {
        return registration;
    }
    
    public void setRegistration(String registration) {
        this.registration = registration;
    }
    
    public Integer getYear() {
        return year;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public String getModel() {
        return model;
    }
    
    public void setModel(String model) {
        this.model = model;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public BigDecimal getDailyRate() {
        return dailyRate;
    }
    
    public void setDailyRate(BigDecimal dailyRate) {
        this.dailyRate = dailyRate;
    }
    
    public CarStatus getStatus() {
        return status;
    }
    
    public void setStatus(CarStatus status) {
        this.status = status;
    }
    
    public OwnerType getOwnerType() {
        return ownerType;
    }
    
    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }
    
    public User getOwner() {
        return owner;
    }
    
    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public List<RentalOrder> getRentalOrders() {
        return rentalOrders;
    }
    
    public void setRentalOrders(List<RentalOrder> rentalOrders) {
        this.rentalOrders = rentalOrders;
    }
    
    public void addRentalOrder(RentalOrder rentalOrder) {
        rentalOrders.add(rentalOrder);
        rentalOrder.setCar(this);
    }
    
    public void removeRentalOrder(RentalOrder rentalOrder) {
        rentalOrders.remove(rentalOrder);
        rentalOrder.setCar(null);
    }
    
    public String getFullDescription() {
        return brand + " " + model + " " + year + " - " + licensePlate;
    }
}

