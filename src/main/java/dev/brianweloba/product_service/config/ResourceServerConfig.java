package dev.brianweloba.product_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
// import org.springframework.security.config.Customizer;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;

@Configuration
// @EnableWebSecurity
public class ResourceServerConfig {

    // @Bean
    // SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    //     http.securityMatcher("/api/v1/**")
    //             .authorizeHttpRequests(authorize -> authorize
    //                     .requestMatchers(HttpMethod.PUT).hasAuthority("SCOPE_products.write")
    //                     .requestMatchers(HttpMethod.GET).hasAuthority("SCOPE_products.read")
    //                     .requestMatchers(HttpMethod.POST).hasAuthority("SCOPE_products.write")
    //                     .requestMatchers(HttpMethod.DELETE).hasAuthority("SCOPE_products.write")
    //                     .anyRequest().authenticated())
    //             .csrf(csrf -> csrf.disable())
    //             .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

    //     return http.build();
    // }
}
