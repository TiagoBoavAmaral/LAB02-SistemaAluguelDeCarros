package com.sistema.aluguelcarros.service;

import com.sistema.aluguelcarros.model.Agent;
import com.sistema.aluguelcarros.model.User;
import com.sistema.aluguelcarros.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AgentService {
    
    @Autowired
    private AgentRepository agentRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<Agent> findAll() {
        return agentRepository.findAll();
    }
    
    public Optional<Agent> findById(Long id) {
        return agentRepository.findById(id);
    }
    
    public Optional<Agent> findByEmail(String email) {
        return agentRepository.findByEmail(email);
    }
    
    public Optional<Agent> findByCnpj(String cnpj) {
        return agentRepository.findByCnpj(cnpj);
    }
    
    public List<Agent> findByAgentType(Agent.AgentType agentType) {
        return agentRepository.findByAgentType(agentType);
    }
    
    public List<Agent> findActiveAgents() {
        return agentRepository.findByActiveTrue();
    }
    
    public List<Agent> findActiveAgentsByType(Agent.AgentType agentType) {
        return agentRepository.findActiveAgentsByType(agentType);
    }
    
    public List<Agent> findByCompanyNameContaining(String companyName) {
        return agentRepository.findByCompanyNameContaining(companyName);
    }
    
    public List<Agent> findAgentsWithEvaluatedOrders() {
        return agentRepository.findAgentsWithEvaluatedOrders();
    }
    
    public List<Agent> findBanksWithContracts() {
        return agentRepository.findBanksWithContracts();
    }
    
    public Agent save(Agent agent) {
        validateAgent(agent);
        
        // Configurar tipo de usuário e role
        agent.setUserType(User.UserType.AGENT);
        agent.addRole(User.Role.ROLE_AGENT);
        
        // Criptografar senha
        if (agent.getPassword() != null && !agent.getPassword().startsWith("$2a$")) {
            agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        }
        
        return agentRepository.save(agent);
    }
    
    public Agent update(Agent agent) {
        if (agent.getId() == null) {
            throw new IllegalArgumentException("ID do agente não pode ser nulo para atualização");
        }
        
        Optional<Agent> existingAgent = agentRepository.findById(agent.getId());
        if (existingAgent.isEmpty()) {
            throw new IllegalArgumentException("Agente não encontrado");
        }
        
        validateAgent(agent);
        
        // Se a senha não foi alterada, manter a senha existente
        if (agent.getPassword() == null || agent.getPassword().isEmpty()) {
            agent.setPassword(existingAgent.get().getPassword());
        } else if (!agent.getPassword().startsWith("$2a$")) {
            agent.setPassword(passwordEncoder.encode(agent.getPassword()));
        }
        
        return agentRepository.save(agent);
    }
    
    public void deleteById(Long id) {
        Optional<Agent> agent = agentRepository.findById(id);
        if (agent.isEmpty()) {
            throw new IllegalArgumentException("Agente não encontrado");
        }
        
        // Verificar se o agente tem pedidos ou contratos ativos
        if (hasActiveEvaluations(agent.get()) || hasActiveContracts(agent.get())) {
            throw new IllegalStateException("Não é possível excluir agente com avaliações ou contratos ativos");
        }
        
        agentRepository.deleteById(id);
    }
    
    public void deactivateAgent(Long id) {
        Optional<Agent> agent = agentRepository.findById(id);
        if (agent.isPresent()) {
            agent.get().setActive(false);
            agentRepository.save(agent.get());
        } else {
            throw new IllegalArgumentException("Agente não encontrado");
        }
    }
    
    public void activateAgent(Long id) {
        Optional<Agent> agent = agentRepository.findById(id);
        if (agent.isPresent()) {
            agent.get().setActive(true);
            agentRepository.save(agent.get());
        } else {
            throw new IllegalArgumentException("Agente não encontrado");
        }
    }
    
    public boolean existsByCnpj(String cnpj) {
        return agentRepository.existsByCnpj(cnpj);
    }
    
    public boolean isCnpjAvailable(String cnpj, Long agentId) {
        Optional<Agent> existingAgent = agentRepository.findByCnpj(cnpj);
        return existingAgent.isEmpty() || existingAgent.get().getId().equals(agentId);
    }
    
    public boolean canEvaluateOrders(Agent agent) {
        return agent.isActive() && agent.getUserType() == User.UserType.AGENT;
    }
    
    public boolean canProvideCredit(Agent agent) {
        return agent.isActive() && 
               agent.getAgentType() == Agent.AgentType.BANK;
    }
    
    public boolean hasActiveEvaluations(Agent agent) {
        return agent.getEvaluatedOrders().stream()
                .anyMatch(order -> order.getStatus().name().equals("UNDER_EVALUATION"));
    }
    
    public boolean hasActiveContracts(Agent agent) {
        return agent.getContracts().stream()
                .anyMatch(contract -> contract.getStatus().name().equals("ACTIVE"));
    }
    
    private void validateAgent(Agent agent) {
        if (agent.getName() == null || agent.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (agent.getEmail() == null || agent.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (agent.getCpf() == null || agent.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        if (agent.getRg() == null || agent.getRg().trim().isEmpty()) {
            throw new IllegalArgumentException("RG é obrigatório");
        }
        
        if (agent.getCnpj() == null || agent.getCnpj().trim().isEmpty()) {
            throw new IllegalArgumentException("CNPJ é obrigatório");
        }
        
        if (agent.getCompanyName() == null || agent.getCompanyName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da empresa é obrigatório");
        }
        
        if (agent.getAgentType() == null) {
            throw new IllegalArgumentException("Tipo de agente é obrigatório");
        }
        
        // Validar unicidade de email
        Optional<Agent> existingByEmail = agentRepository.findByEmail(agent.getEmail());
        if (existingByEmail.isPresent() && !existingByEmail.get().getId().equals(agent.getId())) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        
        // Validar unicidade de CNPJ
        if (!isCnpjAvailable(agent.getCnpj(), agent.getId())) {
            throw new IllegalArgumentException("CNPJ já está em uso");
        }
        
        // Validar formato do CNPJ (básico)
        if (!isValidCnpj(agent.getCnpj())) {
            throw new IllegalArgumentException("CNPJ inválido");
        }
    }
    
    private boolean isValidCnpj(String cnpj) {
        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        // Verifica se tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }
        
        // Aqui poderia implementar a validação completa do CNPJ
        // Por simplicidade, vamos considerar válido se passou nas verificações básicas
        return true;
    }
}

