package com.sistema.aluguelcarros.controller;

import com.sistema.aluguelcarros.model.*;
import com.sistema.aluguelcarros.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/contracts")
@PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
public class ContractController {
    
    @Autowired
    private ContractService contractService;
    
    @Autowired
    private RentalOrderService rentalOrderService;
    
    @Autowired
    private AgentService agentService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listContracts(Model model, @RequestParam(required = false) String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userService.findByEmail(email).orElse(null);
        
        List<Contract> contracts;
        
        if (currentUser != null && currentUser.getUserType() == User.UserType.AGENT) {
            Agent agent = (Agent) currentUser;
            if (agent.getAgentType() == Agent.AgentType.BANK) {
                // Bancos veem apenas seus contratos
                if (status != null && !status.isEmpty()) {
                    try {
                        Contract.ContractStatus contractStatus = Contract.ContractStatus.valueOf(status.toUpperCase());
                        contracts = contractService.findByBank(agent).stream()
                                .filter(contract -> contract.getStatus() == contractStatus)
                                .toList();
                    } catch (IllegalArgumentException e) {
                        contracts = contractService.findByBank(agent);
                    }
                } else {
                    contracts = contractService.findByBank(agent);
                }
            } else {
                // Empresas veem todos os contratos
                if (status != null && !status.isEmpty()) {
                    try {
                        Contract.ContractStatus contractStatus = Contract.ContractStatus.valueOf(status.toUpperCase());
                        contracts = contractService.findByStatus(contractStatus);
                    } catch (IllegalArgumentException e) {
                        contracts = contractService.findAllOrderBySignatureDateDesc();
                    }
                } else {
                    contracts = contractService.findAllOrderBySignatureDateDesc();
                }
            }
        } else {
            contracts = contractService.findAllOrderBySignatureDateDesc();
        }
        
        model.addAttribute("contracts", contracts);
        model.addAttribute("status", status);
        model.addAttribute("contractStatuses", Contract.ContractStatus.values());
        model.addAttribute("currentUser", currentUser);
        
        return "contracts/list";
    }
    
    @GetMapping("/{id}")
    public String viewContract(@PathVariable Long id, Model model) {
        Optional<Contract> contract = contractService.findById(id);
        if (contract.isPresent()) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userService.findByEmail(email).orElse(null);
            
            // Verificar se o usuário pode ver este contrato
            if (currentUser != null && currentUser.getUserType() == User.UserType.AGENT) {
                Agent agent = (Agent) currentUser;
                if (agent.getAgentType() == Agent.AgentType.BANK && 
                    contract.get().getBank() != null && 
                    !contract.get().getBank().getId().equals(agent.getId())) {
                    return "redirect:/contracts";
                }
            }
            
            model.addAttribute("contract", contract.get());
            model.addAttribute("totalWithInterest", contractService.calculateTotalWithInterest(contract.get()));
            model.addAttribute("currentUser", currentUser);
            return "contracts/view";
        }
        return "redirect:/contracts";
    }
    
    @GetMapping("/new")
    public String newContractForm(@RequestParam Long orderId, Model model) {
        Optional<RentalOrder> order = rentalOrderService.findById(orderId);
        if (order.isEmpty() || order.get().getStatus() != RentalOrder.OrderStatus.APPROVED) {
            return "redirect:/orders";
        }
        
        // Verificar se já existe contrato para este pedido
        Optional<Contract> existingContract = contractService.findByRentalOrder(order.get());
        if (existingContract.isPresent()) {
            return "redirect:/contracts/" + existingContract.get().getId();
        }
        
        model.addAttribute("order", order.get());
        model.addAttribute("contractTypes", Contract.ContractType.values());
        model.addAttribute("banks", agentService.findActiveAgentsByType(Agent.AgentType.BANK));
        
        return "contracts/form";
    }
    
    @PostMapping
    public String createContract(@RequestParam Long orderId,
                                @RequestParam String contractType,
                                @RequestParam(required = false) Long bankId,
                                @RequestParam(required = false) BigDecimal creditAmount,
                                @RequestParam(required = false) BigDecimal interestRate,
                                @RequestParam(required = false) String terms,
                                RedirectAttributes redirectAttributes) {
        
        try {
            Optional<RentalOrder> order = rentalOrderService.findById(orderId);
            if (order.isEmpty()) {
                throw new IllegalArgumentException("Pedido não encontrado");
            }
            
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            Agent agent = (Agent) userService.findByEmail(email).orElse(null);
            
            Contract contract;
            Contract.ContractType type = Contract.ContractType.valueOf(contractType.toUpperCase());
            
            if (type == Contract.ContractType.RENTAL_WITH_CREDIT || type == Contract.ContractType.CREDIT) {
                if (bankId == null || creditAmount == null || interestRate == null) {
                    throw new IllegalArgumentException("Dados de crédito são obrigatórios para este tipo de contrato");
                }
                
                Optional<Agent> bank = agentService.findById(bankId);
                if (bank.isEmpty() || bank.get().getAgentType() != Agent.AgentType.BANK) {
                    throw new IllegalArgumentException("Banco inválido");
                }
                
                contract = contractService.createCreditContract(order.get(), bank.get(), creditAmount, interestRate);
            } else {
                contract = contractService.createContract(order.get(), agent);
            }
            
            if (terms != null && !terms.trim().isEmpty()) {
                contract.setTerms(terms);
                contractService.update(contract);
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Contrato criado com sucesso!");
            return "redirect:/contracts/" + contract.getId();
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar contrato: " + e.getMessage());
            return "redirect:/orders/" + orderId;
        }
    }
    
    @PostMapping("/{id}/complete")
    public String completeContract(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contractService.completeContract(id);
            redirectAttributes.addFlashAttribute("successMessage", "Contrato concluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao concluir contrato: " + e.getMessage());
        }
        
        return "redirect:/contracts/" + id;
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelContract(@PathVariable Long id,
                                @RequestParam String reason,
                                RedirectAttributes redirectAttributes) {
        try {
            contractService.cancelContract(id, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Contrato cancelado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cancelar contrato: " + e.getMessage());
        }
        
        return "redirect:/contracts/" + id;
    }
    
    @PostMapping("/{id}/suspend")
    public String suspendContract(@PathVariable Long id,
                                 @RequestParam String reason,
                                 RedirectAttributes redirectAttributes) {
        try {
            contractService.suspendContract(id, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Contrato suspenso com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao suspender contrato: " + e.getMessage());
        }
        
        return "redirect:/contracts/" + id;
    }
    
    @PostMapping("/{id}/reactivate")
    public String reactivateContract(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            contractService.reactivateContract(id);
            redirectAttributes.addFlashAttribute("successMessage", "Contrato reativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao reativar contrato: " + e.getMessage());
        }
        
        return "redirect:/contracts/" + id;
    }
    
    @GetMapping("/with-credit")
    public String listContractsWithCredit(Model model) {
        model.addAttribute("contracts", contractService.findContractsWithCredit());
        return "contracts/with-credit";
    }
}

