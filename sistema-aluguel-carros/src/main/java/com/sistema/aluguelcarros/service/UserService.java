package com.sistema.aluguelcarros.service;

import com.sistema.aluguelcarros.model.User;
import com.sistema.aluguelcarros.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public List<User> findAll() {
        return userRepository.findAll();
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }
    
    public List<User> findByUserType(User.UserType userType) {
        return userRepository.findByUserType(userType);
    }
    
    public List<User> findActiveUsers() {
        return userRepository.findByActiveTrue();
    }
    
    public List<User> findByNameContaining(String name) {
        return userRepository.findByNameContaining(name);
    }
    
    public User save(User user) {
        validateUser(user);
        
        // Criptografar senha se for um novo usuário ou se a senha foi alterada
        if (user.getId() == null || !user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    public User update(User user) {
        if (user.getId() == null) {
            throw new IllegalArgumentException("ID do usuário não pode ser nulo para atualização");
        }
        
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        
        validateUser(user);
        
        // Se a senha não foi alterada, manter a senha existente
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user.setPassword(existingUser.get().getPassword());
        } else if (!user.getPassword().startsWith("$2a$")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        
        return userRepository.save(user);
    }
    
    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }
    
    public void deactivateUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setActive(false);
            userRepository.save(user.get());
        } else {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
    }
    
    public void activateUser(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            user.get().setActive(true);
            userRepository.save(user.get());
        } else {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
    }
    
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
    
    public boolean existsByCpf(String cpf) {
        return userRepository.existsByCpf(cpf);
    }
    
    public boolean isEmailAvailable(String email, Long userId) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isEmpty() || existingUser.get().getId().equals(userId);
    }
    
    public boolean isCpfAvailable(String cpf, Long userId) {
        Optional<User> existingUser = userRepository.findByCpf(cpf);
        return existingUser.isEmpty() || existingUser.get().getId().equals(userId);
    }
    
    private void validateUser(User user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }
        
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        
        if (user.getCpf() == null || user.getCpf().trim().isEmpty()) {
            throw new IllegalArgumentException("CPF é obrigatório");
        }
        
        if (user.getRg() == null || user.getRg().trim().isEmpty()) {
            throw new IllegalArgumentException("RG é obrigatório");
        }
        
        // Validar unicidade de email
        if (!isEmailAvailable(user.getEmail(), user.getId())) {
            throw new IllegalArgumentException("Email já está em uso");
        }
        
        // Validar unicidade de CPF
        if (!isCpfAvailable(user.getCpf(), user.getId())) {
            throw new IllegalArgumentException("CPF já está em uso");
        }
        
        // Validar formato do CPF (básico)
        if (!isValidCpf(user.getCpf())) {
            throw new IllegalArgumentException("CPF inválido");
        }
    }
    
    private boolean isValidCpf(String cpf) {
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");
        
        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Aqui poderia implementar a validação completa do CPF
        // Por simplicidade, vamos considerar válido se passou nas verificações básicas
        return true;
    }
}

