package com.sistema.aluguelcarros.service;

import com.sistema.aluguelcarros.model.*;
import com.sistema.aluguelcarros.repository.RentalOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RentalOrderService {
    
    @Autowired
    private RentalOrderRepository rentalOrderRepository;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private ContractService contractService;
    
    public List<RentalOrder> findAll() {
        return rentalOrderRepository.findAll();
    }
    
    public Optional<RentalOrder> findById(Long id) {
        return rentalOrderRepository.findById(id);
    }
    
    public List<RentalOrder> findByClient(Client client) {
        return rentalOrderRepository.findByClient(client);
    }
    
    public List<RentalOrder> findByClientId(Long clientId) {
        return rentalOrderRepository.findByClientId(clientId);
    }
    
    public List<RentalOrder> findByCar(Car car) {
        return rentalOrderRepository.findByCar(car);
    }
    
    public List<RentalOrder> findByStatus(RentalOrder.OrderStatus status) {
        return rentalOrderRepository.findByStatus(status);
    }
    
    public List<RentalOrder> findByEvaluatedBy(Agent agent) {
        return rentalOrderRepository.findByEvaluatedBy(agent);
    }
    
    public List<RentalOrder> findPendingOrders() {
        return rentalOrderRepository.findPendingOrdersOrderByCreatedAt();
    }
    
    public List<RentalOrder> findOrdersUnderEvaluationByAgent(Agent agent) {
        return rentalOrderRepository.findOrdersUnderEvaluationByAgent(agent);
    }
    
    public List<RentalOrder> findExpiredActiveOrders() {
        return rentalOrderRepository.findExpiredActiveOrders(LocalDate.now());
    }
    
    public List<RentalOrder> findByClientOrderByCreatedAtDesc(Client client) {
        return rentalOrderRepository.findByClientOrderByCreatedAtDesc(client);
    }
    
    public Long countByClient(Client client) {
        return rentalOrderRepository.countByClient(client);
    }
    
    public Long countByStatus(RentalOrder.OrderStatus status) {
        return rentalOrderRepository.countByStatus(status);
    }
    
    public RentalOrder save(RentalOrder rentalOrder) {
        validateRentalOrder(rentalOrder);
        
        // Calcular valor total se não foi informado
        if (rentalOrder.getTotalAmount() == null) {
            BigDecimal totalAmount = carService.calculateRentalCost(
                rentalOrder.getCar(), 
                rentalOrder.getStartDate(), 
                rentalOrder.getEndDate()
            );
            rentalOrder.setTotalAmount(totalAmount);
        }
        
        return rentalOrderRepository.save(rentalOrder);
    }
    
    public RentalOrder update(RentalOrder rentalOrder) {
        if (rentalOrder.getId() == null) {
            throw new IllegalArgumentException("ID do pedido não pode ser nulo para atualização");
        }
        
        Optional<RentalOrder> existingOrder = rentalOrderRepository.findById(rentalOrder.getId());
        if (existingOrder.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        // Verificar se o pedido pode ser modificado
        if (!existingOrder.get().canBeModified()) {
            throw new IllegalStateException("Pedido não pode ser modificado no status atual");
        }
        
        validateRentalOrder(rentalOrder);
        
        return rentalOrderRepository.save(rentalOrder);
    }
    
    public void deleteById(Long id) {
        Optional<RentalOrder> order = rentalOrderRepository.findById(id);
        if (order.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        if (!order.get().canBeCancelled()) {
            throw new IllegalStateException("Pedido não pode ser excluído no status atual");
        }
        
        rentalOrderRepository.deleteById(id);
    }
    
    public RentalOrder createRentalOrder(Client client, Car car, LocalDate startDate, LocalDate endDate, String observations) {
        // Verificar se o cliente está elegível
        if (!clientService.isEligibleForRental(client)) {
            throw new IllegalStateException("Cliente não está elegível para aluguel");
        }
        
        // Verificar se o carro está disponível
        if (!carService.isAvailableForRental(car, startDate, endDate)) {
            throw new IllegalStateException("Carro não está disponível para o período solicitado");
        }
        
        // Calcular valor total
        BigDecimal totalAmount = carService.calculateRentalCost(car, startDate, endDate);
        
        // Criar pedido
        RentalOrder rentalOrder = new RentalOrder(startDate, endDate, totalAmount, client, car);
        rentalOrder.setObservations(observations);
        rentalOrder.setStatus(RentalOrder.OrderStatus.PENDING);
        
        return save(rentalOrder);
    }
    
    public RentalOrder evaluateOrder(Long orderId, Agent agent, RentalOrder.OrderStatus newStatus, String evaluationNotes) {
        Optional<RentalOrder> orderOpt = rentalOrderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        RentalOrder order = orderOpt.get();
        
        if (!order.canBeEvaluated()) {
            throw new IllegalStateException("Pedido não pode ser avaliado no status atual");
        }
        
        order.setStatus(newStatus);
        order.setEvaluatedBy(agent);
        order.setEvaluationNotes(evaluationNotes);
        
        // Se aprovado, criar contrato e alterar status do carro
        if (newStatus == RentalOrder.OrderStatus.APPROVED) {
            carService.setCarAsRented(order.getCar().getId());
            contractService.createContract(order, agent);
            order.setStatus(RentalOrder.OrderStatus.ACTIVE);
        }
        
        return rentalOrderRepository.save(order);
    }
    
    public RentalOrder cancelOrder(Long orderId, String reason) {
        Optional<RentalOrder> orderOpt = rentalOrderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        RentalOrder order = orderOpt.get();
        
        if (!order.canBeCancelled()) {
            throw new IllegalStateException("Pedido não pode ser cancelado no status atual");
        }
        
        order.setStatus(RentalOrder.OrderStatus.CANCELLED);
        order.setEvaluationNotes(reason);
        
        // Se o carro estava alugado, liberar
        if (order.getCar().getStatus() == Car.CarStatus.RENTED) {
            carService.setCarAsAvailable(order.getCar().getId());
        }
        
        return rentalOrderRepository.save(order);
    }
    
    public RentalOrder completeOrder(Long orderId) {
        Optional<RentalOrder> orderOpt = rentalOrderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            throw new IllegalArgumentException("Pedido não encontrado");
        }
        
        RentalOrder order = orderOpt.get();
        
        if (order.getStatus() != RentalOrder.OrderStatus.ACTIVE) {
            throw new IllegalStateException("Apenas pedidos ativos podem ser concluídos");
        }
        
        order.setStatus(RentalOrder.OrderStatus.COMPLETED);
        
        // Liberar o carro
        carService.setCarAsAvailable(order.getCar().getId());
        
        // Finalizar contrato se existir
        if (order.getContract() != null) {
            contractService.completeContract(order.getContract().getId());
        }
        
        return rentalOrderRepository.save(order);
    }
    
    public boolean hasConflictingRentals(Car car, LocalDate startDate, LocalDate endDate) {
        List<RentalOrder> conflictingOrders = rentalOrderRepository.findConflictingRentals(car, startDate, endDate);
        return !conflictingOrders.isEmpty();
    }
    
    public void processExpiredOrders() {
        List<RentalOrder> expiredOrders = findExpiredActiveOrders();
        for (RentalOrder order : expiredOrders) {
            completeOrder(order.getId());
        }
    }
    
    private void validateRentalOrder(RentalOrder rentalOrder) {
        if (rentalOrder.getClient() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        
        if (rentalOrder.getCar() == null) {
            throw new IllegalArgumentException("Carro é obrigatório");
        }
        
        if (rentalOrder.getStartDate() == null) {
            throw new IllegalArgumentException("Data de início é obrigatória");
        }
        
        if (rentalOrder.getEndDate() == null) {
            throw new IllegalArgumentException("Data de fim é obrigatória");
        }
        
        if (rentalOrder.getTotalAmount() == null) {
            throw new IllegalArgumentException("Valor total é obrigatório");
        }
        
        // Validar datas
        if (rentalOrder.getStartDate().isAfter(rentalOrder.getEndDate())) {
            throw new IllegalArgumentException("Data de início deve ser anterior à data de fim");
        }
        
        if (rentalOrder.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de início não pode ser no passado");
        }
        
        // Validar valor
        if (rentalOrder.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor total deve ser maior que zero");
        }
        
        // Para novos pedidos, verificar disponibilidade do carro
        if (rentalOrder.getId() == null) {
            if (hasConflictingRentals(rentalOrder.getCar(), rentalOrder.getStartDate(), rentalOrder.getEndDate())) {
                throw new IllegalStateException("Carro não está disponível para o período solicitado");
            }
        }
    }
}

