package com.sistema.aluguelcarros.service;

import com.sistema.aluguelcarros.model.Contract;
import com.sistema.aluguelcarros.model.RentalOrder;
import com.sistema.aluguelcarros.model.Agent;
import com.sistema.aluguelcarros.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ContractService {
    
    @Autowired
    private ContractRepository contractRepository;
    
    public List<Contract> findAll() {
        return contractRepository.findAll();
    }
    
    public Optional<Contract> findById(Long id) {
        return contractRepository.findById(id);
    }
    
    public Optional<Contract> findByContractNumber(String contractNumber) {
        return contractRepository.findByContractNumber(contractNumber);
    }
    
    public Optional<Contract> findByRentalOrder(RentalOrder rentalOrder) {
        return contractRepository.findByRentalOrder(rentalOrder);
    }
    
    public List<Contract> findByBank(Agent bank) {
        return contractRepository.findByBank(bank);
    }
    
    public List<Contract> findByStatus(Contract.ContractStatus status) {
        return contractRepository.findByStatus(status);
    }
    
    public List<Contract> findByContractType(Contract.ContractType contractType) {
        return contractRepository.findByContractType(contractType);
    }
    
    public List<Contract> findBySignatureDateBetween(LocalDate startDate, LocalDate endDate) {
        return contractRepository.findBySignatureDateBetween(startDate, endDate);
    }
    
    public List<Contract> findExpiredActiveContracts() {
        return contractRepository.findExpiredActiveContracts(LocalDate.now());
    }
    
    public List<Contract> findActiveContractsByBank(Agent bank) {
        return contractRepository.findActiveContractsByBank(bank);
    }
    
    public List<Contract> findContractsWithCredit() {
        return contractRepository.findContractsWithCredit();
    }
    
    public List<Contract> findByClientId(Long clientId) {
        return contractRepository.findByClientId(clientId);
    }
    
    public List<Contract> findAllOrderBySignatureDateDesc() {
        return contractRepository.findAllOrderBySignatureDateDesc();
    }
    
    public Long countByBank(Agent bank) {
        return contractRepository.countByBank(bank);
    }
    
    public Long countByStatus(Contract.ContractStatus status) {
        return contractRepository.countByStatus(status);
    }
    
    public Contract save(Contract contract) {
        validateContract(contract);
        return contractRepository.save(contract);
    }
    
    public Contract update(Contract contract) {
        if (contract.getId() == null) {
            throw new IllegalArgumentException("ID do contrato não pode ser nulo para atualização");
        }
        
        Optional<Contract> existingContract = contractRepository.findById(contract.getId());
        if (existingContract.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado");
        }
        
        validateContract(contract);
        
        return contractRepository.save(contract);
    }
    
    public void deleteById(Long id) {
        Optional<Contract> contract = contractRepository.findById(id);
        if (contract.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado");
        }
        
        if (contract.get().isActive()) {
            throw new IllegalStateException("Não é possível excluir contrato ativo");
        }
        
        contractRepository.deleteById(id);
    }
    
    public Contract createContract(RentalOrder rentalOrder, Agent agent) {
        // Verificar se já existe contrato para este pedido
        Optional<Contract> existingContract = findByRentalOrder(rentalOrder);
        if (existingContract.isPresent()) {
            throw new IllegalStateException("Já existe contrato para este pedido");
        }
        
        Contract contract = new Contract();
        contract.setRentalOrder(rentalOrder);
        contract.setSignatureDate(LocalDate.now());
        contract.setStartDate(rentalOrder.getStartDate());
        contract.setEndDate(rentalOrder.getEndDate());
        contract.setContractValue(rentalOrder.getTotalAmount());
        contract.setContractType(Contract.ContractType.RENTAL);
        contract.setStatus(Contract.ContractStatus.ACTIVE);
        
        // Se o agente for um banco, pode associar crédito
        if (agent != null && agent.getAgentType() == Agent.AgentType.BANK) {
            contract.setBank(agent);
        }
        
        return save(contract);
    }
    
    public Contract createCreditContract(RentalOrder rentalOrder, Agent bank, BigDecimal creditAmount, BigDecimal interestRate) {
        if (bank.getAgentType() != Agent.AgentType.BANK) {
            throw new IllegalArgumentException("Apenas bancos podem conceder crédito");
        }
        
        Contract contract = createContract(rentalOrder, bank);
        contract.setContractType(Contract.ContractType.RENTAL_WITH_CREDIT);
        contract.setCreditAmount(creditAmount);
        contract.setInterestRate(interestRate);
        contract.setBank(bank);
        
        return save(contract);
    }
    
    public Contract completeContract(Long contractId) {
        Optional<Contract> contractOpt = contractRepository.findById(contractId);
        if (contractOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado");
        }
        
        Contract contract = contractOpt.get();
        
        if (!contract.isActive()) {
            throw new IllegalStateException("Apenas contratos ativos podem ser concluídos");
        }
        
        contract.setStatus(Contract.ContractStatus.COMPLETED);
        
        return contractRepository.save(contract);
    }
    
    public Contract cancelContract(Long contractId, String reason) {
        Optional<Contract> contractOpt = contractRepository.findById(contractId);
        if (contractOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado");
        }
        
        Contract contract = contractOpt.get();
        
        if (!contract.isActive()) {
            throw new IllegalStateException("Apenas contratos ativos podem ser cancelados");
        }
        
        contract.setStatus(Contract.ContractStatus.CANCELLED);
        contract.setTerms(contract.getTerms() + "\nMotivo do cancelamento: " + reason);
        
        return contractRepository.save(contract);
    }
    
    public Contract suspendContract(Long contractId, String reason) {
        Optional<Contract> contractOpt = contractRepository.findById(contractId);
        if (contractOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado");
        }
        
        Contract contract = contractOpt.get();
        
        if (!contract.isActive()) {
            throw new IllegalStateException("Apenas contratos ativos podem ser suspensos");
        }
        
        contract.setStatus(Contract.ContractStatus.SUSPENDED);
        contract.setTerms(contract.getTerms() + "\nMotivo da suspensão: " + reason);
        
        return contractRepository.save(contract);
    }
    
    public Contract reactivateContract(Long contractId) {
        Optional<Contract> contractOpt = contractRepository.findById(contractId);
        if (contractOpt.isEmpty()) {
            throw new IllegalArgumentException("Contrato não encontrado");
        }
        
        Contract contract = contractOpt.get();
        
        if (contract.getStatus() != Contract.ContractStatus.SUSPENDED) {
            throw new IllegalStateException("Apenas contratos suspensos podem ser reativados");
        }
        
        contract.setStatus(Contract.ContractStatus.ACTIVE);
        
        return contractRepository.save(contract);
    }
    
    public BigDecimal calculateTotalWithInterest(Contract contract) {
        if (!contract.hasCredit() || contract.getInterestRate() == null || contract.getCreditAmount() == null) {
            return contract.getContractValue();
        }
        
        BigDecimal interest = contract.getCreditAmount()
                .multiply(contract.getInterestRate())
                .divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
        
        return contract.getContractValue().add(interest);
    }
    
    public void processExpiredContracts() {
        List<Contract> expiredContracts = findExpiredActiveContracts();
        for (Contract contract : expiredContracts) {
            completeContract(contract.getId());
        }
    }
    
    private void validateContract(Contract contract) {
        if (contract.getRentalOrder() == null) {
            throw new IllegalArgumentException("Pedido de aluguel é obrigatório");
        }
        
        if (contract.getSignatureDate() == null) {
            throw new IllegalArgumentException("Data de assinatura é obrigatória");
        }
        
        if (contract.getContractValue() == null) {
            throw new IllegalArgumentException("Valor do contrato é obrigatório");
        }
        
        if (contract.getContractValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor do contrato deve ser maior que zero");
        }
        
        // Validar dados de crédito se aplicável
        if (contract.hasCredit()) {
            if (contract.getBank() == null) {
                throw new IllegalArgumentException("Banco é obrigatório para contratos com crédito");
            }
            
            if (contract.getCreditAmount() == null || contract.getCreditAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Valor do crédito deve ser maior que zero");
            }
            
            if (contract.getInterestRate() == null || contract.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Taxa de juros deve ser maior ou igual a zero");
            }
        }
        
        // Validar datas se informadas
        if (contract.getStartDate() != null && contract.getEndDate() != null) {
            if (contract.getStartDate().isAfter(contract.getEndDate())) {
                throw new IllegalArgumentException("Data de início deve ser anterior à data de fim");
            }
        }
    }
}

