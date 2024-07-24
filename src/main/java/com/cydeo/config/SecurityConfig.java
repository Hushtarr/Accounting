package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
@Configuration
public class SecurityConfig {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
                .antMatchers("/companies/**").hasAuthority("Root User")
                .antMatchers("/users/**").hasAuthority("Root User")
                .antMatchers("/users/**").hasAuthority("Admin")
                .antMatchers("/clientVendors/**").hasAuthority("Admin")
                .antMatchers("/categories/**").hasAuthority("Admin")
                .antMatchers("/products/**").hasAuthority("Admin")
                .antMatchers("/purchaseInvoices/**").hasAuthority("Admin")
                .antMatchers("/salesInvoices/**").hasAuthority("Admin")
                .antMatchers("/reports/**").hasAuthority("Admin")
                .antMatchers("/dashboard").hasAuthority("Manager")
                .antMatchers("/clientVendors/**").hasAuthority("Manager")
                .antMatchers("/categories/**").hasAuthority("Manager")
                .antMatchers("/products/**").hasAuthority("Manager")
                .antMatchers("/purchaseInvoices/**").hasAuthority("Manager")
                .antMatchers("/salesInvoices/**").hasAuthority("Manager")
                .antMatchers("/reports/**").hasAuthority("Manager")
                .antMatchers("/dashboard").hasAuthority("Employee")
                .antMatchers("/clientVendors/**").hasAuthority("Employee")
                .antMatchers("/categories/**").hasAuthority("Employee")
                .antMatchers("/products/**").hasAuthority("Employee")
                .antMatchers("/purchaseInvoices/**").hasAuthority("Employee")
                .antMatchers("/salesInvoices/**").hasAuthority("Employee")

                .antMatchers("/","/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authSuccessHandler)
                .failureUrl("/login?error=true")
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                .tokenValiditySeconds(864000)
                .key("cydeo") //can't be any key
                .userDetailsService(securityService)
                .and()
                .build();

    }





}
