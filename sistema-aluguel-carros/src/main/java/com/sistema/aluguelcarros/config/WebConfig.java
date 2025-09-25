package com.sistema.aluguelcarros.config;

import com.sistema.aluguelcarros.security.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/home").setViewName("index");
    }
    
    // Interceptor para adicionar informações do usuário logado em todas as views
    @Override
    public void addInterceptors(org.springframework.web.servlet.config.annotation.InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptorAdapter() {
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response,
                                 Object handler, ModelAndView modelAndView) throws Exception {
                
                if (modelAndView != null) {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    
                    if (auth != null && auth.isAuthenticated() && 
                        !"anonymousUser".equals(auth.getPrincipal())) {
                        
                        if (auth.getPrincipal() instanceof CustomUserDetailsService.CustomUserPrincipal) {
                            CustomUserDetailsService.CustomUserPrincipal userPrincipal = 
                                (CustomUserDetailsService.CustomUserPrincipal) auth.getPrincipal();
                            
                            modelAndView.addObject("currentUser", userPrincipal.getUser());
                            modelAndView.addObject("isAuthenticated", true);
                            modelAndView.addObject("isClient", 
                                userPrincipal.getUserType() == com.sistema.aluguelcarros.model.User.UserType.CLIENT);
                            modelAndView.addObject("isAgent", 
                                userPrincipal.getUserType() == com.sistema.aluguelcarros.model.User.UserType.AGENT);
                        }
                    } else {
                        modelAndView.addObject("isAuthenticated", false);
                        modelAndView.addObject("isClient", false);
                        modelAndView.addObject("isAgent", false);
                    }
                }
            }
        });
    }
}

