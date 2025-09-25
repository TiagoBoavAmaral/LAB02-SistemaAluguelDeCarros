package com.sistema.aluguelcarros.controller;

import com.sistema.aluguelcarros.service.*;
import com.sistema.aluguelcarros.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private RentalOrderService rentalOrderService;
    
    @Autowired
    private ContractService contractService;
    
    @GetMapping("/")
    public String home(Model model) {
        // Estatísticas gerais para a página inicial
        model.addAttribute("totalCars", carService.findAll().size());
        model.addAttribute("availableCars", carService.countByStatus(Car.CarStatus.AVAILABLE));
        model.addAttribute("activeOrders", rentalOrderService.countByStatus(RentalOrder.OrderStatus.ACTIVE));
        model.addAttribute("pendingOrders", rentalOrderService.countByStatus(RentalOrder.OrderStatus.PENDING));
        
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        
        User user = userService.findByEmail(email).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        
        if (user.getUserType() == User.UserType.CLIENT) {
            return clientDashboard(model, (Client) user);
        } else if (user.getUserType() == User.UserType.AGENT) {
            return agentDashboard(model, (Agent) user);
        }
        
        return "dashboard/general";
    }
    
    private String clientDashboard(Model model, Client client) {
        // Dados específicos do cliente
        model.addAttribute("client", client);
        model.addAttribute("myOrders", rentalOrderService.findByClientOrderByCreatedAtDesc(client));
        model.addAttribute("totalOrders", rentalOrderService.countByClient(client));
        model.addAttribute("availableCars", carService.findAvailableCarsOrderByDailyRate());
        
        return "dashboard/client";
    }
    
    private String agentDashboard(Model model, Agent agent) {
        // Dados específicos do agente
        model.addAttribute("agent", agent);
        model.addAttribute("pendingEvaluations", rentalOrderService.findPendingOrders());
        model.addAttribute("myEvaluations", rentalOrderService.findByEvaluatedBy(agent));
        
        if (agent.getAgentType() == Agent.AgentType.BANK) {
            model.addAttribute("myContracts", contractService.findByBank(agent));
            model.addAttribute("totalContracts", contractService.countByBank(agent));
        }
        
        return "dashboard/agent";
    }
    
    @GetMapping("/about")
    public String about() {
        return "about";
    }
    
    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }
}

