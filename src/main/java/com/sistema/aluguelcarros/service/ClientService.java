package com.sistema.aluguelcarros.service;

import com.sistema.aluguelcarros.model.Client;
import com.sistema.aluguelcarros.model.Employment;
import com.sistema.aluguelcarros.model.User;
import com.sistema.aluguelcarros.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    @Autowired
    private EmploymentService employmentService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Client> findAll() {
        return clientRepository.findAll();
    }
    
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }
    
    public Optional<Client> findByEmail(String email) {
        return clientRepository.findByEmail(email);
    }
    
    public Optional<Client> findByCpf(String cpf) {
        return clientRepository.findByCpf(cpf);
    }
    
    public List<Client> findByProfession(String profession) {
        return clientRepository.findByProfession(profession);
    }
    
    public List<Client> findActiveClients() {
        return clientRepository.findByActiveTrue();
    }
    
    public List<Client> findByNameContaining(String name) {
        return clientRepository.findByNameContaining(name);
    }
    
    public List<Client> findClientsWithRentalOrders() {
        return clientRepository.findClientsWithRentalOrders();
    }
    
    public Client save(Client client) {
        validateClient(client);
        
        // Configurar tipo de usuário e role
        client.setUserType(User.UserType.CLIENT);
        client.addRole(User.Role.ROLE_CLIENT);
        
        // Criptografar senha
        if (client.getPassword() != null && !client.getPassword().startsWith("$2a$")) {
            client.setPassword(passwordEncoder.encode(client.getPassword()));
        }
        
        return clientRepository.save(client);
    }
    
    public Client update(Client client) {
        if (client.getId() == null) {
            throw new IllegalArgumentException("ID do cliente não pode ser nulo para atualização");
        }
        
        Optional<Client> existingClient = clientRepository.findById(client.getId());
        if (existingClient.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        validateClient(client);
        
        // Se a senha não foi alterada, manter a senha existente
        if (client.getPassword() == null || client.getPassword().isEmpty()) {
            client.setPassword(existingClient.get().getPassword());
        } else if (!client.getPassword().startsWith("$2a$")) {
            client.setPassword(passwordEncoder.encode(client.getPassword()));
        }
        
        return clientRepository.save(client);
    }
    
    public void deleteById(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        // Verificar se o cliente tem pedidos ativos
        if (hasActiveRentalOrders(client.get())) {
            throw new IllegalStateException("Não é possível excluir cliente com pedidos ativos");
        }
        
        clientRepository.deleteById(id);
    }
    
    public void deactivateClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            client.get().setActive(false);
            clientRepository.save(client.get());
        } else {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
    }
    
    public void activateClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            client.get().setActive(true);
            clientRepository.save(client.get());
        } else {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
    }
    
    public BigDecimal getTotalIncome(Client client) {
        return employmentService.getTotalSalaryByClient(client);
    }
    
    public boolean canAddMoreEmployments(Client client) {
        return client.getEmployments().size() < 3;
    }
    
    public Client addEmployment(Long clientId, Employment employment) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        Client client = clientOpt.get();
        
        if (!canAddMoreEmployments(client)) {
            throw new IllegalStateException("Cliente já possui o máximo de 3 empregos");
        }
        
        employment.setClient(client);
        employmentService.save(employment);
        
        client.addEmployment(employment);
        return clientRepository.save(client);
    }
    
    public Client removeEmployment(Long clientId, Long employmentId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) {
            throw new IllegalArgumentException("Cliente não encontrado");
        }
        
        Client client = clientOpt.get();
        Employment employment = client.getEmployments().stream()
                .filter(e -> e.getId().equals(employmentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Emprego não encontrado"));
        
        client.removeEmployment(employment);
        employmentService.deleteById(employmentId);
        
        return clientRepository.save(client);
    }
    
    public boolean hasActiveRentalOrders(Client client) {
        return client.getRentalOrders().stream()
                .anyMatch(order -> order.getStatus().name().equals("ACTIVE") || 
                                 order.getStatus().name().equals("APPROVED"));
    }
    
    public boolean isEligibleForRental(Client client) {
        // Verificar se o cliente está ativo
        if (!client.isActive()) {
            return false;
        }
        
        // Verificar se tem pelo menos um emprego
        if (client.getEmployments().isEmpty()) {
            return false;
        }
        
        // Verificar renda mínima (exemplo: R$ 1000)
        BigDecimal totalIncome = getTotalIncome(client);
        return totalIncome.compareTo(new BigDecimal("1000.00")) >= 0;
    }
    
    private void validateClient(Client client) {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (client.getCpf() == null || client.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        if (client.getRg() == null || client.getRg().trim().isEmpty()) {
            throw new IllegalArgumentException("RG é obrigatório");
        }
        
        if (client.getProfession() == null || client.getProfession().trim().isEmpty()) {
            throw new IllegalArgumentException("Profissão é obrigatória");
        }
        
        // Validar unicidade de email
        Optional<Client> existingByEmail = clientRepository.findByEmail(client.getEmail());
        if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(client.getId())) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        
        // Validar unicidade de CPF
        Optional<Client> existingByCpf = clientRepository.findByCpf(client.getCpf());
        if (existingByCpf.isPresent() && !existingByCpf.get().getId().equals(client.getId())) {
            throw new IllegalArgumentException("CPF já está em uso");
        }
    }
}

