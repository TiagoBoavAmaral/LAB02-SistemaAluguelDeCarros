package com.sistema.aluguelcarros.controller;

import com.sistema.aluguelcarros.model.Client;
import com.sistema.aluguelcarros.model.Agent;
import com.sistema.aluguelcarros.model.User;
import com.sistema.aluguelcarros.service.ClientService;
import com.sistema.aluguelcarros.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
public class AuthController {
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private AgentService agentService;
    
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }
    
    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("client", new Client());
        model.addAttribute("agent", new Agent());
        return "auth/register";
    }
    
    @PostMapping("/register/client")
    public String registerClient(@Valid @ModelAttribute("client") Client client,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        try {
            clientService.save(client);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente cadastrado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar cliente: " + e.getMessage());
            return "redirect:/register";
        }
    }
    
    @PostMapping("/register/agent")
    public String registerAgent(@Valid @ModelAttribute("agent") Agent agent,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "auth/register";
        }
        
        try {
            agentService.save(agent);
            redirectAttributes.addFlashAttribute("successMessage", "Agente cadastrado com sucesso! Faça login para continuar.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao cadastrar agente: " + e.getMessage());
            return "redirect:/register";
        }
    }
    
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "auth/access-denied";
    }
    
    @GetMapping("/profile")
    public String profile(Model model) {
        // O usuário atual será injetado pelo Spring Security
        return "auth/profile";
    }
}

