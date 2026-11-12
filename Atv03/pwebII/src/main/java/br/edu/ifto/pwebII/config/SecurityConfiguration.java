package br.edu.ifto.pwebII.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   UsuarioDetailsService usuarioDetailsService) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos e páginas públicas
                .requestMatchers("/css/**", "/js/**", "/images/**", "/error").permitAll()
                .requestMatchers("/", "/login").permitAll()

                // Cadastro público de pacientes (auto-cadastro de visitantes)
                .requestMatchers("/paciente/form").permitAll()
                .requestMatchers(HttpMethod.POST, "/paciente/save").permitAll()

                // Atalhos para consultas do usuário autenticado
                .requestMatchers("/paciente/minhas-consultas", "/medico/minha-agenda").authenticated()

                // Regras para módulo de Pacientes
                .requestMatchers("/paciente/remove/**").hasRole("ADMIN")
                .requestMatchers("/paciente/edit/**", "/paciente/update").hasAnyRole("ADMIN", "SECRETARIO")
                .requestMatchers("/paciente/list").hasAnyRole("ADMIN", "SECRETARIO", "MEDICO")
                .requestMatchers("/paciente/consultas/**").hasAnyRole("ADMIN", "SECRETARIO", "MEDICO", "PACIENTE")

                // Regras para módulo de Médicos
                .requestMatchers("/medico/form", "/medico/save", "/medico/edit/**", "/medico/update", "/medico/remove/**").hasRole("ADMIN")
                .requestMatchers("/medico/list").hasAnyRole("ADMIN", "SECRETARIO", "MEDICO", "PACIENTE")
                .requestMatchers("/medico/consultas/**").hasAnyRole("ADMIN", "SECRETARIO", "MEDICO")

                // Regras para módulo de Consultas
                .requestMatchers("/consulta/remove/**").hasAnyRole("ADMIN", "SECRETARIO")
                .requestMatchers("/consulta/edit/**", "/consulta/update").hasAnyRole("ADMIN", "SECRETARIO", "MEDICO")
                .requestMatchers("/consulta/list").hasAnyRole("ADMIN", "SECRETARIO", "MEDICO")
                .requestMatchers("/consulta/form", "/consulta/save").hasAnyRole("ADMIN", "SECRETARIO", "PACIENTE", "MEDICO")

                // Console H2 (para suporte a desenvolvimento e testes)
                .requestMatchers("/h2-console/**").permitAll()

                // Qualquer outra requisição exige autenticação
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .httpBasic(withDefaults())
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            )
            .rememberMe(remember -> remember
                .userDetailsService(usuarioDetailsService)
            );

        return http.build();
    }

    /**
     * Provedor de autenticação com busca no banco de dados e senhas codificadas com BCrypt.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider(UsuarioDetailsService usuarioDetailsService,
                                                           PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(usuarioDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
