package com.demo.springsecurity.configuration;

import com.demo.springsecurity.configuration.security.JwtAuthenticationFilter;
import com.demo.springsecurity.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {


  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final UserServiceImpl userServiceImpl;
  
  @Autowired
  public SecurityConfig(
          JwtAuthenticationFilter jwtAuthenticationFilter, UserServiceImpl userServiceImpl) {

    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
      this.userServiceImpl = userServiceImpl;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider())
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/api/auth/**").permitAll().anyRequest().authenticated());

    httpSecurity.addFilterBefore(
        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userServiceImpl);
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }


  //  @Bean
  //  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
  //    httpSecurity
  //        .authorizeHttpRequests((requests) -> requests.requestMatchers("/api/public").permitAll()
  //                .requestMatchers("/api/admin").hasRole("ADMIN")
  //                .requestMatchers("/api/users").hasRole("USER"))
  //        .httpBasic(Customizer.withDefaults());
  //    return httpSecurity.build();
  //  }
  //
  //      @Bean
  //      public InMemoryUserDetailsManager userDetailsManager() {
  //          UserDetails user = User.builder()
  //                  .username("user")
  //                  .password("{noop}user123")
  //                  .roles("USER")
  //                  .build();
  //
  //          UserDetails admin = User.builder()
  //                  .username("admin")
  //                  .password("{noop}admin123")
  //                  .roles("ADMIN")
  //                  .build();
  //
  //          return new InMemoryUserDetailsManager(user, admin);
  //      }
}
