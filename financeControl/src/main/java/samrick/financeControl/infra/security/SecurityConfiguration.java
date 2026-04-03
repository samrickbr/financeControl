package samrick.financeControl.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private SecurityFilter securityFilter;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(csrf -> csrf.disable()) //desativa a proteção contra csrf, tokens JWT já protegem contra isso em api stateless
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // a api não criará sessões no servidor
                .authorizeHttpRequests(req -> {
                    // Rotas públicas - sem autenticação
                    req.requestMatchers(HttpMethod.POST, "/login").permitAll(); //libera o login
                    req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll();  //libera o cadastro de usuarios
                    req.requestMatchers( "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();

                    // rotas de Administração (ex: deletar usuários
                    req.requestMatchers(HttpMethod.DELETE, "/usuarios/**").hasRole("ADMIN");

                    //Rotas de lançamentos (Acessível po admin, financeiro e comum
                    //Usamos hasAnyHole para permitir múltiplos perfis
                    req.requestMatchers("/lancamentos/**").hasAnyRole("ADMIN", "FINANCEIRO", "USER");

                    req.anyRequest().authenticated(); // bloqueia tudo o resto
                })
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration
                                                               configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
