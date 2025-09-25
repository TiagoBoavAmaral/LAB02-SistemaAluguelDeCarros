package com.sistema.aluguelcarros.service;

import com.sistema.aluguelcarros.model.Employment;
import com.sistema.aluguelcarros.model.Client;
import com.sistema.aluguelcarros.repository.EmploymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EmploymentService {
    
    @Autowired
    private EmploymentRepository employmentRepository;
    
    public List<Employment> findAll() {
        return employmentRepository.findAll();
    }
    
    public Optional<Employment> findById(Long id) {
        return employmentRepository.findById(id);
    }
    
    public List<Employment> findByClient(Client client) {
        return employmentRepository.findByClient(client);
    }
    
    public List<Employment> findByClientId(Long clientId) {
        return employmentRepository.findByClientId(clientId);
    }
    
    public List<Employment> findByCompanyName(String companyName) {
        return employmentRepository.findByCompanyName(companyName);
    }
    
    public List<Employment> findByCompanyNameContaining(String companyName) {
        return employmentRepository.findByCompanyNameContaining(companyName);
    }
    
    public List<Employment> findBySalaryGreaterThanEqual(BigDecimal minSalary) {
        return employmentRepository.findBySalaryGreaterThanEqual(minSalary);
    }
    
    public List<Employment> findBySalaryBetween(BigDecimal minSalary, BigDecimal maxSalary) {
        return employmentRepository.findBySalaryBetween(minSalary, maxSalary);
    }
    
    public BigDecimal getTotalSalaryByClient(Client client) {
        BigDecimal total = employmentRepository.getTotalSalaryByClient(client);
        return total != null ? total : BigDecimal.ZERO;
    }
    
    public Long countByClient(Client client) {
        return employmentRepository.countByClient(client);
    }
    
    public Employment save(Employment employment) {
        validateEmployment(employment);
        
        // Verificar se o cliente já tem 3 empregos
        if (employment.getId() == null) { // Novo emprego
            Long currentCount = countByClient(employment.getClient());
            if (currentCount >= 3) {
                throw new IllegalStateException("Cliente já possui o máximo de 3 empregos");
            }
        }
        
        return employmentRepository.save(employment);
    }
    
    public Employment update(Employment employment) {
        if (employment.getId() == null) {
            throw new IllegalArgumentException("ID do emprego não pode ser nulo para atualização");
        }
        
        Optional<Employment> existingEmployment = employmentRepository.findById(employment.getId());
        if (existingEmployment.isEmpty()) {
            throw new IllegalArgumentException("Emprego não encontrado");
        }
        
        validateEmployment(employment);
        
        return employmentRepository.save(employment);
    }
    
    public void deleteById(Long id) {
        if (!employmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Emprego não encontrado");
        }
        employmentRepository.deleteById(id);
    }
    
    public boolean canClientAddMoreEmployments(Client client) {
        Long currentCount = countByClient(client);
        return currentCount < 3;
    }
    
    public List<Employment> findHighSalaryEmployments(BigDecimal threshold) {
        return employmentRepository.findBySalaryGreaterThanEqual(threshold);
    }
    
    public List<Employment> findByPositionContaining(String position) {
        return employmentRepository.findByPositionContaining(position);
    }
    
    public BigDecimal getAverageSalaryByClient(Client client) {
        List<Employment> employments = findByClient(client);
        if (employments.isEmpty()) {
            return BigDecimal.ZERO;
        }
        
        BigDecimal total = employments.stream()
                .map(Employment::getSalary)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        return total.divide(new BigDecimal(employments.size()), 2, BigDecimal.ROUND_HALF_UP);
    }
    
    public Employment getHighestSalaryEmployment(Client client) {
        List<Employment> employments = findByClient(client);
        return employments.stream()
                .max((e1, e2) -> e1.getSalary().compareTo(e2.getSalary()))
                .orElse(null);
    }
    
    public boolean hasStableIncome(Client client) {
        List<Employment> employments = findByClient(client);
        
        // Considera renda estável se tem pelo menos 1 emprego com salário >= R$ 1000
        return employments.stream()
                .anyMatch(emp -> emp.getSalary().compareTo(new BigDecimal("1000.00")) >= 0);
    }
    
    private void validateEmployment(Employment employment) {
        if (employment.getClient() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }
        
        if (employment.getCompanyName() == null || employment.getCompanyName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da empresa é obrigatório");
        }
        
        if (employment.getSalary() == null) {
            throw new IllegalArgumentException("Salário é obrigatório");
        }
        
        if (employment.getSalary().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Salário deve ser maior que zero");
        }
        
        // Validar salário máximo razoável (exemplo: R$ 100.000)
        if (employment.getSalary().compareTo(new BigDecimal("100000.00")) > 0) {
            throw new IllegalArgumentException("Salário informado parece muito alto");
        }
    }
}

