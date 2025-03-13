package com.gustavo.autocontrato.infra.security;

import com.gustavo.autocontrato.exception.CustomAuthenticationEntryPoint;
import com.gustavo.autocontrato.exception.exceptions.CustomAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private CorsFilter corsFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class) // CorsFilter deve ser antes do filtro de autenticação
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Adicionando o filtro de segurança
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/registrar").permitAll()

                        // Rota restrita para "USER"
                        .requestMatchers(HttpMethod.POST, "/proprietarios").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/proprietarios").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/proprietarios/status/{status}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/proprietarios/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/proprietarios/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/proprietarios/{id}/status").hasRole("USER")

                        // Outras rotas "USER"
                        .requestMatchers(HttpMethod.GET, "/propriedades").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/propriedades/todos").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/propriedades/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/propriedades/aluguel").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/propriedades").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/propriedades/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/propriedades/{id}/status").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/locatarios").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/locatarios/todos").hasRole("USER")
                        .requestMatchers(HttpMethod.GET, "/locatarios/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.POST, "/locatarios").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/locatarios/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/locatarios/{id}/status").hasRole("USER")

                        .requestMatchers(HttpMethod.GET, "/usuarios").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/usuarios/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/{id}/senha").hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH, "/usuarios/{id}/permissao").hasRole("ADMIN")

                        .anyRequest().permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
