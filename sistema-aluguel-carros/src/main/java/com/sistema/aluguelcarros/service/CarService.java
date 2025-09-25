package com.sistema.aluguelcarros.service;

import com.sistema.aluguelcarros.model.Car;
import com.sistema.aluguelcarros.model.User;
import com.sistema.aluguelcarros.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CarService {
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private RentalOrderService rentalOrderService;
    
    public List<Car> findAll() {
        return carRepository.findAll();
    }
    
    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }
    
    public Optional<Car> findByRegistration(String registration) {
        return carRepository.findByRegistration(registration);
    }
    
    public Optional<Car> findByLicensePlate(String licensePlate) {
        return carRepository.findByLicensePlate(licensePlate);
    }
    
    public List<Car> findByStatus(Car.CarStatus status) {
        return carRepository.findByStatus(status);
    }
    
    public List<Car> findByBrand(String brand) {
        return carRepository.findByBrand(brand);
    }
    
    public List<Car> findByModel(String model) {
        return carRepository.findByModel(model);
    }
    
    public List<Car> findByBrandAndModel(String brand, String model) {
        return carRepository.findByBrandAndModel(brand, model);
    }
    
    public List<Car> findByYearBetween(Integer startYear, Integer endYear) {
        return carRepository.findByYearBetween(startYear, endYear);
    }
    
    public List<Car> findByOwnerType(Car.OwnerType ownerType) {
        return carRepository.findByOwnerType(ownerType);
    }
    
    public List<Car> findByOwner(User owner) {
        return carRepository.findByOwner(owner);
    }
    
    public List<Car> findAvailableCars() {
        return carRepository.findByStatus(Car.CarStatus.AVAILABLE);
    }
    
    public List<Car> findAvailableCarsOrderByDailyRate() {
        return carRepository.findAvailableCarsOrderByDailyRate();
    }
    
    public List<Car> findAvailableCarsByMaxRate(BigDecimal maxRate) {
        return carRepository.findAvailableCarsByMaxRate(maxRate);
    }
    
    public List<Car> findByDailyRateBetween(BigDecimal minRate, BigDecimal maxRate) {
        return carRepository.findByDailyRateBetween(minRate, maxRate);
    }
    
    public Long countByStatus(Car.CarStatus status) {
        return carRepository.countByStatus(status);
    }
    
    public Car save(Car car) {
        validateCar(car);
        return carRepository.save(car);
    }
    
    public Car update(Car car) {
        if (car.getId() == null) {
            throw new IllegalArgumentException("ID do carro não pode ser nulo para atualização");
        }
        
        Optional<Car> existingCar = carRepository.findById(car.getId());
        if (existingCar.isEmpty()) {
            throw new IllegalArgumentException("Carro não encontrado");
        }
        
        validateCar(car);
        
        return carRepository.save(car);
    }
    
    public void deleteById(Long id) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isEmpty()) {
            throw new IllegalArgumentException("Carro não encontrado");
        }
        
        // Verificar se o carro tem pedidos ativos
        if (hasActiveRentals(car.get())) {
            throw new IllegalStateException("Não é possível excluir carro com aluguéis ativos");
        }
        
        carRepository.deleteById(id);
    }
    
    public boolean existsByRegistration(String registration) {
        return carRepository.existsByRegistration(registration);
    }
    
    public boolean existsByLicensePlate(String licensePlate) {
        return carRepository.existsByLicensePlate(licensePlate);
    }
    
    public boolean isRegistrationAvailable(String registration, Long carId) {
        Optional<Car> existingCar = carRepository.findByRegistration(registration);
        return existingCar.isEmpty() || existingCar.get().getId().equals(carId);
    }
    
    public boolean isLicensePlateAvailable(String licensePlate, Long carId) {
        Optional<Car> existingCar = carRepository.findByLicensePlate(licensePlate);
        return existingCar.isEmpty() || existingCar.get().getId().equals(carId);
    }
    
    public boolean isAvailableForRental(Car car, LocalDate startDate, LocalDate endDate) {
        if (car.getStatus() != Car.CarStatus.AVAILABLE) {
            return false;
        }
        
        // Verificar se não há conflitos com outros aluguéis
        return !rentalOrderService.hasConflictingRentals(car, startDate, endDate);
    }
    
    public void setCarAsRented(Long carId) {
        Optional<Car> carOpt = carRepository.findById(carId);
        if (carOpt.isPresent()) {
            Car car = carOpt.get();
            car.setStatus(Car.CarStatus.RENTED);
            carRepository.save(car);
        }
    }
    
    public void setCarAsAvailable(Long carId) {
        Optional<Car> carOpt = carRepository.findById(carId);
        if (carOpt.isPresent()) {
            Car car = carOpt.get();
            car.setStatus(Car.CarStatus.AVAILABLE);
            carRepository.save(car);
        }
    }
    
    public void setCarInMaintenance(Long carId) {
        Optional<Car> carOpt = carRepository.findById(carId);
        if (carOpt.isPresent()) {
            Car car = carOpt.get();
            car.setStatus(Car.CarStatus.MAINTENANCE);
            carRepository.save(car);
        }
    }
    
    public boolean hasActiveRentals(Car car) {
        return car.getRentalOrders().stream()
                .anyMatch(order -> order.getStatus().name().equals("ACTIVE") || 
                                 order.getStatus().name().equals("APPROVED"));
    }
    
    public BigDecimal calculateRentalCost(Car car, LocalDate startDate, LocalDate endDate) {
        if (car.getDailyRate() == null) {
            throw new IllegalArgumentException("Carro não possui diária definida");
        }
        
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) + 1;
        return car.getDailyRate().multiply(new BigDecimal(days));
    }
    
    public List<Car> searchCars(String brand, String model, Integer year, BigDecimal maxDailyRate) {
        List<Car> cars = findAvailableCars();
        
        return cars.stream()
                .filter(car -> brand == null || car.getBrand().toLowerCase().contains(brand.toLowerCase()))
                .filter(car -> model == null || car.getModel().toLowerCase().contains(model.toLowerCase()))
                .filter(car -> year == null || car.getYear().equals(year))
                .filter(car -> maxDailyRate == null || car.getDailyRate() == null || 
                              car.getDailyRate().compareTo(maxDailyRate) <= 0)
                .toList();
    }
    
    private void validateCar(Car car) {
        if (car.getRegistration() == null || car.getRegistration().trim().isEmpty()) {
            throw new IllegalArgumentException("Matrícula é obrigatória");
        }
        
        if (car.getYear() == null) {
            throw new IllegalArgumentException("Ano é obrigatório");
        }
        
        if (car.getBrand() == null || car.getBrand().trim().isEmpty()) {
            throw new IllegalArgumentException("Marca é obrigatória");
        }
        
        if (car.getModel() == null || car.getModel().trim().isEmpty()) {
            throw new IllegalArgumentException("Modelo é obrigatório");
        }
        
        if (car.getLicensePlate() == null || car.getLicensePlate().trim().isEmpty()) {
            throw new IllegalArgumentException("Placa é obrigatória");
        }
        
        // Validar ano
        int currentYear = LocalDate.now().getYear();
        if (car.getYear() < 1900 || car.getYear() > currentYear + 1) {
            throw new IllegalArgumentException("Ano inválido");
        }
        
        // Validar unicidade de matrícula
        if (!isRegistrationAvailable(car.getRegistration(), car.getId())) {
            throw new IllegalArgumentException("Matrícula já está em uso");
        }
        
        // Validar unicidade de placa
        if (!isLicensePlateAvailable(car.getLicensePlate(), car.getId())) {
            throw new IllegalArgumentException("Placa já está em uso");
        }
        
        // Validar diária se informada
        if (car.getDailyRate() != null && car.getDailyRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Diária deve ser maior que zero");
        }
    }
}

