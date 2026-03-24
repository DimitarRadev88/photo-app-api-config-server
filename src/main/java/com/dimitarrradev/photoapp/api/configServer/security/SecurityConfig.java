package com.dimitarrradev.photoapp.api.configServer.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);
    @Value("${spring.security.user.name}")
    private String adminUsername;
    @Value("${spring.security.user.password}")
    private String adminPassword;
    @Value("${spring.security.user.roles}")
    private String roleAdmin;

    @Value("${custom.security.user.name}")
    private String clientUsername;
    @Value("${custom.security.user.password}")
    private String clientPassword;
    @Value("${custom.security.user.roles}")
    private String clientRole;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {

        http.authorizeHttpRequests(registry -> registry
                        .requestMatchers(HttpMethod.POST, "/actuator/busrefresh").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/encrypt").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/decrypt").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/**").hasRole("CLIENT")
                        .anyRequest().authenticated()
                ).csrf(csrf -> csrf
                        .ignoringRequestMatchers("/actuator/busrefresh", "/encrypt", "/decrypt"))
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles(roleAdmin)
                .build();

        UserDetails client = User.builder()
                .username(clientUsername)
                .password(passwordEncoder.encode(clientPassword))
                .roles(clientRole)
                .build();

        return new InMemoryUserDetailsManager(admin, client);
    }
}
