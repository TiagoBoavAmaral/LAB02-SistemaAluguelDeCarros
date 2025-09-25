package com.sistema.aluguelcarros.controller;

import com.sistema.aluguelcarros.model.Client;
import com.sistema.aluguelcarros.model.Employment;
import com.sistema.aluguelcarros.service.ClientService;
import com.sistema.aluguelcarros.service.EmploymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/clients")
@PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
public class ClientController {
    
    @Autowired
    private ClientService clientService;
    
    @Autowired
    private EmploymentService employmentService;
    
    @GetMapping
    public String listClients(Model model, @RequestParam(required = false) String search) {
        if (search != null && !search.trim().isEmpty()) {
            model.addAttribute("clients", clientService.findByNameContaining(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("clients", clientService.findAll());
        }
        return "clients/list";
    }
    
    @GetMapping("/{id}")
    public String viewClient(@PathVariable Long id, Model model) {
        Optional<Client> client = clientService.findById(id);
        if (client.isPresent()) {
            model.addAttribute("client", client.get());
            model.addAttribute("employments", employmentService.findByClient(client.get()));
            model.addAttribute("totalIncome", clientService.getTotalIncome(client.get()));
            return "clients/view";
        }
        return "redirect:/clients";
    }
    
    @GetMapping("/new")
    public String newClientForm(Model model) {
        model.addAttribute("client", new Client());
        return "clients/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editClientForm(@PathVariable Long id, Model model) {
        Optional<Client> client = clientService.findById(id);
        if (client.isPresent()) {
            model.addAttribute("client", client.get());
            return "clients/form";
        }
        return "redirect:/clients";
    }
    
    @PostMapping
    public String saveClient(@Valid @ModelAttribute("client") Client client,
                            BindingResult result,
                            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "clients/form";
        }
        
        try {
            if (client.getId() == null) {
                clientService.save(client);
                redirectAttributes.addFlashAttribute("successMessage", "Cliente cadastrado com sucesso!");
            } else {
                clientService.update(client);
                redirectAttributes.addFlashAttribute("successMessage", "Cliente atualizado com sucesso!");
            }
            return "redirect:/clients";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar cliente: " + e.getMessage());
            return "clients/form";
        }
    }
    
    @PostMapping("/{id}/delete")
    public String deleteClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clientService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir cliente: " + e.getMessage());
        }
        return "redirect:/clients";
    }
    
    @PostMapping("/{id}/deactivate")
    public String deactivateClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clientService.deactivateClient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente desativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao desativar cliente: " + e.getMessage());
        }
        return "redirect:/clients/" + id;
    }
    
    @PostMapping("/{id}/activate")
    public String activateClient(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clientService.activateClient(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente ativado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao ativar cliente: " + e.getMessage());
        }
        return "redirect:/clients/" + id;
    }
    
    // Gerenciamento de empregos
    @GetMapping("/{clientId}/employments/new")
    public String newEmploymentForm(@PathVariable Long clientId, Model model) {
        Optional<Client> client = clientService.findById(clientId);
        if (client.isPresent()) {
            if (!clientService.canAddMoreEmployments(client.get())) {
                model.addAttribute("errorMessage", "Cliente já possui o máximo de 3 empregos.");
                return "redirect:/clients/" + clientId;
            }
            
            Employment employment = new Employment();
            employment.setClient(client.get());
            model.addAttribute("employment", employment);
            model.addAttribute("client", client.get());
            return "clients/employment-form";
        }
        return "redirect:/clients";
    }
    
    @PostMapping("/{clientId}/employments")
    public String saveEmployment(@PathVariable Long clientId,
                                @Valid @ModelAttribute("employment") Employment employment,
                                BindingResult result,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        
        Optional<Client> client = clientService.findById(clientId);
        if (client.isEmpty()) {
            return "redirect:/clients";
        }
        
        if (result.hasErrors()) {
            model.addAttribute("client", client.get());
            return "clients/employment-form";
        }
        
        try {
            employment.setClient(client.get());
            employmentService.save(employment);
            redirectAttributes.addFlashAttribute("successMessage", "Emprego adicionado com sucesso!");
            return "redirect:/clients/" + clientId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao adicionar emprego: " + e.getMessage());
            model.addAttribute("client", client.get());
            return "clients/employment-form";
        }
    }
    
    @PostMapping("/{clientId}/employments/{employmentId}/delete")
    public String deleteEmployment(@PathVariable Long clientId,
                                  @PathVariable Long employmentId,
                                  RedirectAttributes redirectAttributes) {
        try {
            clientService.removeEmployment(clientId, employmentId);
            redirectAttributes.addFlashAttribute("successMessage", "Emprego removido com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao remover emprego: " + e.getMessage());
        }
        return "redirect:/clients/" + clientId;
    }
}

