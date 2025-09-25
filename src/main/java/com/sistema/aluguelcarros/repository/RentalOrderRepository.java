package com.sistema.aluguelcarros.repository;

import com.sistema.aluguelcarros.model.RentalOrder;
import com.sistema.aluguelcarros.model.Client;
import com.sistema.aluguelcarros.model.Car;
import com.sistema.aluguelcarros.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentalOrderRepository extends JpaRepository<RentalOrder, Long> {
    
    List<RentalOrder> findByClient(Client client);
    
    List<RentalOrder> findByClientId(Long clientId);
    
    List<RentalOrder> findByCar(Car car);
    
    List<RentalOrder> findByCarId(Long carId);
    
    List<RentalOrder> findByStatus(RentalOrder.OrderStatus status);
    
    List<RentalOrder> findByEvaluatedBy(Agent agent);
    
    @Query("SELECT r FROM RentalOrder r WHERE r.client = :client AND r.status = :status")
    List<RentalOrder> findByClientAndStatus(@Param("client") Client client, 
                                           @Param("status") RentalOrder.OrderStatus status);
    
    @Query("SELECT r FROM RentalOrder r WHERE r.startDate >= :startDate AND r.endDate <= :endDate")
    List<RentalOrder> findByDateRange(@Param("startDate") LocalDate startDate, 
                                     @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM RentalOrder r WHERE r.car = :car AND r.status IN ('APPROVED', 'ACTIVE') " +
           "AND ((r.startDate <= :endDate) AND (r.endDate >= :startDate))")
    List<RentalOrder> findConflictingRentals(@Param("car") Car car, 
                                           @Param("startDate") LocalDate startDate, 
                                           @Param("endDate") LocalDate endDate);
    
    @Query("SELECT r FROM RentalOrder r WHERE r.status = 'PENDING' ORDER BY r.createdAt ASC")
    List<RentalOrder> findPendingOrdersOrderByCreatedAt();
    
    @Query("SELECT r FROM RentalOrder r WHERE r.status = 'UNDER_EVALUATION' AND r.evaluatedBy = :agent")
    List<RentalOrder> findOrdersUnderEvaluationByAgent(@Param("agent") Agent agent);
    
    @Query("SELECT r FROM RentalOrder r WHERE r.endDate < :currentDate AND r.status = 'ACTIVE'")
    List<RentalOrder> findExpiredActiveOrders(@Param("currentDate") LocalDate currentDate);
    
    @Query("SELECT COUNT(r) FROM RentalOrder r WHERE r.client = :client")
    Long countByClient(@Param("client") Client client);
    
    @Query("SELECT COUNT(r) FROM RentalOrder r WHERE r.status = :status")
    Long countByStatus(@Param("status") RentalOrder.OrderStatus status);
    
    @Query("SELECT r FROM RentalOrder r WHERE r.client = :client ORDER BY r.createdAt DESC")
    List<RentalOrder> findByClientOrderByCreatedAtDesc(@Param("client") Client client);
}

