package com.sistema.aluguelcarros.controller;

import com.sistema.aluguelcarros.model.Car;
import com.sistema.aluguelcarros.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cars")
public class CarController {
    
    @Autowired
    private CarService carService;
    
    @GetMapping
    public String listCars(Model model,
                          @RequestParam(required = false) String brand,
                          @RequestParam(required = false) String model_param,
                          @RequestParam(required = false) Integer year,
                          @RequestParam(required = false) BigDecimal maxRate,
                          @RequestParam(required = false) String status) {
        
        List<Car> cars;
        
        if (brand != null || model_param != null || year != null || maxRate != null) {
            cars = carService.searchCars(brand, model_param, year, maxRate);
        } else if (status != null && !status.isEmpty()) {
            try {
                Car.CarStatus carStatus = Car.CarStatus.valueOf(status.toUpperCase());
                cars = carService.findByStatus(carStatus);
            } catch (IllegalArgumentException e) {
                cars = carService.findAll();
            }
        } else {
            cars = carService.findAll();
        }
        
        model.addAttribute("cars", cars);
        model.addAttribute("brand", brand);
        model.addAttribute("model", model_param);
        model.addAttribute("year", year);
        model.addAttribute("maxRate", maxRate);
        model.addAttribute("status", status);
        model.addAttribute("carStatuses", Car.CarStatus.values());
        
        return "cars/list";
    }
    
    @GetMapping("/available")
    public String listAvailableCars(Model model) {
        model.addAttribute("cars", carService.findAvailableCarsOrderByDailyRate());
        return "cars/available";
    }
    
    @GetMapping("/{id}")
    public String viewCar(@PathVariable Long id, Model model) {
        Optional<Car> car = carService.findById(id);
        if (car.isPresent()) {
            model.addAttribute("car", car.get());
            return "cars/view";
        }
        return "redirect:/cars";
    }
    
    @GetMapping("/new")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String newCarForm(Model model) {
        model.addAttribute("car", new Car());
        model.addAttribute("carStatuses", Car.CarStatus.values());
        model.addAttribute("ownerTypes", Car.OwnerType.values());
        return "cars/form";
    }
    
    @GetMapping("/{id}/edit")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String editCarForm(@PathVariable Long id, Model model) {
        Optional<Car> car = carService.findById(id);
        if (car.isPresent()) {
            model.addAttribute("car", car.get());
            model.addAttribute("carStatuses", Car.CarStatus.values());
            model.addAttribute("ownerTypes", Car.OwnerType.values());
            return "cars/form";
        }
        return "redirect:/cars";
    }
    
    @PostMapping
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String saveCar(@Valid @ModelAttribute("car") Car car,
                         BindingResult result,
                         Model model,
                         RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            model.addAttribute("carStatuses", Car.CarStatus.values());
            model.addAttribute("ownerTypes", Car.OwnerType.values());
            return "cars/form";
        }
        
        try {
            if (car.getId() == null) {
                carService.save(car);
                redirectAttributes.addFlashAttribute("successMessage", "Carro cadastrado com sucesso!");
            } else {
                carService.update(car);
                redirectAttributes.addFlashAttribute("successMessage", "Carro atualizado com sucesso!");
            }
            return "redirect:/cars";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao salvar carro: " + e.getMessage());
            model.addAttribute("carStatuses", Car.CarStatus.values());
            model.addAttribute("ownerTypes", Car.OwnerType.values());
            return "cars/form";
        }
    }
    
    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            carService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Carro excluído com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao excluir carro: " + e.getMessage());
        }
        return "redirect:/cars";
    }
    
    @PostMapping("/{id}/status")
    @PreAuthorize("hasRole('AGENT') or hasRole('ADMIN')")
    public String updateCarStatus(@PathVariable Long id,
                                 @RequestParam String status,
                                 RedirectAttributes redirectAttributes) {
        try {
            Car.CarStatus newStatus = Car.CarStatus.valueOf(status.toUpperCase());
            
            switch (newStatus) {
                case AVAILABLE:
                    carService.setCarAsAvailable(id);
                    break;
                case RENTED:
                    carService.setCarAsRented(id);
                    break;
                case MAINTENANCE:
                    carService.setCarInMaintenance(id);
                    break;
                default:
                    Optional<Car> car = carService.findById(id);
                    if (car.isPresent()) {
                        car.get().setStatus(newStatus);
                        carService.update(car.get());
                    }
            }
            
            redirectAttributes.addFlashAttribute("successMessage", "Status do carro atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro ao atualizar status: " + e.getMessage());
        }
        return "redirect:/cars/" + id;
    }
}

