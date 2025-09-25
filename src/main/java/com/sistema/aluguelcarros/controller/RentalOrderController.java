package com.sistema.aluguelcarros.controller;

import com.sistema.aluguelcarros.model.*;
import com.sistema.aluguelcarros.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class RentalOrderController {
    
    @Autowired
    private RentalOrderService rentalOrderService;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private AgentService agentService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String listOrders(Model model, @RequestParam(required = false) String status) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userService.findByEmail(email).orElse(null);
        
        List<RentalOrder> orders;
        
        if (currentUser != null && currentUser.getUserType() == User.UserType.CLIENT) {
            // Cliente vê apenas seus pedidos
            Client client = (Client) currentUser;
            if (status != null && !status.isEmpty()) {
                try {
                    RentalOrder.OrderStatus orderStatus = RentalOrder.OrderStatus.valueOf(status.toUpperCase());
                    orders = rentalOrderService.findByClient(client).stream()
                            .filter(order -> order.getStatus() == orderStatus)
                            .toList();
                } catch (IllegalArgumentException e) {
                    orders = rentalOrderService.findByClient(client);
                }
            } else {
                orders = rentalOrderService.findByClientOrderByCreatedAtDesc(client);
            }
        } else {
            // Agentes veem todos os pedidos
            if (status != null && !status.isEmpty()) {
                try {
                    RentalOrder.OrderStatus orderStatus = RentalOrder.OrderStatus.valueOf(status.toUpperCase());
                    orders = rentalOrderService.findByStatus(orderStatus);
                } catch (IllegalArgumentException e) {
                    orders = rentalOrderService.findAll();
                }
            } else {
                orders = rentalOrderService.findAll();
            }
        }
        
        model.addAttribute("orders", orders);
        model.addAttribute("status", status);
        model.addAttribute("orderStatuses", RentalOrder.OrderStatus.values());
        model.addAttribute("currentUser", currentUser);
        
        return "orders/list";
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String listPendingOrders(Model model) {
        model.addAttribute("orders", rentalOrderService.findPendingOrders());
        return "orders/pending";
    }
    
    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Long id, Model model) {
        Optional<RentalOrder> order = rentalOrderService.findById(id);
        if (order.isPresent()) {
            // Verificar se o usuário atual pode ver este pedido
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth.getName();
            User currentUser = userService.findByEmail(email).orElse(null);
            
            if (currentUser != null && currentUser.getUserType() == User.UserType.CLIENT) {
                Client client = (Client) currentUser;
                if (!order.get().getClient().getId().equals(client.getId())) {
                    return "redirect:/orders";
                }
            }
            
            model.addAttribute("order", order.get());
            model.addAttribute("currentUser", currentUser);
            return "orders/view";
        }
        return "redirect:/orders";
    }
    
    @GetMapping("/new")
    @PreAuthorize("hasRole('CLIENT')")
    public String newOrderForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Client client = (Client) userService.findByEmail(email).orElse(null);
        
        if (client == null) {
            return "redirect:/login";
        }
        
        if (!clientService.isEligibleForRental(client)) {
            model.addAttribute("errorMessage", "Você não está elegível para aluguel. Verifique seus dados e empregos.");
            return "redirect:/dashboard";
        }
        
        RentalOrder order = new RentalOrder();
        order.setClient(client);
        order.setStartDate(LocalDate.now().plusDays(1));
        order.setEndDate(LocalDate.now().plusDays(2));
        
        model.addAttribute("order", order);
        model.addAttribute("availableCars", carService.findAvailableCarsOrderByDailyRate());
        
        return "orders/form";
    }
    
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public String saveOrder(@Valid @ModelAttribute("order") RentalOrder order,
                           BindingResult result,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Client client = (Client) userService.findByEmail(email).orElse(null);
        
        if (client == null) {
            return "redirect:/login";
        }
        
        order.setClient(client);
        
        if (result.hasErrors()) {
            model.addAttribute("availableCars", carService.findAvailableCarsOrderByDailyRate());
            return "orders/form";
        }
        
        try {
            rentalOrderService.createRentalOrder(
                client,
                order.getCar(),
                order.getStartDate(),
                order.getEndDate(),
                order.getObservations()
            );
            redirectAttributes.addFlashAttribute("successMessage", "Pedido criado com sucesso!");
            return "redirect:/orders";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao criar pedido: " + e.getMessage());
            model.addAttribute("availableCars", carService.findAvailableCarsOrderByDailyRate());
            return "orders/form";
        }
    }
    
    @GetMapping("/{id}/evaluate")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String evaluateOrderForm(@PathVariable Long id, Model model) {
        Optional<RentalOrder> order = rentalOrderService.findById(id);
        if (order.isPresent() && order.get().canBeEvaluated()) {
            model.addAttribute("order", order.get());
            model.addAttribute("orderStatuses", new RentalOrder.OrderStatus[]{
                RentalOrder.OrderStatus.APPROVED,
                RentalOrder.OrderStatus.REJECTED,
                RentalOrder.OrderStatus.UNDER_EVALUATION
            });
            return "orders/evaluate";
        }
        return "redirect:/orders";
    }
    
    @PostMapping("/{id}/evaluate")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String evaluateOrder(@PathVariable Long id,
                               @RequestParam String status,
                               @RequestParam String evaluationNotes,
                               RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        Agent agent = (Agent) userService.findByEmail(email).orElse(null);
        
        if (agent == null) {
            return "redirect:/login";
        }
        
        try {
            RentalOrder.OrderStatus newStatus = RentalOrder.OrderStatus.valueOf(status.toUpperCase());
            rentalOrderService.evaluateOrder(id, agent, newStatus, evaluationNotes);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido avaliado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao avaliar pedido: " + e.getMessage());
        }
        
        return "redirect:/orders/" + id;
    }
    
    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Long id,
                             @RequestParam(required = false) String reason,
                             RedirectAttributes redirectAttributes) {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        User currentUser = userService.findByEmail(email).orElse(null);
        
        // Verificar se o usuário pode cancelar este pedido
        Optional<RentalOrder> order = rentalOrderService.findById(id);
        if (order.isPresent()) {
            if (currentUser != null && currentUser.getUserType() == User.UserType.CLIENT) {
                Client client = (Client) currentUser;
                if (!order.get().getClient().getId().equals(client.getId())) {
                    redirectAttributes.addFlashAttribute("errorMessage", "Você não pode cancelar este pedido.");
                    return "redirect:/orders";
                }
            }
        }
        
        try {
            String cancelReason = reason != null ? reason : "Cancelado pelo usuário";
            rentalOrderService.cancelOrder(id, cancelReason);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido cancelado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cancelar pedido: " + e.getMessage());
        }
        
        return "redirect:/orders/" + id;
    }
    
    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String completeOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            rentalOrderService.completeOrder(id);
            redirectAttributes.addFlashAttribute("successMessage", "Pedido concluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao concluir pedido: " + e.getMessage());
        }
        
        return "redirect:/orders/" + id;
    }
}

