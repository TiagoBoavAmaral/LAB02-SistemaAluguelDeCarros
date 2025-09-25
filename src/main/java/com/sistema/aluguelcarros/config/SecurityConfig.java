package com.sistema.aluguelcarros.config;

import com.sistema.aluguelcarros.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String redirectURL = request.getContextPath();
            
            if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"))) {
                redirectURL = "/dashboard";
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_AGENT"))) {
                redirectURL = "/dashboard";
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_CLIENT"))) {
                redirectURL = "/dashboard";
            }
            
            response.sendRedirect(redirectURL);
        };
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Páginas públicas
                .requestMatchers("/", "/home", "/about", "/contact").permitAll()
                .requestMatchers("/register", "/register/**").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                
                // Páginas que requerem autenticação
                .requestMatchers("/dashboard").authenticated()
                .requestMatchers("/profile").authenticated()
                
                // Carros - visualização pública, gerenciamento apenas para agentes
                .requestMatchers("/cars", "/cars/available", "/cars/{id}").permitAll()
                .requestMatchers("/cars/new", "/cars/*/edit", "/cars/*/delete", "/cars/*/status").hasAnyRole("AGENT", "ADMIN")
                
                // Pedidos - clientes podem criar e ver os seus, agentes podem gerenciar todos
                .requestMatchers("/orders/new").hasRole("CLIENT")
                .requestMatchers("/orders", "/orders/{id}").authenticated()
                .requestMatchers("/orders/pending", "/orders/*/evaluate", "/orders/*/complete").hasAnyRole("AGENT", "ADMIN")
                
                // Clientes - apenas agentes podem gerenciar
                .requestMatchers("/clients/**").hasAnyRole("AGENT", "ADMIN")
                
                // Contratos - apenas agentes
                .requestMatchers("/contracts/**").hasAnyRole("AGENT", "ADMIN")
                
                // Qualquer outra requisição requer autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(customAuthenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );
        
        // Desabilitar CSRF para H2 Console (apenas para desenvolvimento)
        http.csrf(csrf -> csrf
            .ignoringRequestMatchers("/h2-console/**")
        );
        
        // Permitir frames para H2 Console (apenas para desenvolvimento)
        http.headers(headers -> headers
            .frameOptions().sameOrigin()
        );
        
        return http.build();
    }
}

