package com.sistema.aluguelcarros.repository;

import com.sistema.aluguelcarros.model.Car;
import com.sistema.aluguelcarros.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    
    Optional<Car> findByRegistration(String registration);
    
    Optional<Car> findByLicensePlate(String licensePlate);
    
    boolean existsByRegistration(String registration);
    
    boolean existsByLicensePlate(String licensePlate);
    
    List<Car> findByStatus(Car.CarStatus status);
    
    List<Car> findByBrand(String brand);
    
    List<Car> findByModel(String model);
    
    List<Car> findByYear(Integer year);
    
    List<Car> findByOwnerType(Car.OwnerType ownerType);
    
    List<Car> findByOwner(User owner);
    
    @Query("SELECT c FROM Car c WHERE c.brand LIKE %:brand%")
    List<Car> findByBrandContaining(@Param("brand") String brand);
    
    @Query("SELECT c FROM Car c WHERE c.model LIKE %:model%")
    List<Car> findByModelContaining(@Param("model") String model);
    
    @Query("SELECT c FROM Car c WHERE c.brand = :brand AND c.model = :model")
    List<Car> findByBrandAndModel(@Param("brand") String brand, @Param("model") String model);
    
    @Query("SELECT c FROM Car c WHERE c.year BETWEEN :startYear AND :endYear")
    List<Car> findByYearBetween(@Param("startYear") Integer startYear, @Param("endYear") Integer endYear);
    
    @Query("SELECT c FROM Car c WHERE c.dailyRate BETWEEN :minRate AND :maxRate")
    List<Car> findByDailyRateBetween(@Param("minRate") BigDecimal minRate, @Param("maxRate") BigDecimal maxRate);
    
    @Query("SELECT c FROM Car c WHERE c.status = 'AVAILABLE' ORDER BY c.dailyRate ASC")
    List<Car> findAvailableCarsOrderByDailyRate();
    
    @Query("SELECT c FROM Car c WHERE c.status = 'AVAILABLE' AND c.dailyRate <= :maxRate")
    List<Car> findAvailableCarsByMaxRate(@Param("maxRate") BigDecimal maxRate);
    
    @Query("SELECT COUNT(c) FROM Car c WHERE c.status = :status")
    Long countByStatus(@Param("status") Car.CarStatus status);
}

